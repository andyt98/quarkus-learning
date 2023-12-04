package com.redhat.training.expense;

import java.util.List;


import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/expenses")
@Produces(MediaType.APPLICATION_JSON)
public class ExpensesResource {

    @Inject
    ExpensesRepository expenses;

    @GET
    @Path("/{name}")
    public Expense getByName(@PathParam("name") String name) {
        Log.debug("Getting expense " + name);

        try {
            return expenses.getByName(name);
        } catch (ExpenseNotFoundException e) {
            var message = e.getMessage();
            Log.error(message);
            throw new NotFoundException(message);
        }
    }

    @GET
    public List<Expense> getAll() {
        return expenses.list();
    }

}
