package com.andy.p2_verticles.p1_hello;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> The HelloVerticle class is a Vert.x verticle that demonstrates the use of asynchronous notification
 * of life-cycle events. This verticle sets up a periodic task and creates an HTTP server. </p>
 *
 * <p> start(Promise) method initializes the verticle, sets up a periodic task, and creates an HTTP server. </p>
 *
 * <p> The verticle uses the AbstractVerticle base class and overrides its start(Promise) method to define its behavior.</p>
 */

public class HelloVerticle extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(HelloVerticle.class);

    private long counter = 1;

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HelloVerticle());
    }

    @Override
    public void start(Promise<Void> startPromise) {

        vertx.setPeriodic(5000, id -> {
            logger.info("tick");
        });

        vertx.createHttpServer()
                .requestHandler(req -> {
                    logger.info("Request #{} from {}", counter++, req.remoteAddress().host());
                    req.response().end("Hello!");
                })
                .listen(8080, http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        logger.info("HTTP server started on port 8080");
                    } else {
                        startPromise.fail(http.cause());
                    }
                });

        logger.info("Open http://localhost:8080/");
    }
}
