package com.andy.coffee_shop.orders.boundary;

import com.andy.coffee_shop.orders.entity.CoffeeType;
import com.andy.coffee_shop.orders.entity.Order;
import com.andy.coffee_shop.orders.control.OrderProcessor;
import com.andy.coffee_shop.orders.control.OrderRepository;
import com.andy.coffee_shop.orders.control.OriginRepository;
import com.andy.coffee_shop.orders.entity.Origin;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class CoffeeShop {

    @Inject
    OrderRepository orderRepository;

    @Inject
    OriginRepository originRepository;

    @Inject
    OrderProcessor orderProcessor;

    @Inject
    Event<Order> updatedOrders;

    public Set<CoffeeType> getCoffeeTypes() {
        return EnumSet.of(CoffeeType.ESPRESSO, CoffeeType.LATTE, CoffeeType.POUR_OVER);
    }

    public Set<Origin> getOrigins(CoffeeType type) {
        return originRepository.listForCoffeeType(type);
    }

    public Origin getOrigin(String name) {
        return originRepository.findById(name);
    }

    public void createOrder(Order order) {
        orderRepository.persist(order);
        updatedOrders.fire(order);
    }

    public Order getOrder(UUID id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrders() {
        return orderRepository.listAll();
    }

    @Scheduled(every = "2s")
    public void processUnfinishedOrders() {
        orderRepository.listUnfinishedOrders().forEach(orderProcessor::processOrder);
    }

    public void updateOrder(UUID id, Order order) {
        Order managedOrder = orderRepository.findById(id);
        managedOrder.setType(order.getType());
        managedOrder.setOrigin(order.getOrigin());
        updatedOrders.fire(order);
    }

}
