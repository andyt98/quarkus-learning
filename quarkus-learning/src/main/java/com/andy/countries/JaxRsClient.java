package com.andy.countries;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@ApplicationScoped
public class JaxRsClient {

    public static final String URL = "https://api.first.org/data/v1/countries";
    private Client client;
    private WebTarget allCountriesTarget;

    @PostConstruct
    void initClient() {
        client = ClientBuilder.newBuilder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        allCountriesTarget = client.target(URL);
    }


    public List<String> fetchCountryNames() {
        try {
            Response response = allCountriesTarget
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();

            JsonArray array = response.readEntity(JsonArray.class);

            return array.getValuesAs(JsonObject.class).stream()
                    .map(json -> json.getJsonObject("name").getString("common", null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Could not fetch the list of countries, reason: " + e.getMessage());
            return List.of();
        }
    }

    @PreDestroy
    void closeClient() {
        client.close();
    }

}
