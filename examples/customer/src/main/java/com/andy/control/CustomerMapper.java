package com.andy.control;

import com.andy.entity.Customer;
import com.andy.entity.CustomerDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerMapper {
    public CustomerDTO toDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getUsername(),
                customer.getEmail()
        );
    }
}
