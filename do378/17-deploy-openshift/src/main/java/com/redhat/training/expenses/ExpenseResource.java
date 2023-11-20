package com.redhat.training.expenses;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;


@Path("/expense")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseResource {

    @Inject
    ExpenseValidator validator;

    @GET
    public List<Expense> list() {
        return Expense.listAll();
    }

    @POST
    @Transactional
    public Expense create(final Expense expense) {
        Expense newExpense = Expense.of(expense.name, expense.paymentMethod, expense.amount.toString());

        if (!validator.isValid(newExpense)) {
            var response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Expense is invalid. Verify expense amount")
                    .build();
            throw new BadRequestException(response);
        }

        newExpense.persist();

        return newExpense;
    }

    @DELETE
    @Path("{uuid}")
    @Transactional
    public List<Expense> delete(@PathParam("uuid") final UUID uuid) {
        long numExpensesDeleted = Expense.delete("uuid", uuid);

        if (numExpensesDeleted == 0) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return Expense.listAll();
    }

    @PUT
    @Transactional
    public void update(final Expense expense) {
        if (expense.uuid != null)
            Expense.update(expense);
        else
            throw new NotFoundException("Expense id not provided.");
    }
}
