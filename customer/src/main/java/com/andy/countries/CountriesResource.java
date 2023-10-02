package com.andy.countries;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
@Path("countries")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CountriesResource {

    @Inject
    CountriesService countriesService;


    @GET
    public List<String> getCountriesJaxRs() {
        return countriesService.getCountriesJaxRs();
    }

    @GET
    @Path("/alternative")
    public List<String> getCountriesMicroprofile() {
        return countriesService.getCountriesMicroprofile();
    }

    @GET
    @Path("/reactive")
    public CompletionStage<List<String>> getCountriesReactive() throws ExecutionException, InterruptedException {
        return countriesService.getCountriesReactive();
    }

}
