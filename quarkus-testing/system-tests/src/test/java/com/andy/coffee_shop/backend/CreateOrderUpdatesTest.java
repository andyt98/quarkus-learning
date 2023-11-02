package com.andy.coffee_shop.backend;

import com.andy.coffee_shop.backend.applications.CoffeeShop;
import com.andy.coffee_shop.backend.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

public class CreateOrderUpdatesTest {

    private CoffeeShop coffeeShop;

    @BeforeEach
    void setUp() {
        coffeeShop = new CoffeeShop();
    }

    @Test
    void create_order_should_send_update() {
        coffeeShop.registerOrderUpdates();
        URI orderUri = coffeeShop.createOrder(new Order("Espresso", "Colombia"));
    }

}
