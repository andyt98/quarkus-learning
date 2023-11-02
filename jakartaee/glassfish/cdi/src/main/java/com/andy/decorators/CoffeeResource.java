package com.andy.decorators;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/coffee")
public class CoffeeResource {
    @Inject
    @Named("basic")
    private Coffee coffee;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getCoffee() {
        return "Coffee: " + coffee.getDescription() + "\nCost: $" + coffee.getCost();
    }
}
