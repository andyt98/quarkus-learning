package com.andy.alternatives;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@RequestScoped
@Path("/greeting")
public class GreetingResource {

    @Inject
    private Greeting greeting;

    @GET
    @Produces("text/plain")
    public String getGreeting() {
        return greeting.greet();
    }
}
