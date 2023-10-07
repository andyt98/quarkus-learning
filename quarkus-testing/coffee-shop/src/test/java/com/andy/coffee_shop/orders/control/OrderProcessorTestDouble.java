package com.andy.coffee_shop.orders.control;

import com.andy.coffee_shop.orders.entity.Order;
import com.andy.coffee_shop.orders.entity.OrderStatus;
import org.mockito.ArgumentCaptor;

import javax.enterprise.event.Event;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OrderProcessorTestDouble extends OrderProcessor {

    private final ArgumentCaptor<Order> orderCaptor;

    public OrderProcessorTestDouble() {
        orderRepository = mock(OrderRepository.class);
        barista = mock(Barista.class);
        updatedOrders = mock(Event.class);

        orderCaptor = ArgumentCaptor.forClass(Order.class);
        when(barista.retrieveOrderStatus(orderCaptor.capture())).thenReturn(OrderStatus.PREPARING);
    }

    @Override
    public void processOrder(Order order) {
        when(orderRepository.findById(order.getId())).thenReturn(order);
        super.processOrder(order);
    }

    public void verifyProcessOrders(List<Order> orders) {
        verify(barista, times(orders.size())).retrieveOrderStatus(any());
        assertThat(orderCaptor.getAllValues()).containsExactlyElementsOf(orders);
    }
}
