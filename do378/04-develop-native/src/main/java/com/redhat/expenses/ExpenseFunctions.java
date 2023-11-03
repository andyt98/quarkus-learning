package com.redhat.expenses;

import io.quarkus.funqy.Funq;
import jakarta.inject.Inject;

import java.util.List;



public class ExpenseFunctions {

    @Inject
    ExpenseRepository repository;

    @Funq
    public List<Expense> expenses() {
        return repository.all();
    }

    @Funq
    public void createExpense( Expense expense ) {
        repository.add( expense );
    }
}
