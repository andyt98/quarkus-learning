package com.andy.customer.control;



import com.andy.customer.entity.Customer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerDaoClassic implements CustomerDao {

    @Inject
    EntityManager entityManager;

    @Override
    public Optional<Customer> findByIdOptional(UUID id) {
        return Optional.ofNullable(entityManager.find(Customer.class, id));
    }

    @Override
    public Optional<Customer> findByEmailOptional(String email) {
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

    @Override
    public List<Customer> listAll() {
        return entityManager.createQuery("SELECT c FROM Customer c", Customer.class)
                .getResultList();
    }

    @Override
    public boolean existsWithId(UUID uuid) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Customer c WHERE c.uuid = :customerId",
                Long.class
        );
        query.setParameter("customerId", uuid);

        Long count = query.getSingleResult();
        return count > 0;
    }


    @Override
    public boolean existsWithEmail(String email) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Customer> customerRoot = query.from(Customer.class);

        Predicate emailPredicate = criteriaBuilder.equal(customerRoot.get("email"), email);
        query.select(criteriaBuilder.count(customerRoot)).where(emailPredicate);

        Long count = entityManager.createQuery(query).getSingleResult();
        return count > 0;
    }

    @Override
    @Transactional
    public void persist(Customer customer) {
        entityManager.persist(customer);
    }


    @Override
    @Transactional
    public void deleteById(UUID id) {
        Customer customer = entityManager.find(Customer.class, id);
        if (customer != null) {
            entityManager.remove(customer);
        }
    }
}
