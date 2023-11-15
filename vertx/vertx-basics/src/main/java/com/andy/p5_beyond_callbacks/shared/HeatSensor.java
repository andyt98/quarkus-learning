package com.andy.p5_beyond_callbacks.shared;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;

import java.util.Random;
import java.util.UUID;

/**
 * HeatSensor is a Vert.x verticle simulating a temperature sensor.
 * It periodically updates its temperature based on a random Gaussian fluctuation.
 * This class also creates an HTTP server that responds with the sensor's current temperature
 * and ID in JSON format upon receiving a request.
 * <p>
 * Temperature updates occur at random intervals between 1 and 5 seconds. The initial
 * temperature is set to 21.0 degrees. The HTTP server's port is configurable.
 */

public class HeatSensor extends AbstractVerticle {

    private final Random random = new Random();
    private final String sensorId = UUID.randomUUID().toString();
    private double temperature = 21.0;

    private void scheduleNextUpdate() {
        vertx.setTimer(random.nextInt(5000) + 1000, this::update);
    }

    private void update(long timerId) {
        temperature = temperature + (delta() / 10);
        scheduleNextUpdate();
    }

    private double delta() {
        if (random.nextInt() > 0) {
            return random.nextGaussian();
        } else {
            return -random.nextGaussian();
        }
    }

    @Override
    public void start() {
        vertx.createHttpServer()
                .requestHandler(this::handleRequest)
                .listen(config().getInteger("http.port", 3000));
        scheduleNextUpdate();
    }

    private void handleRequest(HttpServerRequest req) {
        JsonObject data = new JsonObject()
                .put("id", sensorId)
                .put("temp", temperature);
        req.response()
                .putHeader("Content-Type", "application/json")
                .end(data.encode());
    }
}
