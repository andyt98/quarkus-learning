package com.andy.coffee_shop.backend;


import com.andy.coffee_shop.backend.applications.CoffeeShop;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CoffeeShopSmokeTest {

    private CoffeeShop coffeeShop;

    @BeforeEach
    void setUp() {
        coffeeShop = new CoffeeShop();
    }

    @AfterEach
    void tearDown() {
        coffeeShop.close();
    }

    @Test
    void is_system_up() {
        coffeeShop.verifySystemUp();
    }

    @Test
    void coffee_types_are_available() {
        assertThat(coffeeShop.getTypes()).containsExactlyInAnyOrder("Pour_over", "Latte", "Espresso");
    }


}
