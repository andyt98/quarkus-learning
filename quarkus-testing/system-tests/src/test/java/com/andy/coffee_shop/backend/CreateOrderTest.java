package com.andy.coffee_shop.backend;

import com.andy.coffee_shop.backend.applications.Barista;
import com.andy.coffee_shop.backend.applications.CoffeeShop;
import com.andy.coffee_shop.backend.entity.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateOrderTest {

    private CoffeeShop coffeeShop;
    private Barista barista;

    @BeforeEach
    void setUp() {
        coffeeShop = new CoffeeShop();
        barista = new Barista();
    }

    @Test
    void create_order_verify() {
        URI orderUri = coffeeShop.createOrder(new Order("Espresso", "Colombia"));

        Order order = coffeeShop.retrieveOrder(orderUri);

        assertThat(order.getType()).isEqualTo("Espresso");
        assertThat(order.getOrigin()).isEqualTo("Colombia");

        assertThat(coffeeShop.getOrders()).contains(orderUri);
    }

    @Test
    void create_order_verify_status_update() {
        Order order = new Order("Espresso", "Colombia");
        URI orderUri = coffeeShop.createOrder(order);

        barista.answerForOrder(orderUri, "PREPARING");
        barista.waitForInvocation(orderUri, "PREPARING");
        assertThat(coffeeShop.retrieveOrder(orderUri).getStatus()).isEqualTo("Preparing");
        barista.verifyRequests(orderUri, order);
        barista.debug(orderUri);

        barista.answerForOrder(orderUri, "FINISHED");
        barista.waitForInvocation(orderUri, "PREPARING");
        assertThat(coffeeShop.retrieveOrder(orderUri).getStatus()).isEqualTo("Finished");
        barista.verifyRequests(orderUri, order);
        barista.debug(orderUri);

        barista.answerForOrder(orderUri, "COLLECTED");
        barista.waitForInvocation(orderUri, "FINISHED");
        assertThat(coffeeShop.retrieveOrder(orderUri).getStatus()).isEqualTo("Collected");
        barista.verifyRequests(orderUri, order);
        barista.debug(orderUri);
    }

    @AfterEach
    void tearDown() {
        coffeeShop.close();
    }

}
