package com.andy.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@SequenceGenerator(name = "professor_seq", sequenceName = "professor_sequence", allocationSize = 1)
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "professor_seq")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Course> courses = new HashSet<>();

    public Professor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}
