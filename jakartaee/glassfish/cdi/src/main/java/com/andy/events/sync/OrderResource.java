package com.andy.events.sync;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;

@Path("/order")
public class OrderResource {

    @Inject
    OrderService orderService;

    @GET
    @Produces("text/plain")
    public String order(@QueryParam("amount") Double amount) {
        Order order = orderService.placeOrder(amount);
        return String.format("Order placed at %s of amount %s", order.getPlacedAt(), amount);
    }
}