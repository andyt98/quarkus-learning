package com.andy;

import com.andy.Customer;
import com.andy.CustomerDAO;
import com.andy.CustomerDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CustomerService {

    @Inject
    private CustomerDAO customerDAO;

    public Optional<Customer> findById(Long id) {
        return customerDAO.findById(id);
    }

    public List<Customer> findAll() {
        return customerDAO.findAll();
    }

    @Transactional
    public Customer create(CustomerDTO customerDTO) {
        Customer customer = new Customer(customerDTO.getUsername(), customerDTO.getEmail());
        return customerDAO.create(customer);
    }

    @Transactional
    public Customer update(Long id, CustomerDTO customerDTO) {
        Customer existingCustomer = customerDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        existingCustomer.setUsername(customerDTO.getUsername());
        existingCustomer.setEmail(customerDTO.getEmail());

        return customerDAO.update(existingCustomer);
    }

    @Transactional
    public void delete(Long id) {
        customerDAO.delete(id);
    }
}
