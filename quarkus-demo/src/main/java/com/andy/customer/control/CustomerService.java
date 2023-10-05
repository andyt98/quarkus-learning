package com.andy.customer.control;


import com.andy.customer.entity.Customer;
import com.andy.customer.entity.CustomerCreateRequest;
import com.andy.customer.entity.CustomerDto;
import com.andy.customer.exception.DuplicateResourceException;
import com.andy.customer.exception.ErrorMessages;
import com.andy.customer.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.LockSupport;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerRepository customerRepository;

    @Inject
    CustomerMapper customerMapper;

    @Inject
    @Created
    Event<Customer> customerCreatedEvent;

    @Inject
    ManagedExecutor managedExecutor;

    @Inject
    ThreadContext threadContext;


    public List<CustomerDto> findAll() {

        return customerRepository
                .listAll()
                .stream()
                .map(customerMapper::toDTO)
                .toList();
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


//        customerCreatedEvent.fire(customer);
//        customerCreatedEvent.fireAsync(customer);

        customerRepository.persist(customer);
        managedExecutor.execute(threadContext.contextualRunnable(this::sendNotification));
    }


    private void sendNotification(){
        System.out.println("Sending notification to the newly created customer...");
        LockSupport.parkNanos(2_000_000_000L);
        System.out.println("...done");
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
