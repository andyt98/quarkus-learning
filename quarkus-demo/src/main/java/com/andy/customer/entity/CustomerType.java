package com.andy.customer.entity;

import com.andy.customer.control.CustomerTypeSerializer;
import jakarta.json.bind.annotation.JsonbTypeSerializer;

@JsonbTypeSerializer(CustomerTypeSerializer.class)
public enum CustomerType {
    REGULAR,
    PREMIUM,
    VIP
}