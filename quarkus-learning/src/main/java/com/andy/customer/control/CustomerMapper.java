package com.andy.customer.control;

import com.andy.customer.entity.Customer;
import com.andy.customer.entity.CustomerDto;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerMapper {

    public CustomerDto toDTO(Customer customer) {
        return new CustomerDto(customer.getUuid(),
                customer.getName(),
                customer.getEmail(),
                customer.getCustomerType(),
                customer.getCreatedAt()
        );
    }

}
