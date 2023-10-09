package com.andy.coffee_shop.backend.applications;

import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;


public class Barista {

    public Barista() {
        String host = System.getProperty("test.barista.host", "localhost");
        int port = Integer.parseInt(System.getProperty("test.barista.port", "8081"));

        configureFor(host, port);

        stubFor(post("/processes").willReturn(okForJson(Map.of("status", "PREPARING"))));
    }

    public void answerForOrder(URI orderUri, String status) {
        String orderId = extractOrderId(orderUri);
        stubFor(post("/processes")
                .withRequestBody(requestJson(orderId))
                .willReturn(okForJson(Map.of("status", status))));
    }

    private StringValuePattern requestJson(String orderId) {
        return equalToJson("""
                {
                    "order": "%s"
                }
                """.formatted(orderId), true, true);
    }

    public void waitForInvocation(URI orderUri, String status) {
        String orderId = extractOrderId(orderUri);
        await().atMost(10, SECONDS).until(() -> requestMatched(orderId, status));
    }

    private boolean requestMatched(String orderId, String status) {
        List<LoggedRequest> matchedRequests = findAll(postRequestedFor(urlEqualTo("/processes"))
                .withRequestBody(requestJson(orderId, status)));
        return !matchedRequests.isEmpty();
    }

    private StringValuePattern requestJson(String orderId, String status) {
        String json = """
                {
                    "order": "%s",
                    "status": "%s"
                }
                """.formatted(orderId, status);
        return equalToJson(json, true, true);
    }

    private String extractOrderId(URI orderUri) {
        String string = orderUri.toString();
        return string.substring(string.lastIndexOf('/') + 1);
    }

}
