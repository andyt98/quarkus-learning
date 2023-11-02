package com.andy.qualifiers;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;

@Default
@Dependent
public class CreditCardProcessor implements PaymentProcessor {

    @Override
    public String process(double amount) {
        return "Processing credit card payment: $" + amount;
    }
}