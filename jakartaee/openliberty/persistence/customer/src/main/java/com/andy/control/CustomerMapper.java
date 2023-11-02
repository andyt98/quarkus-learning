package com.andy.control;

import com.andy.entity.Customer;
import jakarta.enterprise.context.ApplicationScoped;
import org.modelmapper.ModelMapper;

@ApplicationScoped
public class CustomerMapper {

    private final ModelMapper modelMapper;

    public CustomerMapper() {
        this.modelMapper = new ModelMapper();
    }

    public CustomerDTO toDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

    public Customer toEntity(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }
}
