package com.andy.coffee_shop.backend.applications;

import com.andy.coffee_shop.backend.entity.Order;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.ws.rs.core.HttpHeaders.LOCATION;
import static org.assertj.core.api.Assertions.assertThat;

public class CoffeeShop {

    private final Client client;
    private final WebTarget baseTarget;

    public CoffeeShop() {
        client = ClientBuilder.newClient();
        baseTarget = client.target(buildBaseUri());
    }

    private URI buildBaseUri() {
        String host = System.getProperty("test.coffee-shop.host", "localhost");
        String port = System.getProperty("test.coffee-shop.port", "8080");
        return UriBuilder.fromUri("http://{host}:{port}/").build(host, port);
    }

    public void verifySystemUp() {
        Response response = baseTarget.path("/q/health")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus()).isEqualTo(200);

        JsonObject json = response.readEntity(JsonObject.class);

        assertThat(json.getString("status")).isEqualTo("UP");
        assertThat(json.getJsonArray("checks").getValuesAs(JsonObject.class))
                .allMatch(o -> o.getString("status").equals("UP"));
    }

    public void close() {
        client.close();
    }

    public Set<String> getTypes() {
        JsonArray array = baseTarget.path("types")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(JsonArray.class);

        return array.getValuesAs(JsonObject.class).stream()
                .map(o -> o.getString("type"))
                .collect(Collectors.toSet());
    }

    public URI createOrder(Order order) {
        JsonObject json = Json.createObjectBuilder()
                .add("type", order.type)
                .add("origin", order.origin)
                .build();

        Response response = baseTarget.path("orders")
                .request()
                .post(Entity.json(json));

        assertThat(response.getStatusInfo().getFamily()).isEqualTo(Response.Status.Family.SUCCESSFUL);

        return URI.create(response.getHeaderString(LOCATION));
    }

    public Order retrieveOrder(URI orderId) {
        Order order = client.target(orderId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(Order.class);
        return order;
    }
}
