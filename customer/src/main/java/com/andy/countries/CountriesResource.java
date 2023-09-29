package com.andy.countries;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("countries")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CountriesResource {

    @Inject
    JaxRsClient jaxRsClient;

    @Inject
    @RestClient
    MicroprofileClient microprofileClient;

    @GET
    public List<String> getCountriesJaxRs() {
        return jaxRsClient.fetchCountryNames();
    }

    @GET
    @Path("/alternative")
    public List<String> getCountriesMicroprofile() {
        List<Country> countries = microprofileClient.countriesList();

        return countries.stream()
                .map(c -> c.getName().getCommon())
                .collect(Collectors.toList());
    }

}
