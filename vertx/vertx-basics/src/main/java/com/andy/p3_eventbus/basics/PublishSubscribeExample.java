package com.andy.p3_eventbus.basics;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * This example demonstrates the Publish/Subscribe messaging pattern.
 * In this pattern, messages are published to a particular address and are delivered to all
 * registered subscribers. Unlike point-to-point messaging, publish/subscribe allows multiple consumers
 * to receive the message.
 * <p>
 * In Vert.x, the EventBus supports publish/subscribe messaging allowing different parts of your application
 * to communicate with each other irrespective of what language they are written in, or where they are located.
 * <p>
 * The publish/subscribe pattern in Vert.x is inherently asynchronous and supports the distribution of messages
 * across different Vert.x instances.
 */
public class PublishSubscribeExample {

    public static void main(String[] args) {
        var vertx = Vertx.vertx();
        vertx.deployVerticle(new Publish());
        vertx.deployVerticle(new Subscriber1());
        vertx.deployVerticle(Subscriber2.class.getName(), new DeploymentOptions().setInstances(2));
    }

    /**
     * Publisher verticle that periodically publishes messages to all subscribers.
     */
    public static class Publish extends AbstractVerticle {
        @Override
        public void start(final Promise<Void> startPromise) {
            vertx.setPeriodic(Duration.ofSeconds(10).toMillis(), id ->
                    vertx.eventBus().publish(Publish.class.getName(), "A message for everyone!")
            );
            startPromise.complete();
        }
    }

    /**
     * First subscriber verticle which listens on the EventBus for messages sent by the Publisher.
     */
    public static class Subscriber1 extends AbstractVerticle {

        private static final Logger LOG = LoggerFactory.getLogger(Subscriber1.class);

        @Override
        public void start(final Promise<Void> startPromise) {
            vertx.eventBus().<String>consumer(Publish.class.getName(), message -> {
                LOG.debug("Subscriber1 Received: {}", message.body());
            });
            startPromise.complete();
        }
    }

    /**
     * Second subscriber verticle which also listens on the EventBus for messages sent by the Publisher.
     * This verticle can be deployed with multiple instances to demonstrate the distributive nature of the
     * publish/subscribe pattern.
     */
    public static class Subscriber2 extends AbstractVerticle {

        private static final Logger LOG = LoggerFactory.getLogger(Subscriber2.class);

        @Override
        public void start(final Promise<Void> startPromise) {
            vertx.eventBus().<String>consumer(Publish.class.getName(), message -> {
                LOG.debug("Subscriber2 Received: {}", message.body());
            });
            startPromise.complete();
        }
    }
}
