package com.andy.qualifiers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

@RequestScoped
@Path("/payment")
public class PaymentResource {

    @Inject
    private PaymentService paymentService;

    @GET
    @Produces("text/plain")
    @Path("/card")
    public String getCardPayment(@QueryParam("amount") double amount) {
        return paymentService.processPayment(amount, PaymentMethod.CREDIT_CARD);
    }


    @GET
    @Produces("text/plain")
    @Path("/paypal")
    public String getPayPalPayment(@QueryParam("amount") double amount) {
        return paymentService.processPayment(amount, PaymentMethod.PAYPAL);
    }
}