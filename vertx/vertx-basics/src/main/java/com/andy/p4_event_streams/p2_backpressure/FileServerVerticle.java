package com.andy.p4_event_streams.p2_backpressure;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServerResponse;

/**
 * A Vert.x verticle that demonstrates the use of backpressure when serving large files over HTTP.
 */
public class FileServerVerticle extends AbstractVerticle {

    private static final int PORT = 8080;

    /**
     * Handles writing file data to the HTTP response while managing backpressure.
     * <p>
     * When the write queue of the response is full, reading from the file is paused.
     * This avoids buffering too much data in memory when the client is slow to consume the data.
     * Once the client has caught up and the buffer has drained (the drain handler is called),
     * file reading resumes.
     * <p>
     * This approach ensures that the server remains responsive and memory usage is kept under control,
     * even when serving large files or dealing with slow clients.
     */
    @Override
    public void start(Promise<Void> startPromise) {
        HttpServer server = vertx.createHttpServer();

        server.requestHandler(request -> {
            // Assuming the file name is provided in the URL path.
            String fileName = request.path().substring(1);

            // Opens the file asynchronously, applying backpressure handling when streaming.
            vertx.fileSystem().open(fileName, new OpenOptions(), readResult -> {
                if (readResult.succeeded()) {
                    AsyncFile file = readResult.result();
                    HttpServerResponse response = request.response();
                    response.setStatusCode(200)
                            .putHeader("Content-Type", "application/octet-stream")
                            .setChunked(true);

                    file.handler(buffer -> {
                                response.write(buffer);
                                if (response.writeQueueFull()) {
                                    file.pause(); // Pause reading from the file to prevent memory overflow.
                                    // Resume reading from the file when the write queue has space again.
                                    response.drainHandler(done -> file.resume());
                                }
                            }).endHandler(v -> response.end()) // Close the response when the file is completely written.
                            .exceptionHandler(th -> {
                                th.printStackTrace();
                                response.close(); // Close the response on error.
                            });

                } else {
                    request.response().setStatusCode(404).end("File not found");
                }
            });
        });

        // Start listening on the specified port.
        server.listen(PORT, result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listening on port " + PORT);
                startPromise.complete();
            } else {
                System.out.println("Failed to bind to port " + PORT);
                startPromise.fail(result.cause());
            }
        });
    }
}
