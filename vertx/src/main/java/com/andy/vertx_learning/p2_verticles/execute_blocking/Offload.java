package com.andy.vertx_learning.p2_verticles.execute_blocking;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Worker verticles are a sensible option for running blocking tasks, but it may not always make sense to extract blocking code into worker verticles. Doing so can lead to an explosion in the number of worker verticle classes performing small duties, and each class may not form a sensible standalone functional unit.</p>
 *
 * <p>The other option for running blocking code is to use the <code>executeBlocking</code> method from the <code>Vertx</code> class. This method takes some blocking code to execute, offloads it to a worker thread, and sends the result back to the event loop as a new event.</p>
 *
 * <p>By default, successive <code>executeBlocking</code> operations have their results processed in the same order as the calls to <code>executeBlocking</code>. There is a variant of <code>executeBlocking</code> with an additional <code>boolean</code> parameter, and when it's set to <code>false</code>, results are made available as event-loop events as soon as they are available, regardless of the order of the <code>executeBlocking</code> calls.</p>
 */

public class Offload extends AbstractVerticle {

  private final Logger logger = LoggerFactory.getLogger(Offload.class);

  @Override
  public void start() {
    vertx.setPeriodic(5000, id -> {
      logger.info("Tick");
      vertx.executeBlocking(this::blockingCode, this::resultHandler);
    });
  }

  private void blockingCode(Promise<String> promise) {
    logger.info("Blocking code running");
    try {
      Thread.sleep(4000);
      logger.info("Done!");
      promise.complete("Ok!");
    } catch (InterruptedException e) {
      promise.fail(e);
    }
  }

  private void resultHandler(AsyncResult<String> ar) {
    if (ar.succeeded()) {
      logger.info("Blocking code result: {}", ar.result());
    } else {
      logger.error("Whoops", ar.cause());
    }
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Offload());
  }
}