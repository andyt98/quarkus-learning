package com.andy.countries;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.concurrent.CompletionStage;


@Path("v3.1")
@RegisterRestClient(baseUri = "https://restcountries.com/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface MicroprofileClient {

    @GET
    @Path("all")
    List<Country> countriesList();

    @GET
    @Path("all")
    CompletionStage<List<Country>> countriesListAsync();

}
