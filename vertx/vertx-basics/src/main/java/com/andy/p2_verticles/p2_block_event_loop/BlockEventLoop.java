package com.andy.p2_verticles.p2_block_event_loop;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class BlockEventLoop extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new BlockEventLoop());
    }

    @Override
    public void start() {
        vertx.setTimer(1000, id -> {
            while (true) ;
        });
    }
}
