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
 * CollectorService is a Vert.x verticle that collects data from multiple sensor services
 * and sends a consolidated response to a snapshot service. It demonstrates handling multiple
 * asynchronous HTTP requests using callbacks.
 * <p>
 * The class sets up an HTTP server listening on port 8080. The handleRequest method issues
 * parallel HTTP GET requests to three sensor services, expected to be running on ports 3000, 3001,
 * and 3002 on localhost. It uses a Vert.x WebClient for making these requests.
 * <p>
 * Features:
 * <ul>
 * <li> Collects data from multiple sensors asynchronously.
 * <li> Uses WebClient for simplified HTTP request handling.
 * <li>  Handles JSON responses and logs errors using a structured approach.
 * <li>  Sends consolidated sensor data to a snapshot service using an HTTP POST request.
 * <li>  Utilizes AtomicInteger to manage asynchronous responses count.
 * <li>  Responds back to the original HTTP request with the aggregated data in JSON format.
 * <ul>
 */
public class CollectorService extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(CollectorService.class);
    private WebClient webClient;

    /**
     * Initializes the CollectorService verticle. This method is called when the verticle is deployed.
     * It sets up a WebClient for making HTTP requests and starts an HTTP server listening on port 8080.
     * The HTTP server uses the handleRequest method to process incoming requests.
     */
    @Override
    public void start() {
        webClient = WebClient.create(vertx);
        vertx.createHttpServer()
                .requestHandler(this::handleRequest)
                .listen(8080);
    }

    /**
     * Handles incoming HTTP requests to the CollectorService. It makes parallel GET requests to three
     * sensor services using WebClient and collects their responses.
     *
     * @param request The incoming HTTP server request.
     *                Each GET request is made to 'localhost' with ports 3000, 3001, and 3002 respectively.
     *                The method aggregates successful responses and once all requests are complete (or failed),
     *                it forwards this aggregated data to the snapshot service using sendToSnapshot method.
     */
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
                            sendToSnapshot(request, data);
                        }
                    });
        }
    }

    /**
     * Sends the aggregated sensor data to the snapshot service. This method is invoked after collecting
     * responses from all sensor services.
     *
     * @param request The original HTTP server request.
     * @param data    The JsonObject containing aggregated data from sensor services.
     *                It makes an HTTP POST request to the snapshot service located at 'localhost' on port 4000.
     *                Upon success, it proceeds to send a response back to the original requester using sendResponse method.
     *                In case of a failure in reaching the snapshot service, it logs the error and sends a 500 status code response.
     */
    private void sendToSnapshot(HttpServerRequest request, JsonObject data) {
        webClient
                .post(4000, "localhost", "/")
                .expect(ResponsePredicate.SC_SUCCESS)
                .sendJsonObject(data, ar -> {
                    if (ar.succeeded()) {
                        sendResponse(request, data);
                    } else {
                        logger.error("Snapshot down?", ar.cause());
                        request.response().setStatusCode(500).end();
                    }
                });
    }

    private void sendResponse(HttpServerRequest request, JsonObject data) {
        request.response()
                .putHeader("Content-Type", "application/json")
                .end(data.encode());
    }
}
