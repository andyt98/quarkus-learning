package com.andy.control;

import com.andy.entity.Course;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class CourseDAO {

    @PersistenceContext(unitName = "postgresPU")
    private EntityManager entityManager;

    @Transactional
    public Course createCourse(Course course) {
        entityManager.persist(course);
        return course;
    }

    public Course getCourseById(Long courseId) {
        return entityManager.find(Course.class, courseId);
    }

    public List<Course> getAllCourses() {
        return entityManager.createQuery("SELECT c FROM Course c", Course.class)
                .getResultList();
    }

    @Transactional
    public Course updateCourse(Course course) {
        return entityManager.merge(course);
    }

    @Transactional
    public boolean deleteCourse(Long courseId) {
        Course course = entityManager.find(Course.class, courseId);
        if (course != null) {
            entityManager.remove(course);
            return true;
        }
        return false;
    }

}
