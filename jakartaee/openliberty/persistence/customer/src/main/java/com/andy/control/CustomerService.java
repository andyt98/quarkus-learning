package com.andy.control;

import com.andy.boundary.*;
import com.andy.entity.Customer;
import com.andy.exception.DuplicateResourceException;
import com.andy.exception.RequestValidationException;
import com.andy.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomerService {

    @Inject
    private CustomerDAO customerDAO;

    @Inject
    CustomerMapper customerMapper;

    private static final String EMAIL_ALREADY_TAKEN = "Email already taken.";
    private static final String CUSTOMER_NOT_FOUND = "Customer with [%s] not found.";

    public void create(CustomerCreateRequest createCustomerRequest) {
        String email = createCustomerRequest.getEmail();
        if (customerDAO.existsCustomerWithEmail(email)) {
            throw new DuplicateResourceException(EMAIL_ALREADY_TAKEN);
        }

        Customer customer = new Customer(
                createCustomerRequest.getUsername(),
                email
        );

        customerDAO.createCustomer(customer);
    }

    public List<CustomerDTO> findAll() {
        return customerDAO
                .findAllCustomers()
                .stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO findById(Long id) {
        return customerDAO
                .findCustomerById(id)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(CUSTOMER_NOT_FOUND, "id " + id))
                );
    }

    public CustomerDTO findByEmail(String email) {
        return customerDAO
                .findCustomerByEmail(email)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(CUSTOMER_NOT_FOUND, "email " + email))
                );
    }


    public void update(Long id, CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = customerDAO
                .findCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(CUSTOMER_NOT_FOUND, id))
                );

        String updateRequestEmail = customerUpdateRequest.getEmail();
        String updateRequestUsername = customerUpdateRequest.getUsername();

        if (!updateRequestEmail.equals(customer.getEmail()) &&
                customerDAO.existsCustomerWithEmail(updateRequestEmail)) {
            throw new DuplicateResourceException(EMAIL_ALREADY_TAKEN);
        }

        if (!updateRequestEmail.equals(customer.getEmail())) {
            customer.setEmail(updateRequestEmail);
        }

        if (!updateRequestUsername.equals(customer.getUsername())) {
            customer.setUsername(updateRequestUsername);
        }

        if (!updateRequestEmail.equals(customer.getEmail()) ||
                !updateRequestUsername.equals(customer.getUsername())) {
            customerDAO.updateCustomer(customer);
        } else {
            throw new RequestValidationException("No data changes found.");
        }
    }

    public void delete(Long id) {
        if (!customerDAO.existsCustomerWithId(id)) {
            throw new ResourceNotFoundException(
                    String.format(CUSTOMER_NOT_FOUND, "id " + id)
            );
        }

        customerDAO.deleteCustomerById(id);
    }
}
