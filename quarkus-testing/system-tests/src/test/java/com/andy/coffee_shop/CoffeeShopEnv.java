package com.andy.coffee_shop;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.lifecycle.Startables;

import java.io.IOException;
import java.nio.file.Paths;

public class CoffeeShopEnv {

    static Network network = Network.newNetwork();

    static GenericContainer<?> barista = new GenericContainer<>("wiremock/wiremock:2.33.2")
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/__admin/"))
            .withNetwork(network)
            .withNetworkAliases("barista");

    static PostgreSQLContainer<?> coffeeShopDb = new PostgreSQLContainer<>("postgres:9.5")
            .withExposedPorts(5432)
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres")
            .withNetwork(network)
            .withNetworkAliases("coffee-shop-db");

    static GenericContainer<?> coffeeShop = new GenericContainer<>(new ImageFromDockerfile("coffee-shop")
            .withDockerfile(Paths.get(System.getProperty("user.dir"), "coffee-shop/Dockerfile")))
            .withEnv("QUARKUS_LAUNCH_DEVMODE", "true")
            .withEnv("QUARKUS_LIVE_RELOAD_PASSWORD", "123")
            .withEnv("QUARKUS_LIVE_RELOAD_URL", "http://localhost:8080")
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/q/health"))
            .withNetwork(network)
            .withNetworkAliases("coffee-shop")
            .dependsOn(barista, coffeeShopDb);

    public static void main(String[] args) throws IOException {
        Startables.deepStart(coffeeShop, barista, coffeeShopDb).join();

        String coffeeShopHost = coffeeShop.getHost();
        int coffeeShopPort = coffeeShop.getMappedPort(8080);
        String baristaHost = barista.getHost();
        int baristaPort = barista.getMappedPort(8080);

        String coffeeShop = "http://" + coffeeShopHost + ":" + coffeeShopPort + "/index.html";
        System.out.println("The coffee-shop URL is: " + coffeeShop);
        String barista = "http://" + baristaHost + ":" + baristaPort + "/";
        System.out.println("The barista URL is: " + barista);
        System.out.println();

        System.out.println("\nYou can connect to Quarkus remote-dev by executing the following in coffee-shop/:");
        System.out.println("mvn quarkus:remote-dev -Ddebug=false -Dquarkus.live-reload.url=http://" + coffeeShopHost + ":" + coffeeShopPort + " -Dquarkus.live-reload.password=123 -Dquarkus.package.type=mutable-jar");
        System.out.println("\nContainers started. Press any key or kill process to stop");
        System.in.read();
    }

}
