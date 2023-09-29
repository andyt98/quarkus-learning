package com.andy.customer.control;

import com.andy.customer.entity.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerRepository implements PanacheRepositoryBase<Customer, UUID> {

    @Override
    public Optional<Customer> findByIdOptional(UUID uuid) {
        return PanacheRepositoryBase.super.findByIdOptional(uuid);
    }

    @Override
    public List<Customer> listAll() {
        return PanacheRepositoryBase.super.listAll();
    }

    @Override
    public void persist(Customer customer) {
        PanacheRepositoryBase.super.persist(customer);
    }

    @Override
    public boolean deleteById(UUID uuid) {
        return PanacheRepositoryBase.super.deleteById(uuid);
    }


}
