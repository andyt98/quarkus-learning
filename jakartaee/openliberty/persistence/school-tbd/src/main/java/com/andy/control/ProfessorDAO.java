package com.andy.control;

import com.andy.entity.Professor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;

import java.util.List;

@ApplicationScoped
public class ProfessorDAO {

    @PersistenceUnit(unitName = "postgresPU")
    private  EntityManagerFactory entityManagerFactory;

    public Professor createProfessor(Professor professor) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(professor);
            entityManager.getTransaction().commit();
            return professor;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to create professor: " + e.getMessage(), e);
        } finally {
            entityManager.close();
        }
    }

    public Professor getProfessorById(Long professorId) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.find(Professor.class, professorId);
        }
    }

    public List<Professor> getAllProfessors() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT p FROM Professor p", Professor.class)
                    .getResultList();
        }
    }

    public Professor updateProfessor(Professor professor) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Professor updatedProfessor = entityManager.merge(professor);
            entityManager.getTransaction().commit();
            return updatedProfessor;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update professor: " + e.getMessage(), e);
        } finally {
            entityManager.close();
        }
    }

    public void deleteProfessor(Long professorId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Professor professor = entityManager.find(Professor.class, professorId);
            if (professor != null) {
                entityManager.remove(professor);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete professor: " + e.getMessage(), e);
        } finally {
            entityManager.close();
        }
    }

}
