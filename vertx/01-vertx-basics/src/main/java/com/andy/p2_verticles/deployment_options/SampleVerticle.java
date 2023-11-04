package com.andy.p2_verticles.deployment_options;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Configuration data can be passed to a verticle using a JsonObject. The {@code config()} method of the verticle
 * returns the JsonObject configuration instance, and the accessor method supports optional default values. If a key is
 * not present in the JSON object, a default value can be specified.</p>
 *
 * <p>In the example code, a SampleVerticle is deployed multiple times with different configuration data. The
 * {@code main()} method creates a JsonObject with an integer value for the "n" key, and sets it as the configuration
 * for the verticle. The DeploymentOptions allow more control over the verticle deployment, including passing the
 * configuration data and setting the number of instances to deploy.</p>
 *
 * <p>When the verticles are deployed, the {@code start()} method of each verticle logs the value of the "n" key from
 * the configuration. The output shows the different values of the configuration data for each deployed instance of
 * SampleVerticle.</p>
 *
 * @see SampleVerticle
 * @see JsonObject
 * @see DeploymentOptions
 */
public class SampleVerticle extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(SampleVerticle.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        for (int n = 0; n < 4; n++) {
            JsonObject conf = new JsonObject().put("n", n);
            DeploymentOptions opts = new DeploymentOptions()
                    .setConfig(conf)
                    .setInstances(n);
            vertx.deployVerticle("com.andy.vertx_learning.p2_verticles.configuration_data.SampleVerticle", opts);
        }
    }

    @Override
    public void start() {
        logger.info("n = {}", config().getInteger("n", -1));
    }
}
