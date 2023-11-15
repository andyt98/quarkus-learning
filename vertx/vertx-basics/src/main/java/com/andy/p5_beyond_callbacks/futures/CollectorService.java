package com.andy.p5_beyond_callbacks.futures;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * CollectorService is a Vert.x verticle that demonstrates the usage of Futures and Promises
 * to handle asynchronous operations in a reactive manner. The class collects temperature data
 * from multiple sensor services and sends a consolidated response to a snapshot service.
 * <p>
 * Features:
 * <ul>
 * <li> Uses Futures and Promises for asynchronous programming, avoiding callback hell.
 * <li> Fetches temperature data from sensor services asynchronously.
 * <li> Aggregates data and sends it to the snapshot service in a non-blocking way.
 * <li> Handles HTTP requests and responses efficiently with error handling.
 * </ul>
 * <p>
 * The class sets up an HTTP server listening on port 8080. Upon receiving a request,
 * it fetches temperatures from three sensor services and then sends the aggregated data to the
 * snapshot service. This is implemented using Vert.x's WebClient and reactive programming patterns.
 */
public class CollectorService extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(CollectorService.class);
    private WebClient webClient;

    /**
     * Starts the CollectorService verticle. Initializes the WebClient and sets up an HTTP server.
     * The server listens on port 8080 and uses handleRequest to process incoming requests.
     * This method uses a Promise to signify asynchronous completion or failure of verticle start-up.
     *
     * @param promise A Promise representing the completion state of the verticle start-up.
     */
    @Override
    public void start(Promise<Void> promise) {
        webClient = WebClient.create(vertx);
        vertx.createHttpServer()
                .requestHandler(this::handleRequest)
                .listen(8080)
                .onFailure(promise::fail)
                .onSuccess(ok -> {
                    System.out.println("http://localhost:8080/");
                    promise.complete();
                });
    }

    /**
     * Handles HTTP requests by fetching temperature data from sensor services and sending it to the snapshot service.
     * Uses CompositeFuture to aggregate futures from multiple asynchronous fetchTemperature calls.
     * On successful aggregation, sends the data to the snapshot service and responds back to the client.
     * In case of failure, logs the error and sends a 500 status code response.
     *
     * @param request The incoming HTTP server request.
     */
    private void handleRequest(HttpServerRequest request) {
        CompositeFuture.all(
                        fetchTemperature(3000),
                        fetchTemperature(3001),
                        fetchTemperature(3002))
                .flatMap(this::sendToSnapshot)
                .onSuccess(data -> request.response()
                        .putHeader("Content-Type", "application/json")
                        .end(data.encode()))
                .onFailure(err -> {
                    logger.error("Something went wrong", err);
                    request.response().setStatusCode(500).end();
                });
    }

    /**
     * Sends the aggregated temperature data to the snapshot service.
     * Constructs a JSON object with the data and sends it using a POST request.
     * Returns a Future representing the outcome of the send operation.
     *
     * @param temps A CompositeFuture containing the aggregated temperature data.
     * @return A Future representing the result of the send operation.
     */
    private Future<JsonObject> sendToSnapshot(CompositeFuture temps) {
        List<JsonObject> tempData = temps.list();
        JsonObject data = new JsonObject()
                .put("data", new JsonArray()
                        .add(tempData.get(0))
                        .add(tempData.get(1))
                        .add(tempData.get(2)));
        return webClient
                .post(4000, "localhost", "/")
                .expect(ResponsePredicate.SC_SUCCESS)
                .sendJson(data)
                .map(response -> data);
    }


    /**
     * Fetches temperature data from a sensor service.
     * Makes a GET request to the specified port and returns a Future with the temperature data.
     *
     * @param port The port number of the sensor service.
     * @return A Future containing the fetched temperature data.
     */
    private Future<JsonObject> fetchTemperature(int port) {
        return webClient
                .get(port, "localhost", "/")
                .expect(ResponsePredicate.SC_SUCCESS)
                .as(BodyCodec.jsonObject())
                .send()
                .map(HttpResponse::body);
    }
}
