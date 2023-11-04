package com.andy.p2_verticles.p3_deploy;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <ol>
 *   <li>We deploy a new instance of EmptyVerticle every second.</li>
 *   <li>Deploying a verticle is an asynchronous operation, and there is a variant of the deploy method that supports an asynchronous result.</li>
 *   <li>We will undeploy a verticle after five seconds.</li>
 *   <li>Undeploying is very similar to deploying.</li>
 * </ol>
 */


public class Deployer extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(Deployer.class);

    @Override
    public void start() {
        long delay = 1000;
        for (int i = 0; i < 50; i++) {
            vertx.setTimer(delay, id -> deploy());  // <1>
            delay = delay + 1000;
        }
    }

    private void deploy() {
        vertx.deployVerticle(new EmptyVerticle(), ar -> {   // <2>
            if (ar.succeeded()) {
                String id = ar.result();
                logger.info("Successfully deployed {}", id);
                vertx.setTimer(5000, tid -> undeployLater(id));   // <3>
            } else {
                logger.error("Error while deploying", ar.cause());
            }
        });
    }

    private void undeployLater(String id) {
        vertx.undeploy(id, ar -> {  // <4>
            if (ar.succeeded()) {
                logger.info("{} was undeployed", id);
            } else {
                logger.error("{} could not be undeployed", id);
            }
        });
    }
}
