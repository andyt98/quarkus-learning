package com.andy.p5_beyond_callbacks.reactivex.intro;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.concurrent.TimeUnit;

/**
 * Demonstrates basic usage patterns of RxJava, a library for composing asynchronous and event-based programs
 * using observable sequences. This class includes examples of creating and manipulating various Observable types
 * and showcases common RxJava operators.
 */
public class Intro {

    public static void main(String[] args) throws InterruptedException {
        // Example 1: Basic Observable with map and subscribe
        Observable.just(1, 2, 3) // Creates an Observable emitting 1, 2, 3
                .map(Object::toString) // Converts integers to strings
                .map(s -> "@" + s) // Adds "@" prefix to each string
                .subscribe(System.out::println); // Subscribes and prints each modified element

        // Example 2: Observable with error handling
        Observable.<String>error(() -> new RuntimeException("Woops")) // Creates an Observable that emits an error
                .map(String::toUpperCase) // This map operation will be skipped due to the error
                .subscribe(System.out::println, Throwable::printStackTrace); // Subscribes and handles any errors

        // Example 3: Combining Single instances into a Flowable
        Single<String> s1 = Single.just("foo"); // Creates a Single emitting "foo"
        Single<String> s2 = Single.just("bar"); // Creates a Single emitting "bar"
        Flowable<String> m = Single.merge(s1, s2); // Merges both Singles into a Flowable
        m.subscribe(System.out::println); // Subscribes and prints each item from the Singles

        // Example 4: Complex Observable chain with various operators
        Observable
                .just("--", "this", "is", "--", "a", "sequence", "of", "items", "!")
                .doOnSubscribe(d -> System.out.println("Subscribed!")) // Action on subscription
                .delay(5, TimeUnit.SECONDS) // Delays the emission of items by 5 seconds
                .filter(s -> !s.startsWith("--")) // Filters out items starting with "--"
                .doOnNext(x -> System.out.println("doOnNext: " + x)) // Action on each item
                .map(String::toUpperCase) // Converts each item to upper case
                .buffer(2) // Buffers every two items
                .subscribe(
                        pair -> System.out.println("next: " + pair),
                        Throwable::printStackTrace,
                        () -> System.out.println("~Done~")); // Final action on completion

        Thread.sleep(10_000); // Sleeps to allow time for Observable to emit items
    }
}
