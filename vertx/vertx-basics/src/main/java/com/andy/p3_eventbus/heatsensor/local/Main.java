package com.andy.p3_eventbus.heatsensor.local;

import com.andy.p3_eventbus.heatsensor.HeatSensor;
import com.andy.p3_eventbus.heatsensor.HttpServer;
import com.andy.p3_eventbus.heatsensor.Listener;
import com.andy.p3_eventbus.heatsensor.SensorData;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

/**
 * Bootstraps the verticles for the application.
 * This class deploys multiple instances of the HeatSensor verticle and single instances of Listener, SensorData, and HttpServer verticles.
 * It sets up the application's verticle infrastructure, demonstrating Vert.x's vertical deployment and instance management.
 */
public class Main {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(HeatSensor.class.getName(), new DeploymentOptions().setInstances(4));
        vertx.deployVerticle(Listener.class.getName());
        vertx.deployVerticle(SensorData.class.getName());
        vertx.deployVerticle(HttpServer.class.getName());
    }
}
