package com.redhat.training.expense;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class ExpensesRepository {

    private final List<Expense> expenses;

    ExpensesRepository() {
        expenses = new ArrayList<>();
        expenses.add(new Expense("pat-1", 43.5, "patricia@example.com"));
        expenses.add(new Expense("joel-2", 10.0, "joel@example.com"));
        expenses.add(new Expense("joel-3", 24.2, "joel@example.com"));
    }

    List<Expense> list() {
        return expenses;
    }

    Expense getByName(String name) throws ExpenseNotFoundException {
        var found = expenses
                .stream()
                .filter(expense -> expense.name.equalsIgnoreCase(name))
                .toList();

        try {
            return found.get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new ExpenseNotFoundException(name);
        }
    }
}
