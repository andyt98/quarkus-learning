package com.andy.coffee_shop.orders.boundary;

import com.andy.coffee_shop.orders.control.OrderProcessorTestDouble;
import com.andy.coffee_shop.orders.control.OrderRepository;
import com.andy.coffee_shop.orders.control.OriginRepository;
import com.andy.coffee_shop.orders.entity.Order;

import javax.enterprise.event.Event;
import java.util.List;

import static org.mockito.Mockito.*;

public class CoffeeShopTestDouble extends CoffeeShop {

    public CoffeeShopTestDouble(OrderProcessorTestDouble orderProcessor) {
        this.orderProcessor = orderProcessor;
        orderRepository = mock(OrderRepository.class);
        originRepository = mock(OriginRepository.class);
        updatedOrders = mock(Event.class);
    }

    public void verifyCreateOrder(Order order) {
        verify(orderRepository).persist(order);
        verify(updatedOrders).fire(order);
    }

    public void answerForUnfinishedOrders(List<Order> orders) {
        when(orderRepository.listUnfinishedOrders()).thenReturn(orders);
        orders.forEach(order -> when(orderRepository.findById(order.getId())).thenReturn(order));
    }

    public void verifyProcessUnfinishedOrders(List<Order> orders) {
        verify(orderRepository).listUnfinishedOrders();
        ((OrderProcessorTestDouble) orderProcessor).verifyProcessOrders(orders);
    }
}
