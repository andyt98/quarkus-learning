package com.andy.customer.control;

import com.andy.customer.entity.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerRepository implements PanacheRepositoryBase<Customer, UUID> {

    public Optional<Customer> findByEmailOptional(String email) {
        return find("email", email).firstResultOptional();
    }

    public boolean existsWithEmail(String email) {
        return findByEmailOptional(email).isPresent();
    }

    public boolean existsWithId(UUID id) {
        return findByIdOptional(id).isPresent();
    }
}
