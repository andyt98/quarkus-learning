package com.andy.p2_verticles.deploy;

import io.vertx.core.Vertx;

/**
 * <p>This theory explains the deployment of verticles in Vert.x.</p>
 *
 * <p>Verticles are deployed via the Vertx object, typically following this pattern:</p>
 *
 * <ol>
 *   <li>Deploy a main verticle.</li>
 *   <li>The main verticle deploys other verticles.</li>
 *   <li>Deployed verticles may in turn deploy further verticles.</li>
 * </ol>
 *
 * <p>Note that Vert.x does not have a formal notion of parent/child verticles.</p>
 *
 * <p>The theory provides examples of two verticles:</p>
 *
 * <ul>
 *   <li>EmptyVerticle: A simple verticle that logs when it starts and stops.</li>
 *   <li>Deployer: A verticle that deploys multiple instances of EmptyVerticle and schedules their undeployment.</li>
 * </ul>
 *
 * <p>The Deployer verticle is deployed from a main method in the main class.</p>
 *
 * <p>The logs show the start and stop events of the EmptyVerticle instances, as well as the successful deployment
 * and undeployment of the verticles.</p>
 *
 * <p>The number of event-loop threads in Vert.x is based on the number of CPU cores, and verticles are assigned
 * to event loops in a round-robin fashion.</p>
 *
 * @see EmptyVerticle
 * @see Deployer
 */

public class Main {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Deployer());
    }
}
