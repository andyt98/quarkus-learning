package com.andy.qualifiers;

import jakarta.enterprise.context.Dependent;

@PayPal
@Dependent
public class PayPalProcessor implements PaymentProcessor {

    @Override
    public String process(double amount) {
        return "Processing PayPal payment: $" + amount;
    }
}