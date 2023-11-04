package com.andy.vertx_starter.p4_eventbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

/**
 * Demonstrates the point-to-point messaging pattern using the Vert.x Event Bus.
 *
 * <p>Point-to-point messaging systems are based on the concept of message queues and
 * senders/receivers. Each message is addressed to a specific queue, and receiving clients
 * extract messages from the queues established to hold their incoming messages.</p>
 *
 * <p>In point-to-point communication, a message is kept in the queue until it is consumed
 * or until it expires. Only one consumer will receive and process the message. If several
 * consumers are listening on the same queue, the system ensures that the message is delivered
 * to only one of them.</p>
 *
 * <p>In Vert.x, the Event Bus supports point-to-point messaging where you can send messages
 * to addresses (which can be seen as queues). A typical point-to-point setup involves one sender
 * and one receiver, although it's possible to have multiple receivers where only one will
 * receive a particular message (competing consumers).</p>
 *
 * <p>This example has a Sender verticle, which sends a message every second to an address,
 * and a Receiver verticle, which listens on that address and logs any messages it receives.
 * This is a simple use case of the point-to-point model within Vert.x's asynchronous
 * and event-driven architecture.</p>
 */
public class PointToPointExample {

    public static void main(String[] args) {
        var vertx = Vertx.vertx();
        vertx.deployVerticle(new Sender());
        vertx.deployVerticle(new Receiver());
    }

    /**
     * The Sender class is an implementation of a Vert.x verticle responsible for sending messages.
     * It periodically sends messages to a specific address on the Event Bus, illustrating
     * the 'point' in point-to-point messaging, where it serves as the single point of origin for messages.
     */
    public static class Sender extends AbstractVerticle {
        @Override
        public void start(final Promise<Void> startPromise) throws Exception {
            startPromise.complete();
            vertx.setPeriodic(1000, id -> {
                // Send a message every second
                vertx.eventBus().send(Sender.class.getName(), "Sending a message...");
            });
        }
    }

    /**
     * The Receiver class is an implementation of a Vert.x verticle that consumes messages.
     * It listens to a specific address on the Event Bus for messages and acts as the 'point'
     * in point-to-point messaging that receives and processes the message. If multiple receivers
     * are registered, only one will consume any given message.
     */
    public static class Receiver extends AbstractVerticle {

        private static final Logger LOG = LoggerFactory.getLogger(Receiver.class);

        @Override
        public void start(final Promise<Void> startPromise) throws Exception {
            startPromise.complete();
            vertx.eventBus().<String>consumer(Sender.class.getName(), message -> {
                LOG.debug("Received: {}", message.body());
            });
        }
    }
}
