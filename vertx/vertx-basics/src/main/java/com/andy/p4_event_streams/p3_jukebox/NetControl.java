package com.andy.p4_event_streams.p3_jukebox;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NetControl is a Vert.x verticle that handles TCP connections for controlling
 * a jukebox application. It uses a NetServer to accept connections on port 3000
 * and parses incoming data as commands using RecordParser to delineate messages
 * by newline characters.
 */
public class NetControl extends AbstractVerticle {


    private final Logger logger = LoggerFactory.getLogger(NetControl.class);

    @Override
    public void start() {
        logger.info("Start");
        vertx.createNetServer()
                .connectHandler(this::handleClient)
                .listen(3000);
    }

    /**
     * Handles a new client connection. Sets up a RecordParser to parse incoming
     * data based on delimiter (newline) and passes the parsed buffer to handleBuffer.
     *
     * @param socket The NetSocket representing the client connection.
     */
    private void handleClient(NetSocket socket) {
        logger.info("New connection");
        RecordParser.newDelimited("\n", socket)
                .handler(buffer -> handleBuffer(socket, buffer))
                .endHandler(v -> logger.info("Connection ended"));
    }


    /**
     * Handles a new client connection. Sets up a RecordParser to parse incoming
     * data based on delimiter (newline) and passes the parsed buffer to handleBuffer.
     *
     * @param socket The NetSocket representing the client connection.
     */
    private void handleBuffer(NetSocket socket, Buffer buffer) {
        String command = buffer.toString();
        switch (command) {
            case "/list":
                listCommand(socket);
                break;
            case "/play":
                vertx.eventBus().send("jukebox.play", "");
                break;
            case "/pause":
                vertx.eventBus().send("jukebox.pause", "");
                break;
            default:
                if (command.startsWith("/schedule ")) {
                    schedule(command);
                } else {
                    socket.write("Unknown command\n");
                }
        }
    }


    private void schedule(String command) {
        String track = command.substring(10);
        JsonObject json = new JsonObject().put("file", track);
        vertx.eventBus().send("jukebox.schedule", json);
    }

    private void listCommand(NetSocket socket) {
        vertx.eventBus().request("jukebox.list", "", reply -> {
            if (reply.succeeded()) {
                JsonObject data = (JsonObject) reply.result().body();
                data.getJsonArray("files")
                        .stream().forEach(name -> socket.write(name + "\n"));
            } else {
                logger.error("/list error", reply.cause());
            }
        });
    }

}
