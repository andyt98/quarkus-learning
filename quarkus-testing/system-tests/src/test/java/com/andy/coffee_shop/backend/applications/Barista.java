package com.andy.coffee_shop.backend.applications;

import com.andy.coffee_shop.backend.entity.Order;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import jakarta.json.Json;
import jakarta.json.JsonReader;

import java.io.StringReader;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

// This class mocks the Barista service using WireMock.
public class Barista {

    // Constructor initializes the WireMock server.
    public Barista() {
        // Fetch host and port from system properties with defaults if not specified.
        String host = System.getProperty("test.barista.host", "localhost");
        int port = Integer.parseInt(System.getProperty("test.barista.port", "8081"));

        // Configure WireMock to listen to the specified host and port.
        configureFor(host, port);

        // Initially, stub the POST request to "/processes" to return a status of "PREPARING".
        stubFor(post("/processes")
                .willReturn(okForJson(Map.of("status", "PREPARING"))));
    }

    // Modifies the response WireMock will provide when the Barista service is invoked with a specific order URI.
    public void answerForOrder(URI orderUri, String status) {
        String orderId = extractOrderId(orderUri);

        // Remove any existing stubs with this order ID.
        removeServeEvents(postRequestedFor(urlEqualTo("/processes"))
                .withRequestBody(requestJson(orderId)));

        // Create a new stub for the given order ID with the desired response status.
        stubFor(post("/processes")
                .withRequestBody(requestJson(orderId))
                .willReturn(okForJson(Map.of("status", status))));
    }

    // Utility method to create a JSON string for request matching.
    private StringValuePattern requestJson(String orderId) {
        return equalToJson("""
                {
                    "order": "%s"
                }
                """.formatted(orderId), true, true);
    }

    // Waits for the Barista service to be invoked with a specific order URI and status.
    public void waitForInvocation(URI orderUri, String status) {
        String orderId = extractOrderId(orderUri);
        await().atMost(10, SECONDS).until(() -> requestMatched(orderId, status));
    }

    // Checks if a request with the specified order ID and status has been received by WireMock.
    private boolean requestMatched(String orderId, String status) {
        List<LoggedRequest> matchedRequests = findAll(postRequestedFor(
                urlEqualTo("/processes"))
                .withRequestBody(requestJson(orderId, status)));
        return !matchedRequests.isEmpty();
    }

    // Utility method to create a JSON string for request matching with a specific status.
    private StringValuePattern requestJson(String orderId, String status) {
        String json = """
                {
                    "order": "%s",
                    "status": "%s"
                }
                """.formatted(orderId, status);
        return equalToJson(json, true, true);
    }

    // Extracts the order ID from the order URI.
    private String extractOrderId(URI orderUri) {
        String string = orderUri.toString();
        return string.substring(string.lastIndexOf('/') + 1);
    }

    // Prints out request and response details for debugging purposes.
    public void debug(URI orderUri) {
        String orderId = extractOrderId(orderUri);
        getAllServeEvents().stream()
                .filter(e -> e.getRequest().getBodyAsString().contains(orderId))
                .forEach(e -> {
                    System.out.println("Request Body: " + e.getRequest().getBodyAsString());
                    System.out.println("Response Status: " + e.getResponse().getStatus());
                    System.out.println("Response Body: " + e.getResponse().getBodyAsString());
                });
    }

    // Verifies if the received requests match the expected requests for a given order.
    public void verifyRequests(URI orderUri, Order order) {
        String orderId = extractOrderId(orderUri);
        findAll(anyRequestedFor(anyUrl())).stream()
                .map(r -> {
                    JsonReader reader = Json.createReader(new StringReader(r.getBodyAsString()));
                    return reader.readObject();
                })
                .filter(j -> j.getString("order").equals(orderId))
                .forEach(r -> {
                    assertThat(r.getString("type")).isEqualTo(order.getType().toUpperCase());
                    assertThat(r.getString("origin")).isEqualTo(order.getOrigin().toUpperCase());
                    assertThat(r.getString("status")).isIn("PREPARING", "FINISHED");
                });
    }
}
