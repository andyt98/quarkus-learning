package com.andy.hello;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Path("hello")
@Produces(MediaType.TEXT_PLAIN)
public class HelloResource {

    @Inject
    ExampleConfig exampleConfig;

    @ConfigProperty(name = "example.greeting")
    String greeting;

    @GET
    public String hello(){
        System.out.println("greeting = " + greeting);
        System.out.println("exampleConfig.greeting() = " + exampleConfig.greeting());
        System.out.println("exampleConfig.integer() = " + exampleConfig.integer());
        System.out.println("exampleConfig.boolProperty() = " + exampleConfig.boolProperty());
        System.out.println("exampleConfig.origin().name() = " + exampleConfig.origin().name());
        System.out.println("coffeeConfig.origin.continent = " + exampleConfig.origin().continent());
        System.out.println("exampleConfig.countries() = " + exampleConfig.countries());
        return exampleConfig.greeting();
    }

}
