package com.andy.p5_beyond_callbacks.callbacks;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CollectorServiceCBH demonstrates the "callback hell" pattern in asynchronous programming.
 * This class is a variant of CollectorService where all asynchronous operations are nested within callbacks.
 * <p>
 * The verticle creates an HTTP server that listens on port 8080. The core logic for handling requests
 * is encapsulated in the handleRequest method, which, unlike its predecessor, nests all operations in a single method.
 * <p>
 * Key Characteristics:
 * <li>  Nested Callbacks: The handleRequest method illustrates nested callbacks where each asynchronous operation
 * is directly handled inside the callback of the previous operation. This leads to deeper nesting and
 * intertwined logic that is harder to read and maintain.
 * <li>  Error Handling Complexity: The nested structure makes it challenging to effectively handle errors and
 * manage the flow of asynchronous operations, especially when dealing with multiple sources of errors.
 * <li>  Asynchronous Coordination: The method demonstrates how asynchronous operations are coordinated in a
 * nested manner. This includes making parallel HTTP requests, aggregating their results, and then processing
 * the aggregated result in subsequent nested operations.
 * <p>
 * Usage:
 * The class serves as an example of "callback hell," showcasing how not to structure asynchronous code,
 * especially when dealing with sequential composition of operations. It is a practical illustration of why
 * alternative asynchronous programming models may be preferred in complex scenarios.
 */
public class CollectorServiceCBH extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(CollectorServiceCBH.class);
    private WebClient webClient;

    @Override
    public void start() {
        webClient = WebClient.create(vertx);
        vertx.createHttpServer()
                .requestHandler(this::handleRequest)
                .listen(8080);
    }

    private void handleRequest(HttpServerRequest request) {
        List<JsonObject> responses = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < 3; i++) {
            webClient
                    .get(3000 + i, "localhost", "/")
                    .expect(ResponsePredicate.SC_SUCCESS)
                    .as(BodyCodec.jsonObject())
                    .send(ar -> {
                        if (ar.succeeded()) {
                            responses.add(ar.result().body());
                        } else {
                            logger.error("Sensor down?", ar.cause());
                        }
                        if (counter.incrementAndGet() == 3) {
                            JsonObject data = new JsonObject()
                                    .put("data", new JsonArray(responses));
                            webClient
                                    .post(4000, "localhost", "/")
                                    .expect(ResponsePredicate.SC_SUCCESS)
                                    .sendJsonObject(data, ar1 -> {
                                        if (ar1.succeeded()) {
                                            request.response()
                                                    .putHeader("Content-Type", "application/json")
                                                    .end(data.encode());
                                        } else {
                                            logger.error("Snapshot down?", ar1.cause());
                                            request.response().setStatusCode(500).end();
                                        }
                                    });

                        }
                    });
        }
    }

}
