package com.andy.p5_beyond_callbacks.reactivex;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.client.predicate.ResponsePredicate;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CollectorService extends AbstractVerticle and demonstrates the use of RxJava for reactive programming
 * in a Vert.x environment. This version of CollectorService contrasts with earlier versions by utilizing
 * RxJava's Single and Observable types for handling asynchronous operations, which simplifies the code by
 * avoiding the manual handling of callbacks or futures and promises.
 */
public class CollectorService extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(CollectorService.class);
    private WebClient webClient;

    /**
     * Starts the CollectorService verticle reactively. It sets up a WebClient and an HTTP server.
     * The server listens reactively on port 8080 and processes incoming HTTP requests.
     *
     * @return A Completable that completes when the HTTP server is successfully set up.
     */
    @Override
    public Completable rxStart() {
        webClient = WebClient.create(vertx); // Initialize the WebClient
        return vertx.createHttpServer() // Create and configure the HTTP server
                .requestHandler(this::handleRequest) // Set the request handler
                .rxListen(8080) // Listen reactively on port 8080
                .ignoreElement(); // Completable that completes upon successful listening
    }

    /**
     * Handles incoming HTTP requests. It collects temperatures from sensor services and
     * sends the aggregated data to the snapshot service. This method demonstrates the
     * reactive handling of requests and responses using RxJava's Single.
     *
     * @param request The incoming HTTP server request.
     */
    private void handleRequest(HttpServerRequest request) {
        // Chain of operations to collect temperatures, send to snapshot, and handle the response
        Single<JsonObject> data = collectTemperatures();
        sendToSnapshot(data).subscribe(
                json -> request.response()
                        .putHeader("Content-Type", "application/json")
                        .end(json.encode()),
                err -> {
                    logger.error("Something went wrong", err);
                    request.response().setStatusCode(500).end();
                });
    }

    /**
     * Collects temperatures from sensor services using RxJava's Single.zip operator.
     * This method showcases the composition of asynchronous results without nested callbacks.
     *
     * @return A Single that emits a JsonObject containing aggregated sensor data.
     */
    private Single<JsonObject> collectTemperatures() {
        // Fetch temperatures from different ports
        Single<HttpResponse<JsonObject>> r1 = fetchTemperature(3000);
        Single<HttpResponse<JsonObject>> r2 = fetchTemperature(3001);
        Single<HttpResponse<JsonObject>> r3 = fetchTemperature(3002);

        // Zip the responses together into a single JsonObject
        return Single.zip(r1, r2, r3, (j1, j2, j3) -> {
            JsonArray array = new JsonArray()
                    .add(j1.body())
                    .add(j2.body())
                    .add(j3.body());
            return new JsonObject().put("data", array);
        });
    }

    /**
     * Fetches temperature data from a specified port using WebClient.
     *
     * @param port The port of the sensor service.
     * @return A Single that emits the HttpResponse containing the temperature data.
     */
    private Single<HttpResponse<JsonObject>> fetchTemperature(int port) {
        // Asynchronously send a GET request and return the response
        return webClient
                .get(port, "localhost", "/")
                .expect(ResponsePredicate.SC_SUCCESS)
                .as(BodyCodec.jsonObject())
                .rxSend();
    }

    /**
     * Sends the collected temperature data to the snapshot service.
     * Demonstrates flat mapping of a Single to another asynchronous operation.
     *
     * @param data The collected temperature data.
     * @return A Single that emits the JsonObject after sending it to the snapshot service.
     */
    private Single<JsonObject> sendToSnapshot(Single<JsonObject> data) {
        return data.flatMap(json -> webClient
                .post(4000, "localhost", "")
                .expect(ResponsePredicate.SC_SUCCESS)
                .rxSendJsonObject(json)
                .flatMap(resp -> Single.just(json)));
    }
}
