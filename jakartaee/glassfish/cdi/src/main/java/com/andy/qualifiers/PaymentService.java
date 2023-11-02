package com.andy.qualifiers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PaymentService {

    @Inject
    private PaymentProcessor creditCardProcessor;

    @Inject
    @PayPal
    private PaymentProcessor payPalProcessor;


    public String processPayment(double amount, PaymentMethod paymentMethod) {
        PaymentProcessor processor = switch (paymentMethod) {
            case CREDIT_CARD -> creditCardProcessor;
            case PAYPAL -> payPalProcessor;
        };

        return processor.process(amount);
    }
}