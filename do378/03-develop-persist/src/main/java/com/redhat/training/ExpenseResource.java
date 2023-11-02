package com.redhat.training;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/expenses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseResource {

    @Inject
    ExpenseRepository expenseRepository;

    @GET
    public List<Expense> list(@DefaultValue("5") @QueryParam("pageSize") int pageSize,
                              @DefaultValue("1") @QueryParam("pageNum") int pageNum) {
        PanacheQuery<Expense> expenseQuery = expenseRepository.findAll(
                Sort.by("amount").and("associateId"));

        return expenseQuery.page(Page.of(pageNum - 1, pageSize)).list();
    }

    @POST
    @Transactional
    public void create(final Expense expense) {
        expenseRepository.persist(expense);
    }

    @DELETE
    @Path("{uuid}")
    @Transactional
    public void delete(@PathParam("uuid") final UUID uuid) {
        long numExpensesDeleted = expenseRepository.delete("uuid", uuid);

        if (numExpensesDeleted == 0) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @PUT
    @Transactional
    public void update(final Expense expense) {
        try {
            updateExpense(expense);
        } catch (RuntimeException e) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    private void updateExpense(final Expense expense) throws RuntimeException {
        Optional<Expense> previousExpense = expenseRepository.findByIdOptional(expense.id);

        previousExpense.ifPresentOrElse((updatedExpense) -> {
            updatedExpense.uuid = expense.uuid;
            updatedExpense.name = expense.name;
            updatedExpense.amount = expense.amount;
            updatedExpense.paymentMethod = expense.paymentMethod;
            expenseRepository.persist(updatedExpense);
        }, () -> {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        });
    }
}
