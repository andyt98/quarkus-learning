package com.andy.coffee_shop.backend.entity;

public class Order {

    public String type;
    public String origin;

    public Order(String type, String origin) {
        this.type = type;
        this.origin = origin;
    }

    public Order() {
    }
}
