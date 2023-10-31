package com.andy.coffee_shop.orders.boundary;

import com.andy.coffee_shop.orders.TestData;
import com.andy.coffee_shop.orders.control.OrderProcessorTestDouble;
import com.andy.coffee_shop.orders.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

// Example of Use Case Test using TestDoubles
public class CoffeeShopUseCaseTest {

    private CoffeeShopTestDouble underTest;

    @BeforeEach
    void setUp() {
        OrderProcessorTestDouble orderProcessor = new OrderProcessorTestDouble();
        underTest = new CoffeeShopTestDouble(orderProcessor);
    }

    @Test
    void verify_createOrder() {
        Order order = new Order();
        underTest.createOrder(order);
        underTest.verifyCreateOrder(order);
    }

    @Test
    void verify_processUnfinishedOrders() {
        List<Order> orders = TestData.unfinishedOrders();

        underTest.answerForUnfinishedOrders(orders);

        underTest.processUnfinishedOrders();

        underTest.verifyProcessUnfinishedOrders(orders);
    }

}
