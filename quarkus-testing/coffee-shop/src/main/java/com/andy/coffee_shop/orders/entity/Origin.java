package com.andy.coffee_shop.orders.entity;

import javax.persistence.*;
import java.util.EnumSet;
import java.util.Set;

@Entity
@Table(name = "origins")
public class Origin {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "origin_coffee_types",
            joinColumns = @JoinColumn(name = "origin_name", nullable = false))
    @Column(name = "coffee_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private final Set<CoffeeType> coffeeTypes = EnumSet.noneOf(CoffeeType.class);
    @Id
    private String name;

    public Origin() {
    }

    public Origin(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<CoffeeType> getCoffeeTypes() {
        return coffeeTypes;
    }

    @Override
    public String toString() {
        return "Origin{" +
                "name='" + name + '\'' +
                ", coffeeTypes=" + coffeeTypes +
                '}';
    }
}
