package com.andy.control;

import com.andy.entity.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class CustomerRepository implements PanacheRepositoryBase<Customer, Long> {
    public Optional<Customer> findByEmailOptional(String email) {
        return find("email", email).firstResultOptional();
    }
}
