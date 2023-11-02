package com.andy.events.async;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class OrderService {

    @Inject
    private Event<Order> newOrderEvent;

    public Order placeOrder(double amount) {
        Order newOrder = new Order(amount);
        newOrderEvent.fireAsync(newOrder); // Use fireAsync instead of fire
        return newOrder;
    }
}
