package com.andy.events.sync;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class OrderService {

    @Inject
    private Event<Order> newOrderEvent;

    public Order placeOrder(double amount) {
        Order newOrder = new Order(amount);
        newOrderEvent.fire(newOrder);
        return newOrder;
    }
}