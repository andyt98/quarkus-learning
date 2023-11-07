package com.andy.p4_event_streams.p3_jukebox;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
/**
 * The Jukebox class is a Vert.x verticle that simulates a music jukebox.
 * It allows for tracks to be listed, scheduled for playback, and managed via an EventBus.
 * It also includes functionality to stream audio to HTTP clients and support file downloads.
 * <p>
 * The class demonstrates the use of Vert.x's event-driven nature and the implementation of backpressure for
 * stream handling. The EventBus is used to communicate between different parts of the application,
 * while streaming is managed by writing chunks of audio to HTTP responses.
 * <p>
 * Backpressure is implicitly handled in the streaming part, where if the response's write queue is full,
 * the file reading is paused, and it is resumed once the queue is available to receive more data. This ensures
 * that the system does not run out of memory or lose data if the client cannot process the data as fast as it is
 * being sent.
 */
public class Jukebox extends AbstractVerticle {

    public static final String TRACKS_PATH = "tracks";

    private enum State {PLAYING, PAUSED}

    private final Logger logger = LoggerFactory.getLogger(Jukebox.class);
    private State currentMode = State.PAUSED;
    private final Queue<String> playlist = new ArrayDeque<>();
    private final Set<HttpServerResponse> streamers = new HashSet<>();
    private AsyncFile currentFile;
    private long positionInFile;

    @Override
    public void start() {
        logger.info("Start");

        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("jukebox.list", this::list);
        eventBus.consumer("jukebox.schedule", this::schedule);
        eventBus.consumer("jukebox.play", this::play);
        eventBus.consumer("jukebox.pause", this::pause);

        vertx.createHttpServer()
                .requestHandler(this::httpHandler)
                .listen(8080);

        vertx.setPeriodic(100, this::streamAudioChunk);
    }


    private void list(Message<?> request) {
        vertx.fileSystem().readDir(TRACKS_PATH, ".*mp3$", ar -> {
            if (ar.succeeded()) {
                List<String> files = ar.result()
                        .stream()
                        .map(File::new)
                        .map(File::getName)
                        .collect(Collectors.toList());
                JsonObject json = new JsonObject().put("files", new JsonArray(files));
                request.reply(json);
            } else {
                logger.error("readDir failed", ar.cause());
                request.fail(500, ar.cause().getMessage());
            }
        });
    }

    private void play(Message<?> request) {
        logger.info("Play");
        currentMode = State.PLAYING;
    }

    private void pause(Message<?> request) {
        logger.info("Pause");
        currentMode = State.PAUSED;
    }

    private void schedule(Message<JsonObject> request) {
        String file = request.body().getString("file");
        logger.info("Scheduling {}", file);
        if (playlist.isEmpty() && currentMode == State.PAUSED) {
            currentMode = State.PLAYING;
        }
        playlist.offer(file);
    }


    private void httpHandler(HttpServerRequest request) {
        logger.info("{} '{}' {}", request.method(), request.path(), request.remoteAddress());
        if ("/".equals(request.path())) {
            openAudioStream(request);
            return;
        }
        if (request.path().startsWith("/download/")) {
            String sanitizedPath = request.path().substring(10).replaceAll("/", "");
            download(sanitizedPath, request);
            return;
        }
        request.response().setStatusCode(404).end();
    }


    private void openAudioStream(HttpServerRequest request) {
        logger.info("New streamer");
        HttpServerResponse response = request.response()
                .putHeader("Content-Type", "audio/mpeg")
                .setChunked(true);
        streamers.add(response);
        response.endHandler(v -> {
            streamers.remove(response);
            logger.info("A streamer left");
        });
    }


    private void download(String path, HttpServerRequest request) {
        String file = TRACKS_PATH + "/" + path;
        if (!vertx.fileSystem().existsBlocking(file)) {
            request.response().setStatusCode(404).end();
            return;
        }
        OpenOptions opts = new OpenOptions().setRead(true);
        vertx.fileSystem().open(file, opts, ar -> {
            if (ar.succeeded()) {
                downloadFile(ar.result(), request);
            } else {
                logger.error("Read failed", ar.cause());
                request.response().setStatusCode(500).end();
            }
        });
    }


    /**
     * Handles the download of a file by streaming it to the HTTP server response.
     * This method demonstrates the concept of backpressure in an asynchronous, non-blocking IO operation.
     * <p>
     * As the file is read, chunks of data (buffers) are written to the HTTP response. If at any point the response
     * indicates that its write queue is full (a condition of backpressure), the reading from the file is paused
     * to avoid overwhelming the client. Once the client has caught up and the write queue of the response is
     * ready to handle more data, the reading from the file is resumed.
     * <p>
     * This mechanism ensures that the server does not run out of memory by trying to load too much data into the
     * response buffer and that the client is not overwhelmed by data it may not be able to process quickly enough.
     *
     * @param file    The AsyncFile instance representing the file to be downloaded.
     * @param request The HttpServerRequest instance representing the client's request for the file.
     */
    private void downloadFile(AsyncFile file, HttpServerRequest request) {
        HttpServerResponse response = request.response();
        response.setStatusCode(200)
                .putHeader("Content-Type", "audio/mpeg")
                .setChunked(true);

        file.handler(buffer -> {
            response.write(buffer);
            if (response.writeQueueFull()) {
                file.pause();
                response.drainHandler(v -> file.resume());
            }
        });

        file.endHandler(v -> response.end());
    }

    /**
     * Streams the contents of a file to the HTTP response using Vert.x's pipeTo feature.
     * The pipeTo method automatically manages backpressure, ensuring that the server does
     * not read from the file faster than the response can write to the client.
     * <p>
     * This approach is advantageous because it requires less boilerplate code and reduces
     * the complexity of handling the data flow control manually.
     *
     * @param file    The AsyncFile instance representing the file to be streamed to the client.
     * @param request The HttpServerRequest instance representing the client's request.
     */
    private void downloadFilePipe(AsyncFile file, HttpServerRequest request) {
        HttpServerResponse response = request.response();
        response.setStatusCode(200)
                .putHeader("Content-Type", "audio/mpeg")
                .setChunked(true);
        file.pipeTo(response);
    }


    private void streamAudioChunk(long id) {
        if (currentMode == State.PAUSED) {
            return;
        }
        if (currentFile == null && playlist.isEmpty()) {
            currentMode = State.PAUSED;
            return;
        }
        if (currentFile == null) {
            openNextFile();
        }
        currentFile.read(Buffer.buffer(4096), 0, positionInFile, 4096, ar -> {
            if (ar.succeeded()) {
                processReadBuffer(ar.result());
            } else {
                logger.error("Read failed", ar.cause());
                closeCurrentFile();
            }
        });
    }


    private void openNextFile() {
        logger.info("Opening {}", playlist.peek());
        OpenOptions opts = new OpenOptions().setRead(true);
        currentFile = vertx.fileSystem()
                .openBlocking(TRACKS_PATH + "/" + playlist.poll(), opts);
        positionInFile = 0;
    }

    private void closeCurrentFile() {
        logger.info("Closing file");
        positionInFile = 0;
        currentFile.close();
        currentFile = null;
    }

    /**
     * Processes the buffer read from the current file and writes it to all connected streamers.
     * This method ensures that we do not overload the client by checking if the write queue of
     * the streamer is full before writing to it. If the write queue is full, the method does
     * not write to that streamer, effectively implementing a simple form of backpressure.
     * <p>
     * Backpressure is a flow control mechanism that prevents overwhelming the receiver (streamer)
     * with data faster than it can process. This manual check helps to avoid buffer overflow and
     * potential out-of-memory errors that may occur if the server continuously sends data without
     * regard for the receiver's capacity to handle it.
     *
     * @param buffer The Buffer instance containing the data read from the file.
     */
    private void processReadBuffer(Buffer buffer) {
        logger.info("Read {} bytes from pos {}", buffer.length(), positionInFile);
        positionInFile += buffer.length();
        if (buffer.length() == 0) {
            closeCurrentFile();
            return;
        }
        for (HttpServerResponse streamer : streamers) {
            if (!streamer.writeQueueFull()) {
                streamer.write(buffer.copy());
            }
        }
    }
}
