package com.andy.coffee_shop.orders;

import com.andy.coffee_shop.orders.entity.CoffeeType;
import com.andy.coffee_shop.orders.entity.Order;
import com.andy.coffee_shop.orders.entity.Origin;

import java.util.List;
import java.util.UUID;

public final class TestData {
    public static List<Order> unfinishedOrders() {
        return List.of(
                new Order(UUID.randomUUID(), CoffeeType.ESPRESSO, new Origin("Colombia")),
                new Order(UUID.randomUUID(), CoffeeType.ESPRESSO, new Origin("Ethiopia")));
    }
}
