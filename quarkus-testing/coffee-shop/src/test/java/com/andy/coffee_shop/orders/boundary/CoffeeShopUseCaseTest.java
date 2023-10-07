package com.andy.coffee_shop.orders.boundary;

import com.andy.coffee_shop.orders.control.OrderProcessorTestDouble;
import com.andy.coffee_shop.orders.entity.CoffeeType;
import com.andy.coffee_shop.orders.entity.Order;
import com.andy.coffee_shop.orders.entity.Origin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CoffeeShopUseCaseTest {

    private CoffeeShopTestDouble coffeeShop;

    @BeforeEach
    void setUp() {
        OrderProcessorTestDouble orderProcessor = new OrderProcessorTestDouble();
        coffeeShop = new CoffeeShopTestDouble(orderProcessor);
    }

    @Test
    void verify_createOrder() {
        Order order = new Order();
        coffeeShop.createOrder(order);
        coffeeShop.verifyCreateOrder(order);
    }

    @Test
    void verify_processUnfinishedOrders() {
        List<Order> orders = Arrays.asList(
                new Order(UUID.randomUUID(), CoffeeType.ESPRESSO, new Origin("Colombia")),
                new Order(UUID.randomUUID(), CoffeeType.ESPRESSO, new Origin("Ethiopia")));

        coffeeShop.answerForUnfinishedOrders(orders);

        coffeeShop.processUnfinishedOrders();

        coffeeShop.verifyProcessUnfinishedOrders(orders);
    }

}
