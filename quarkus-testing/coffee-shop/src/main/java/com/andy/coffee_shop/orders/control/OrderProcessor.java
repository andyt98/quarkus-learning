package com.andy.coffee_shop.orders.control;

import com.andy.coffee_shop.orders.entity.Order;
import com.andy.coffee_shop.orders.entity.OrderStatus;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderProcessor {

    @Inject
    OrderRepository orderRepository;

    @Inject
    Barista barista;

    @Inject
    Event<Order> updatedOrders;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void processOrder(Order order) {
        Order managedOrder = orderRepository.findById(order.getId());
        OrderStatus status = barista.retrieveOrderStatus(managedOrder);
        managedOrder.setStatus(status);
        updatedOrders.fire(order);
    }

}
