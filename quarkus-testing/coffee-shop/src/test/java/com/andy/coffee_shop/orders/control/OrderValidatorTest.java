package com.andy.coffee_shop.orders.control;

import com.andy.coffee_shop.orders.boundary.CoffeeShop;
import com.andy.coffee_shop.orders.entity.CoffeeType;
import com.andy.coffee_shop.orders.entity.Origin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.ConstraintValidatorContext;
import java.io.StringReader;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderValidatorTest {

    private OrderValidator validator;
    private ConstraintValidatorContext context;


    @BeforeEach
    void setUp() {
        validator = new OrderValidator();
        validator.coffeeShop = mock(CoffeeShop.class);
        EnumSet<CoffeeType> coffeeTypes = EnumSet.allOf(CoffeeType.class);
        when(validator.coffeeShop.getCoffeeTypes()).thenReturn(coffeeTypes);
        when(validator.coffeeShop.getOrigin(anyString())).then(i -> {
            String name = i.getArgument(0);
            Origin origin = new Origin(name);
            origin.getCoffeeTypes().addAll(coffeeTypes);
            return origin;
        });
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void empty_json_not_valid() {
        JsonObject json = Json.createObjectBuilder().build();
        assertThat(validator.isValid(json, context)).isFalse();
    }

    @Test
    void missing_type_not_valid() {
        JsonObject json = Json.createObjectBuilder().add("origin", "Colombia").build();
        assertThat(validator.isValid(json, context)).isFalse();
    }

    @Test
    void missing_origin_not_valid() {
        JsonObject json = Json.createObjectBuilder().add("type", "Espresso").build();
        assertThat(validator.isValid(json, context)).isFalse();
    }

    @Test
    void valid_json() {
        JsonObject json = Json.createObjectBuilder()
                .add("type", "Espresso")
                .add("origin", "Ethiopia")
                .build();
        assertThat(validator.isValid(json, context)).isTrue();
    }


    @ParameterizedTest
    @MethodSource("validData")
    void valid_data(String json) {
        JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();
        assertThat(validator.isValid(jsonObject, context)).isTrue();
    }

    public static Collection<String> validData() {
        return List.of(
                """
                        {
                            "type": "Espresso",
                            "origin": "Ethiopia"
                        }
                        """
                ,
                """
                        {
                            "type": "Espresso",
                            "origin": "Colombia"
                        }
                        """
                ,
                """
                        {
                            "type": "Latte",
                            "origin": "Ethiopia"
                        }
                        """
        );
    }


    @ParameterizedTest
    @MethodSource("invalidData")
    void should_reject_invalid_data(String json) {
        JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();
        assertThat(validator.isValid(jsonObject, context)).isFalse();
    }

    public static Collection<String> invalidData() {
        return List.of(
                "{}"
                ,
                """
                        {
                            "type": "Espresso"
                        }
                        """
                ,
                """
                        {
                            "origin": "Ethiopia"
                        }
                        """
                ,
                """
                        {
                            "type": "Cappuccino",
                            "origin": "Ethiopia"
                        }
                        """
        );
    }


}