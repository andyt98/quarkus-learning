package com.andy.barista;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Path("health")
public class HealthCheckResource {

    @GET
    public String healthCheck() {
        return "OK";
    }

}
