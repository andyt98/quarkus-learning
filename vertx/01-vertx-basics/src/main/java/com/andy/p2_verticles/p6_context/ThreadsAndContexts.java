package com.andy.p2_verticles.p6_context;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class demonstrates the usage of Vert.x Contexts in various scenarios.
 * It includes creating contexts without a verticle, utilizing context data, and handling exceptions within a context.
 */
public class ThreadsAndContexts {

    private static final Logger logger = LoggerFactory.getLogger(ThreadsAndContexts.class);

    public static void main(String[] args) {
        createAndRun();
        dataAndExceptions();
    }

    /**
     * Demonstrates creating and running code in different contexts.
     * Each call to getOrCreateContext may potentially create a new context if the current thread is not
     * already associated with a context. The runOnContext method schedules the execution of the code on the event loop.
     */
    private static void createAndRun() {
        Vertx vertx = Vertx.vertx();

        // Creates a new context or gets the existing one and logs "ABC" on that context's event loop.
        vertx.getOrCreateContext()
                .runOnContext(v -> logger.info("ABC"));

        // Creates another new context or gets the existing one and logs "123" on that context's event loop.
        vertx.getOrCreateContext()
                .runOnContext(v -> logger.info("123"));
    }

    /**
     * Demonstrates the use of context data and exception handling.
     * Here, we add data to the context and set an exception handler for handling any exceptions that occur
     * while executing handlers on this context's event loop.
     */
    private static void dataAndExceptions() {
        Vertx vertx = Vertx.vertx();
        Context ctx = vertx.getOrCreateContext();

        // Putting data into the context which can be retrieved in any handler executing on this context.
        ctx.put("foo", "bar");

        // Setting an exception handler for the context to handle exceptions that are thrown by any handler.
        ctx.exceptionHandler(t -> {
            if ("Tada".equals(t.getMessage())) {
                logger.info("Got a _Tada_ exception");
            } else {
                logger.error("Woops", t);
            }
        });

        // Throwing an exception to trigger the exception handler above.
        ctx.runOnContext(v -> {
            throw new RuntimeException("Tada");
        });

        // Throwing an exception to trigger the exception handler above.
        ctx.runOnContext(v -> logger.info("foo = {}", (String) ctx.get("foo")));
    }
}
