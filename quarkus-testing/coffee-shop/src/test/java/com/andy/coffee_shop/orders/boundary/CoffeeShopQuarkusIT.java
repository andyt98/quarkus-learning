package com.andy.coffee_shop.orders.boundary;

import com.andy.coffee_shop.orders.entity.Order;
import com.andy.coffee_shop.orders.entity.Origin;
import com.andy.coffee_shop.orders.entity.CoffeeType;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@Disabled("turnaround time too slow")
public class CoffeeShopQuarkusIT {

    @Inject
    CoffeeShop coffeeShop;

    @Test
    void test() {
        UUID id = UUID.randomUUID();
        Origin colombia = new Origin("Colombia");
        Order order = new Order(id, CoffeeType.ESPRESSO, colombia);
        coffeeShop.createOrder(order);

        Order loaded = coffeeShop.getOrder(id);
        assertThat(loaded.getType()).isEqualTo(CoffeeType.ESPRESSO);
        assertThat(loaded.getOrigin().getName()).isEqualTo(colombia.getName());
        Assertions.assertThat(loaded.getOrigin().getCoffeeTypes()).contains(CoffeeType.ESPRESSO);
    }

}
