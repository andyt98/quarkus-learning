package com.andy.countries;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class CountriesService {

    @Inject
    JaxRsClient jaxRsClient;

    @Inject
    @RestClient
    MicroprofileClient microprofileClient;

    @Inject
    ApiFirstOrgCountriesClient apiFirstOrgCountriesClient;

    @Inject
    Logger logger;

    public List<String> getCountriesJaxRs() {
        logger.log("countries() invoked");
        return jaxRsClient.fetchCountryNames();
    }

    public List<String> getCountriesMicroprofile() {
        List<Country> countries = microprofileClient.countriesList();

        return countries.stream()
                .map(c -> c.getName().getCommon())
                .toList();
    }

    public CompletionStage<List<String>> getCountriesReactive() throws ExecutionException, InterruptedException {

        CompletionStage<List<Country>> countries = microprofileClient.countriesListAsync();
        CompletionStage<List<String>> apiFirstCountries = apiFirstOrgCountriesClient.countriesListAsync();


        return countries.thenCombine(apiFirstCountries, (cs1, cs2) -> {
            List<String> result = new ArrayList<>(cs1.stream()
                    .map(c -> c.getName().getCommon())
                    .toList());

            result.retainAll(cs2);
            return result;
        });
    }
    
    
}
