package com.andy.events.sync;

import java.time.LocalDate;

public class Order {

    private final LocalDate placedAt;
    private final double amount;


    public Order(double amount) {
        this.amount = amount;
        this.placedAt = LocalDate.now();
    }

    public LocalDate getPlacedAt() {
        return placedAt;
    }

    public double getAmount() {
        return amount;
    }
}