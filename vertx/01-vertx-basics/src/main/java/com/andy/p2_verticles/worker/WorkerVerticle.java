package com.andy.p2_verticles.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Worker verticles in Vert.x execute on worker threads instead of the event loop. They are used for handling tasks
 * that may take a long time to complete. Worker verticles are single-threaded, but the thread may not always be the same.
 * Successive events may not execute on the same thread. Worker verticles can only be accessed by a single worker thread
 * at a time. Enabling multithreading for worker verticles is an advanced usage and not recommended. It is encouraged to
 * adjust worker pool sizes to match the workload instead of enabling worker multi-threading.</p>
 *
 * <p>To create a worker verticle, extend the AbstractVerticle class and override the start() method. Deploy the worker
 * verticle using appropriate deployment options, such as setting the number of instances and enabling worker mode.</p>
 */

public class WorkerVerticle extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(WorkerVerticle.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        DeploymentOptions opts = new DeploymentOptions()
                .setInstances(2)
                .setWorker(true);
        vertx.deployVerticle("com.andy.vertx_learning.p2_verticles.worker.WorkerVerticle", opts);
    }

    @Override
    public void start() {
        vertx.setPeriodic(10_000, id -> {
            try {
                logger.info("Zzz...");
                Thread.sleep(8000);
                logger.info("Up!");
            } catch (InterruptedException e) {
                logger.error("Whoops", e);
            }
        });
    }
}
