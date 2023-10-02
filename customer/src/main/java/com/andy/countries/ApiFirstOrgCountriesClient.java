package com.andy.countries;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class ApiFirstOrgCountriesClient {

    private Client client;
    private WebTarget countriesTarget;

    @PostConstruct
    void initClient() {
        client = ClientBuilder.newClient();
        countriesTarget = client.target("https://api.first.org/data/v1/countries");
    }

    public CompletionStage<List<String>> countriesListAsync() {
        return countriesTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .rx()
                .get(JsonObject.class)
                .thenApply(json -> json.getJsonObject("data")
                        .values()
                        .stream().map(jsonValue -> jsonValue.asJsonObject().getString("country"))
                        .toList());
    }

    @PreDestroy
    private void closeClient() {
        client.close();
    }

}
