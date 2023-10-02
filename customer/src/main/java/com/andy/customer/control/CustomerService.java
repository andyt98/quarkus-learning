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
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerRepository customerRepository;

    @Inject
    CustomerMapper customerMapper;


    public List<CustomerDto> findAll() {
        return customerRepository
                .listAll()
                .stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CustomerDto findById(UUID id) {
        return customerRepository
                .findByIdOptional(id)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.CUSTOMER_NOT_FOUND.message(), "id " + id))
                );
    }

    public CustomerDto findByEmail(String email) {
        return customerRepository
                .findByEmailOptional(email)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.CUSTOMER_NOT_FOUND.message(), "email " + email))
                );
    }

    @Transactional
    public void create(CustomerCreateRequest createCustomerRequest) {
        String email = createCustomerRequest.getEmail();
        if (customerRepository.existsWithEmail(email)) {
            throw new DuplicateResourceException(ErrorMessages.EMAIL_ALREADY_TAKEN.message());
        }

        Customer customer = new Customer(
                createCustomerRequest.getUsername(),
                email,
                createCustomerRequest.getCustomerType()
         );

        customerRepository.persist(customer);
    }


    @Transactional
    public void delete(UUID id) {
        if (!customerRepository.existsWithId(id)) {
            throw new ResourceNotFoundException(
                    String.format(ErrorMessages.CUSTOMER_NOT_FOUND.message(), "id " + id)
            );
        }

        customerRepository.deleteById(id);
    }
}
