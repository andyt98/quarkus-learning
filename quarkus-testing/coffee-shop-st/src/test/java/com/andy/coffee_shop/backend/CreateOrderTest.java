package com.andy.coffee_shop.backend;


import com.andy.coffee_shop.backend.applications.CoffeeShop;
import com.andy.coffee_shop.backend.entity.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateOrderTest {

    private CoffeeShop coffeeShop;

    @BeforeEach
    void setUp() {
        coffeeShop = new CoffeeShop();
    }

    @Test
    void create_order() {
        URI id = coffeeShop.createOrder(new Order("Espresso", "Colombia"));

        Order order = coffeeShop.retrieveOrder(id);

        assertThat(order.type).isEqualTo("Espresso");
        assertThat(order.origin).isEqualTo("Colombia");
    }

    @AfterEach
    void tearDown() {
        coffeeShop.close();
    }

}
