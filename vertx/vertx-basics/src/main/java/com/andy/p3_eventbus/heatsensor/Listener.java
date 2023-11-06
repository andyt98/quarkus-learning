package com.andy.p3_eventbus.heatsensor;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

/**
 * Listens for updates from the heat sensors and logs the temperature readings.
 * This verticle acts as a data listener within a publish-subscribe messaging system, showcasing how to handle messages received on the event bus.
 */
public class Listener extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(Listener.class);
    private final DecimalFormat format = new DecimalFormat("#.##");

    @Override
    public void start() {
        EventBus bus = vertx.eventBus();
        bus.<JsonObject>consumer("sensor.updates", msg -> {
            JsonObject body = msg.body();
            String id = body.getString("id");
            String temperature = format.format(body.getDouble("temp"));
            logger.info("{} reports a temperature ~{}C", id, temperature);
        });
    }
}
