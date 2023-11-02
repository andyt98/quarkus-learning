package com.andy.producers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@RequestScoped
@Path("/random-number")
public class RandomNumberResource {

    @Inject
    @RandomNumber
    private int randomNumber;

    @GET
    @Produces("text/plain")
    public String processRandomNumber() {
        return "Received random number: " + randomNumber;
    }
}