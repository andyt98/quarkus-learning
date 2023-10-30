package com.andy.coffee_shop.orders.control;

import com.andy.coffee_shop.orders.entity.Order;
import com.andy.coffee_shop.orders.entity.OrderStatus;
import org.mockito.ArgumentCaptor;

import javax.enterprise.event.Event;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OrderProcessorTestDouble extends OrderProcessor {

    // Create an ArgumentCaptor to capture the arguments passed to the barista.retrieveOrderStatus method.
    private final ArgumentCaptor<Order> orderCaptor;

    public OrderProcessorTestDouble() {
        orderRepository = mock(OrderRepository.class);
        barista = mock(Barista.class);
        updatedOrders = mock(Event.class);

        // Initialize the ArgumentCaptor to capture Order objects.
        orderCaptor = ArgumentCaptor.forClass(Order.class);

        when(barista.retrieveOrderStatus(orderCaptor.capture()))
                .thenReturn(OrderStatus.PREPARING);
    }

    @Override
    public void processOrder(Order order) {
        when(orderRepository.findById(order.getId())).thenReturn(order);
        super.processOrder(order);
    }

    public void verifyProcessOrders(List<Order> orders) {
        // Verify that barista.retrieveOrderStatus was called exactly as many times as the number of orders in the list.
        verify(barista, times(orders.size())).retrieveOrderStatus(any());

        // Use AssertJ to assert that the captured Order objects (captured by ArgumentCaptor) match the expected orders.
        // This ensures that the correct Order objects were passed to barista.retrieveOrderStatus during processing.
        assertThat(orderCaptor.getAllValues()).containsExactlyElementsOf(orders);
    }
}
