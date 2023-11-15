package com.andy.p5_beyond_callbacks.reactivex.intro;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.RxHelper;
import io.vertx.reactivex.core.AbstractVerticle;

import java.util.concurrent.TimeUnit;

/**
 * Demonstrates the integration of RxJava with Vert.x, showcasing how reactive programming
 * principles can be applied within a Vert.x application. This class extends AbstractVerticle,
 * a core component of Vert.x, and uses RxJava to create reactive flows.
 */
public class VertxIntro extends AbstractVerticle {

    /**
     * Starts the Vert.x verticle in a reactive style using RxJava. This method overrides rxStart
     * from AbstractVerticle, which is specifically designed for use with RxJava in Vert.x.
     *
     * @return A Completable that indicates the completion of the verticle start-up process.
     */
    @Override
    public Completable rxStart() {
        // Example: Reactive timer using RxJava and Vert.x
        Observable
                .interval(1, TimeUnit.SECONDS, RxHelper.scheduler(vertx)) // Creates an Observable that emits a long value at fixed time intervals
                .subscribe(n -> System.out.println("tick")); // Prints "tick" every second

        // Setting up a Vert.x HTTP server reactively
        return vertx.createHttpServer() // Creates a new HTTP server
                .requestHandler(r -> r.response().end("Ok")) // Sets request handler to respond with "Ok"
                .rxListen(8080) // Makes the server listen on port 8080 reactively
                .ignoreElement(); // Converts the Single to a Completable, ignoring the result
    }

    /**
     * Main method to deploy the VertxIntro verticle using Vert.x.
     *
     * @param args Command line arguments (not used in this example).
     */
    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new VertxIntro()); // Deploys the VertxIntro verticle
    }
}
