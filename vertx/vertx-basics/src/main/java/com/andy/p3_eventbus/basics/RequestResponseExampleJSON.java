package com.andy.p3_eventbus.basics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * This example demonstrates the Request-Response pattern using JSON objects for structured data exchange.
 * It makes use of Vert.x's built-in support for encoding and decoding JSON objects and arrays over the Event Bus.
 */
public class RequestResponseExampleJSON {

    public static void main(String[] args) {
        var vertx = Vertx.vertx();
        vertx.deployVerticle(new RequestVerticle());
        vertx.deployVerticle(new ResponseVerticle());
    }

    /**
     * Verticle that sends JSON objects as requests over the Event Bus and logs JSON array responses.
     */
    public static class RequestVerticle extends AbstractVerticle {

        private static final Logger LOG = LoggerFactory.getLogger(RequestVerticle.class);
        static final String ADDRESS = "my.request.address";

        @Override
        public void start(final Promise<Void> startPromise) throws Exception {
            startPromise.complete();
            var eventBus = vertx.eventBus();
            // Construct a JSON object to be used as a request message
            final var message = new JsonObject()
                    .put("message", "Hello World!")
                    .put("version", 1);
            LOG.debug("Sending: {}", message);
            // Request with a JSON object and expect a JSON array as a response
            eventBus.<JsonArray>request(ADDRESS, message, reply -> {
                if (reply.succeeded()) {
                    LOG.debug("Response: {}", reply.result().body());
                } else {
                    LOG.error("Response failed", reply.cause());
                }
            });
        }
    }

    /**
     * Verticle that listens for JSON object requests and responds with JSON arrays.
     */
    public static class ResponseVerticle extends AbstractVerticle {

        private static final Logger LOG = LoggerFactory.getLogger(ResponseVerticle.class);

        @Override
        public void start(final Promise<Void> startPromise) throws Exception {
            startPromise.complete();
            // Set up a consumer that processes received JSON object messages and replies with a JSON array
            vertx.eventBus().<JsonObject>consumer(RequestVerticle.ADDRESS, message -> {
                LOG.debug("Received Message: {}", message.body());
                // Create and reply with a JSON array
                message.reply(new JsonArray().add("one").add("two").add("three"));
            });
        }
    }
}
