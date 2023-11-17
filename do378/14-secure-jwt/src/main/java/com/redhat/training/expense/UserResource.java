package com.redhat.training.expense;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import java.util.ArrayList;
import java.util.List;


@Path("/user")
@RolesAllowed({"USER"})
public class UserResource {

    @Inject
    ExpensesService expenses;

    @GET
    @Path("/expenses")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Expense> listUserExpenses(SecurityContext context) {
        var authenticatedUser = context.getUserPrincipal();

        if (authenticatedUser == null) {
            return new ArrayList<>();
        }

        return expenses.listByOwner(authenticatedUser.getName());
    }

}
