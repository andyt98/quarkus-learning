package com.andy.p5_beyond_callbacks.futures;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Demonstrates the usage of Promises and Futures in Vert.x for asynchronous programming.
 * The class shows different ways to create, manipulate, and combine futures and promises.
 */
public class Intro {

    public static void main(String[] args) {
        // Creating a Vert.x instance.
        Vertx vertx = Vertx.vertx();

        // Creating a Promise which will later be completed or failed.
        Promise<String> promise = Promise.promise();

        System.out.println("Waiting...");

        // Setting a timer to simulate an asynchronous operation.
        vertx.setTimer(5000, id -> {
            // Completing or failing the promise based on a simple condition.
            if (System.currentTimeMillis() % 2L == 0L) {
                promise.complete("Ok!");
            } else {
                promise.fail(new RuntimeException("Bad luck..."));
            }
        });

        // Getting the Future associated with the Promise.
        Future<String> future = promise.future();

        // Handling the future's success or failure.
        future
                .onSuccess(System.out::println)
                .onFailure(err -> System.out.println(err.getMessage()));

        // Chaining operations on the future with recover, map, and flatMap.
        promise.future()
                .recover(err -> Future.succeededFuture("Let's say it's ok!"))
                .map(String::toUpperCase)
                .flatMap(str -> {
                    Promise<String> next = Promise.promise();
                    vertx.setTimer(3000, id -> next.complete(">>> " + str));
                    return next.future();
                })
                .onSuccess(System.out::println);

        // Converting a Vert.x Future to a standard Java CompletionStage.
        CompletionStage<String> cs = promise.future().toCompletionStage();

        // Applying transformations and handling completion.
        cs
                .thenApply(String::toUpperCase)
                .thenApply(str -> "~~~ " + str)
                .whenComplete((str, err) -> {
                    if (err == null) {
                        System.out.println(str);
                    } else {
                        System.out.println("Oh... " + err.getMessage());
                    }
                });

        // Creating a CompletableFuture that completes after a delay.
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "5 seconds have elapsed";
        });

        // Converting a CompletableFuture to a Vert.x Future.
        Future
                .fromCompletionStage(cf, vertx.getOrCreateContext())
                .onSuccess(System.out::println)
                .onFailure(Throwable::printStackTrace);
    }
}
