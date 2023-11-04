package com.andy.vertx_starter.p4_eventbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

/**
 * Demonstrates the Request-Response messaging pattern using the Vert.x Event Bus.
 *
 * <p>The Request-Response messaging pattern is a two-way communication pattern where a requester
 * sends a message with the expectation of receiving a response. It's akin to a synchronous
 * operation in an asynchronous system, where the requester waits for the responder to process
 * the message and return a reply.</p>
 *
 * <p>This pattern is commonly used for operations where the sender requires confirmation that the
 * message has been processed or needs additional data from the receiver to continue processing.</p>
 *
 * <p>In Vert.x, the Request-Response pattern is built into the Event Bus API. The Event Bus
 * provides an easy way to send messages and wait for replies using the 'request' and 'reply'
 * methods. The requester can send a message and provide a handler that will be called when the
 * response comes back. On the other side, the responder listens for incoming messages, processes
 * them, and can reply using the 'reply' method of the message object.</p>
 */
public class RequestResponseExample {

    static final String ADDRESS = "my.request.address";

    public static void main(String[] args) {
        var vertx = Vertx.vertx();
        vertx.deployVerticle(new RequestVerticle());
        vertx.deployVerticle(new ResponseVerticle());
    }

    /**
     * RequestVerticle is a Vert.x verticle that acts as a requester.
     * It sends a message to a predefined address and logs the response.
     */
    public static class RequestVerticle extends AbstractVerticle {

        private static final Logger LOG = LoggerFactory.getLogger(RequestVerticle.class);

        @Override
        public void start(final Promise<Void> startPromise) throws Exception {
            startPromise.complete();
            var eventBus = vertx.eventBus();
            final String message = "Hello World!";
            LOG.debug("Sending: {}", message);
            // Send a request and handle the response asynchronously
            eventBus.<String>request(ADDRESS, message, reply -> {
                if (reply.succeeded()) {
                    LOG.debug("Response: {}", reply.result().body());
                } else {
                    LOG.error("Failed to get response: {}", reply.cause().getMessage());
                }
            });
        }
    }

    /**
     * ResponseVerticle is a Vert.x verticle that acts as a responder.
     * It listens for incoming messages on a predefined address and replies to them.
     */
    public static class ResponseVerticle extends AbstractVerticle {
        private static final Logger LOG = LoggerFactory.getLogger(ResponseVerticle.class);

        @Override
        public void start(final Promise<Void> startPromise) throws Exception {
            startPromise.complete();
            // Set up a consumer that replies to incoming messages
            vertx.eventBus().<String>consumer(ADDRESS, message -> {
                LOG.debug("Received Message: {}", message.body());
                // Reply to the received message
                message.reply("Received your message. Thanks!");
            });
        }
    }
}
