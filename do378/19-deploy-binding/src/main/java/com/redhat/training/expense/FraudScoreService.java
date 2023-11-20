package com.redhat.training.expense;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@Path("/score")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
@RegisterRestClient
public interface FraudScoreService {
    @GET
    FraudScore getByAmount(@QueryParam("amount") double amount);
}
