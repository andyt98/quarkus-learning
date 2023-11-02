package com.andy.control;


import com.andy.entity.Customer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CustomerDAO {

    @PersistenceContext(unitName = "postgresPU")
    private EntityManager entityManager;

    public Optional<Customer> findCustomerById(Long id) {
        return Optional.ofNullable(entityManager.find(Customer.class, id));
    }

    public Optional<Customer> findCustomerByEmail(String email) {
        TypedQuery<Customer> query = entityManager.createQuery(
                "SELECT c FROM Customer c WHERE c.email = :email",
                Customer.class
        );
        query.setParameter("email", email);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<Customer> findAllCustomers() {
        return entityManager.createQuery("SELECT c FROM Customer c", Customer.class)
                .getResultList();
    }

    public boolean existsCustomerWithId(Long id) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Customer c WHERE c.id = :customerId",
                Long.class
        );
        query.setParameter("customerId", id);

        Long count = query.getSingleResult();
        return count > 0;
    }


    public boolean existsCustomerWithEmail(String email) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Customer> customerRoot = query.from(Customer.class);

        Predicate emailPredicate = criteriaBuilder.equal(customerRoot.get("email"), email);
        query.select(criteriaBuilder.count(customerRoot)).where(emailPredicate);

        Long count = entityManager.createQuery(query).getSingleResult();
        return count > 0;
    }

    @Transactional
    public void createCustomer(Customer customer) {
        entityManager.persist(customer);
    }

    @Transactional
    public void updateCustomer(Customer customer) {
        entityManager.merge(customer);
    }

    @Transactional
    public void deleteCustomerById(Long id) {
        Customer customer = entityManager.find(Customer.class, id);
        if (customer != null) {
            entityManager.remove(customer);
        }
    }
}
