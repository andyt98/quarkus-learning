package com.redhat.training.expense;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/admin")
@RolesAllowed({"ADMIN"})
public class AdminResource {

    @Inject
    ExpensesService expenses;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/expenses")
    public List<Expense> listAllExpenses() {
        return expenses.list();
    }

}
