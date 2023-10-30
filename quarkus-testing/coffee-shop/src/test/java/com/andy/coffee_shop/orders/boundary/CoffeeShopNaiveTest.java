package com.andy.coffee_shop.orders.boundary;


import com.andy.coffee_shop.orders.control.Barista;
import com.andy.coffee_shop.orders.control.OrderProcessor;
import com.andy.coffee_shop.orders.control.OrderRepository;
import com.andy.coffee_shop.orders.entity.CoffeeType;
import com.andy.coffee_shop.orders.entity.Order;
import com.andy.coffee_shop.orders.entity.OrderStatus;
import com.andy.coffee_shop.orders.entity.Origin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.enterprise.event.Event;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

// don't use such an approach
// too much effort to write & maintain
// -> write use case tests with test doubles instead
class CoffeeShopNaiveTest {

    private CoffeeShop underTest;
    private OrderRepository orderRepository;
    private Barista barista;
    private ArgumentCaptor<Order> orderCaptor;

    @BeforeEach
    void setUp() {
        underTest = new CoffeeShop();
        OrderProcessor orderProcessor = new OrderProcessor();

        underTest.orderProcessor = orderProcessor;
        orderRepository = mock(OrderRepository.class);
        underTest.orderRepository = orderRepository;
        underTest.updatedOrders = mock(Event.class);

        barista = mock(Barista.class);
        setReflectiveField(orderProcessor, "orderRepository", orderRepository);
        setReflectiveField(orderProcessor, "barista", barista);
        setReflectiveField(orderProcessor, "updatedOrders", mock(Event.class));
        orderCaptor = ArgumentCaptor.forClass(Order.class);

        when(barista.retrieveOrderStatus(orderCaptor.capture())).thenReturn(OrderStatus.PREPARING);
    }

    @Test
    void testCreateOrder() {
        Order order = new Order();
        underTest.createOrder(order);
        verify(orderRepository).persist(order);
        verify(underTest.updatedOrders).fire(order);
    }

    @Test
    void testProcessUnfinishedOrders() {
        List<Order> orders = Arrays.asList(
                new Order(UUID.randomUUID(), CoffeeType.ESPRESSO, new Origin("Colombia")),
                new Order(UUID.randomUUID(), CoffeeType.ESPRESSO, new Origin("Ethiopia")));

        when(orderRepository.listUnfinishedOrders()).thenReturn(orders);
        orders.forEach(o -> when(orderRepository.findById(o.getId())).thenReturn(o));

        underTest.processUnfinishedOrders();

        verify(orderRepository).listUnfinishedOrders();

        verify(barista, times(orders.size())).retrieveOrderStatus(any());
        assertThat(orderCaptor.getAllValues()).containsExactlyElementsOf(orders);
    }

    public static void setReflectiveField(Object object, String fieldName, Object value) {
        try {
            Field f1 = object.getClass().getDeclaredField(fieldName);
            f1.setAccessible(true);
            f1.set(object, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
