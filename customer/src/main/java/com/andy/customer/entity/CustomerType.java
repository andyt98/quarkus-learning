package com.andy.customer.entity;

import jakarta.json.bind.annotation.JsonbTypeSerializer;

@JsonbTypeSerializer(CustomerTypeSerializer.class)
public enum CustomerType {
    REGULAR,
    PREMIUM,
    VIP
}