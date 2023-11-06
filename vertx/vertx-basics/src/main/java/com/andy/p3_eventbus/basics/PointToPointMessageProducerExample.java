package com.andy.p3_eventbus.basics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageProducer;

/**
 * Demonstrates the use of a MessageProducer for point-to-point messaging with the Vert.x Event Bus.
 *
 * <p>The MessageProducer is a convenient abstraction for sending messages to a specific event bus address.
 * It allows the sender to keep a reference to a producer and send messages through it without specifying
 * the address each time.</p>
 */
public class PointToPointMessageProducerExample {

    public static void main(String[] args) {
        var vertx = Vertx.vertx();
        vertx.deployVerticle(new Sender());
        vertx.deployVerticle(new Receiver());
    }

    /**
     * Sender verticle that uses a MessageProducer to send messages at a fixed interval.
     *
     * <p>Upon starting, the Sender creates a MessageProducer targeting a specific address.
     * It then periodically sends messages to that address. When the verticle is stopped,
     * it closes the MessageProducer, which is an example of proper resource cleanup.</p>
     */
    public static class Sender extends AbstractVerticle {

        private MessageProducer<String> messageProducer;

        @Override
        public void start(final Promise<Void> startPromise) throws Exception {
            startPromise.complete();
            // Create a MessageProducer that sends messages to an address identified by the Sender class name
            messageProducer = vertx.eventBus().sender(Sender.class.getName());
            vertx.setPeriodic(1000, id -> {
                // Use the MessageProducer to send a message every second
                messageProducer.write("Sending a message...");
            });
        }

        @Override
        public void stop(final Promise<Void> stopPromise) throws Exception {
            // Close the MessageProducer when the verticle is stopped
            messageProducer.close(whenDone -> stopPromise.complete());
        }
    }

    /**
     * Receiver verticle that listens for messages from a MessageProducer on a specific address.
     *
     * <p>The Receiver sets up a consumer on the event bus to listen for messages at the address
     * provided by the Sender. It logs the received messages for debugging purposes.</p>
     */
    public static class Receiver extends AbstractVerticle {

        private static final Logger LOG = LoggerFactory.getLogger(Receiver.class);

        @Override
        public void start(final Promise<Void> startPromise) throws Exception {
            startPromise.complete();
            // Set up a consumer to receive messages sent by the Sender's MessageProducer
            vertx.eventBus().<String>consumer(Sender.class.getName(), message -> {
                LOG.debug("Received: {}", message.body());
            });
        }
    }
}
