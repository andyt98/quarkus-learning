package com.andy.control;

import com.andy.boundary.CustomerCreateRequest;
import com.andy.boundary.CustomerUpdateRequest;
import com.andy.entity.Customer;
import com.andy.entity.CustomerDTO;
import com.andy.exception.DuplicateResourceException;
import com.andy.exception.RequestValidationException;
import com.andy.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerRepository customerRepository;

    @Inject
    CustomerMapper customerMapper;

    private static final String EMAIL_ALREADY_TAKEN = "Email already taken.";
    private static final String CUSTOMER_NOT_FOUND = "Customer with [%s] not found.";

    @Transactional
    public void create(CustomerCreateRequest createCustomerRequest) {
        String email = createCustomerRequest.getEmail();

        if (customerRepository.findByEmailOptional(email).isPresent()) {
            throw new DuplicateResourceException(EMAIL_ALREADY_TAKEN);
        }

        Customer customer = new Customer(
                createCustomerRequest.getUsername(),
                email
        );

        customerRepository.persist(customer);
    }

    public List<CustomerDTO> findAll() {
        return customerRepository
                .findAll()
                .stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO findById(Long id) {
        return customerRepository
                .findByIdOptional(id)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(CUSTOMER_NOT_FOUND, "id " + id))
                );
    }

    public CustomerDTO findByEmail(String email) {
        return customerRepository
                .findByEmailOptional(email)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(CUSTOMER_NOT_FOUND, "email " + email))
                );
    }


    @Transactional
    public void update(Long id, CustomerUpdateRequest customerUpdateRequest) {

        Customer customer = customerRepository
                .findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(CUSTOMER_NOT_FOUND, id))
                );

        String updateRequestEmail = customerUpdateRequest.getEmail();
        String updateRequestUsername = customerUpdateRequest.getUsername();

        boolean isEmailChanged = !updateRequestEmail.equals(customer.getEmail());
        boolean isUsernameChanged = !updateRequestUsername.equals(customer.getUsername());

        if (isEmailChanged &&
                customerRepository.findByEmailOptional(updateRequestEmail).isPresent()) {
            throw new DuplicateResourceException(EMAIL_ALREADY_TAKEN);
        }

        if (isEmailChanged) {
            customer.setEmail(updateRequestEmail);
        }

        if (isUsernameChanged) {
            customer.setUsername(updateRequestUsername);
        }

        if (!isEmailChanged && !isUsernameChanged) {
            throw new RequestValidationException("No data changes found.");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (customerRepository.findByIdOptional(id).isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format(CUSTOMER_NOT_FOUND, "id " + id)
            );
        }

        customerRepository.deleteById(id);
    }
}
