package com.andy.customer.entity;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerMapper {

    public CustomerDto toDTO(Customer customer) {
        return new CustomerDto(customer.getUuid(),
                customer.getName(),
                customer.getEmail(),
                customer.getCustomerType()
        );
    }

}
