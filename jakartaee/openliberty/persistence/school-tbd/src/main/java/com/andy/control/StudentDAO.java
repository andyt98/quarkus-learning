package com.andy.control;

import com.andy.entity.Student;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class StudentDAO {

    @PersistenceContext(unitName = "postgresPU")
    private EntityManager entityManager;

    @Transactional
    public Student createStudent(Student student) {
        entityManager.persist(student);
        return student;
    }

    public Student getStudentById(UUID studentId) {
        return entityManager.find(Student.class, studentId);
    }

    public List<Student> getAllStudents() {
        return entityManager.createQuery("SELECT s FROM Student s", Student.class)
                .getResultList();
    }

    @Transactional
    public Student updateStudent(Student student) {
        return entityManager.merge(student);
    }


    @Transactional
    public void deleteStudent(UUID studentId) {
        Student student = entityManager.find(Student.class, studentId);
        if (student != null) {
            entityManager.remove(student);
        }
    }
}
