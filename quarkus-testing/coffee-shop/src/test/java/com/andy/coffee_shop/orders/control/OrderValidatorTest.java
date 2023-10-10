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
        // Creating an instance of the OrderValidator under test
        validator = new OrderValidator();

        // Creating a mock object of the CoffeeShop class
        validator.coffeeShop = mock(CoffeeShop.class);

        // Configuring the mock object behavior
        EnumSet<CoffeeType> coffeeTypes = EnumSet.allOf(CoffeeType.class);
        when(validator.coffeeShop.getCoffeeTypes()).thenReturn(coffeeTypes);
        when(validator.coffeeShop.getOrigin(anyString())).then(i -> {
            // Stubbing the behavior of the getOrigin() method
            String name = i.getArgument(0);
            Origin origin = new Origin(name);
            origin.getCoffeeTypes().addAll(coffeeTypes);
            return origin;
        });

        // Creating a mock object of the ConstraintValidatorContext class
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void empty_json_not_valid() {

        // given
        JsonObject json = Json.createObjectBuilder().build();

        // when
        boolean valid = validator.isValid(json, context);

        // then
        assertThat(valid).isFalse();
    }

    @Test
    void missing_type_not_valid() {

        // given
        JsonObject json = Json.createObjectBuilder().add("origin", "Colombia").build();

        // when
        boolean valid = validator.isValid(json, context);

        // then
        assertThat(valid).isFalse();
    }

    @Test
    void missing_origin_not_valid() {

        // given
        JsonObject json = Json.createObjectBuilder().add("type", "Espresso").build();

        // when
        boolean valid = validator.isValid(json, context);

        // then
        assertThat(valid).isFalse();
    }

    @Test
    void valid_json() {

        // given
        JsonObject json = Json.createObjectBuilder()
                .add("type", "Espresso")
                .add("origin", "Ethiopia")
                .build();

        // when
        boolean valid = validator.isValid(json, context);

        // then
        assertThat(valid).isTrue();
    }


    @ParameterizedTest
    @MethodSource("validData")
    void valid_data(String jsonString) {

        //given
        JsonObject json = Json.createReader(new StringReader(jsonString)).readObject();

        // when
        boolean valid = validator.isValid(json, context);

        // then
        assertThat(valid).isTrue();
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
    void should_reject_invalid_data(String jsonString) {

        // given
        JsonObject json = Json.createReader(new StringReader(jsonString)).readObject();

        // when
        boolean valid = validator.isValid(json, context);

        // then
        assertThat(valid).isFalse();
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