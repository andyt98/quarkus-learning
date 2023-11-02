package com.redhat.training;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Set;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/expenses")
@RegisterRestClient
public interface ExpenseServiceClient {

    @GET
    Set<Expense> getAll();

    @POST
    Expense create(Expense expense);
}
