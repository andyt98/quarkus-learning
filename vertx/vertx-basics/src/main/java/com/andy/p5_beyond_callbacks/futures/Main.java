package com.andy.p5_beyond_callbacks.futures;

import com.andy.p5_beyond_callbacks.shared.HeatSensor;
import com.andy.p5_beyond_callbacks.shared.SnapshotService;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Main {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(HeatSensor.class.getName());
        new DeploymentOptions().setConfig(new JsonObject()
                .put("http.port", 3000));

        vertx.deployVerticle(HeatSensor.class.getName(),
                new DeploymentOptions().setConfig(new JsonObject()
                        .put("http.port", 3001)));

        vertx.deployVerticle(HeatSensor.class.getName(),
                new DeploymentOptions().setConfig(new JsonObject()
                        .put("http.port", 3002)));

        vertx.deployVerticle(SnapshotService.class.getName());
        vertx.deployVerticle(CollectorService.class.getName());
    }
}
