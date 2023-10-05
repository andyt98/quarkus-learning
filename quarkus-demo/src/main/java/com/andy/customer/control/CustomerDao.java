package com.andy.customer.control;

import com.andy.customer.entity.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerDao {
    Optional<Customer> findByIdOptional(UUID uuid);

    Optional<Customer> findByEmailOptional(String email);

    List<Customer> listAll();

    boolean existsWithId(UUID uuid);

    boolean existsWithEmail(String email);

    void persist(Customer customer);

    void deleteById(UUID id);
}
