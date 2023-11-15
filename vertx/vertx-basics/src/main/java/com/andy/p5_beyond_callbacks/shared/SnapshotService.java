package com.andy.p5_beyond_callbacks.shared;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SnapshotService is a Vert.x verticle that sets up an HTTP server to log temperature data.
 * It accepts POST requests with JSON payloads, logging the data and responding to the sender.
 * Requests must use POST method and 'application/json' Content-Type, otherwise a 400 Bad Request
 * response is returned. The server listens on a configurable port, defaulting to 4000.
 */
public class SnapshotService extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(SnapshotService.class);

    @Override
    public void start() {
        vertx.createHttpServer()
                .requestHandler(req -> {
                    if (badRequest(req)) {
                        req.response().setStatusCode(400).end();
                    }
                    req.bodyHandler(buffer -> {
                        logger.info("Latest temperatures: {}", buffer.toJsonObject().encodePrettily());
                        req.response().end();
                    });
                })
                .listen(config().getInteger("http.port", 4000));
    }

    private boolean badRequest(HttpServerRequest req) {
        return !req.method().equals(HttpMethod.POST) ||
                !"application/json".equals(req.getHeader("Content-Type"));
    }
}
