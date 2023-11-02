package com.andy.events.async;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import java.util.logging.Logger;

@Path("/order-async")
public class OrderResource {
    private static final Logger logger = Logger.getLogger(OrderResource.class.getName());

    @Inject
    OrderService orderService;

    @GET
    @Produces("text/plain")
    public String order(@QueryParam("amount") Double amount) {
        logger.info(String.format("Initiating order placement for amount: %s ...", amount));
        Order order = orderService.placeOrder(amount);
        return String.format("Order placement initiated for amount %s...", amount);
    }
}
