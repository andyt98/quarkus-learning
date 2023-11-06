package com.andy.p3_eventbus.heatsensor;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Maintains the latest temperature readings from various sensors and calculates their average.
 * This verticle is an example of a data aggregator that listens for sensor updates on the event bus and processes them to provide additional metrics.
 */
public class SensorData extends AbstractVerticle {

    private final HashMap<String, Double> lastValues = new HashMap<>();

    @Override
    public void start() {
        EventBus bus = vertx.eventBus();
        bus.consumer("sensor.updates", this::update);
        bus.consumer("sensor.average", this::average);
    }

    private void update(Message<JsonObject> message) {
        JsonObject json = message.body();
        lastValues.put(json.getString("id"), json.getDouble("temp"));
    }

    private void average(Message<JsonObject> message) {
        double avg = lastValues.values().stream()
                .collect(Collectors.averagingDouble(Double::doubleValue));
        JsonObject json = new JsonObject().put("average", avg);
        message.reply(json);
    }
}
