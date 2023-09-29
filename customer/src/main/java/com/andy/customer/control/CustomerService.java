package com.andy.customer.control;


import com.andy.customer.entity.CustomerCreateRequest;
import com.andy.customer.entity.Customer;
import com.andy.customer.entity.CustomerDto;
import com.andy.customer.entity.CustomerMapper;
import com.andy.customer.exception.DuplicateResourceException;
import com.andy.customer.exception.ErrorMessages;
import com.andy.customer.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerDao customerDAO;

    @Inject
    CustomerMapper customerMapper;


    public List<CustomerDto> findAll() {
        return customerDAO
                .listAll()
                .stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CustomerDto findById(UUID id) {
        return customerDAO
                .findByIdOptional(id)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.CUSTOMER_NOT_FOUND.message(), "id " + id))
                );
    }

    public CustomerDto findByEmail(String email) {
        return customerDAO
                .findByEmailOptional(email)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.CUSTOMER_NOT_FOUND.message(), "email " + email))
                );
    }

    public void create(CustomerCreateRequest createCustomerRequest) {
        String email = createCustomerRequest.getEmail();
        if (customerDAO.existsWithEmail(email)) {
            throw new DuplicateResourceException(ErrorMessages.CUSTOMER_NOT_FOUND.message());
        }

        Customer customer = new Customer(
                createCustomerRequest.getUsername(),
                email,
                createCustomerRequest.getCustomerType()
        );

        customerDAO.persist(customer);
    }

    public void delete(UUID id) {
        if (!customerDAO.existsWithId(id)) {
            throw new ResourceNotFoundException(
                    String.format(ErrorMessages.CUSTOMER_NOT_FOUND.message(), "id " + id)
            );
        }

        customerDAO.deleteById(id);
    }
}
