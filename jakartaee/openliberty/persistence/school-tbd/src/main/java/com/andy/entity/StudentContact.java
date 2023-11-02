package com.andy.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class StudentContact {

    @Id
    private UUID studentId;
    @Column(unique = true, nullable = false)
    private String email;
    @Embedded
    @Column(nullable = false)
    private Address address;
    private String phoneNumber;
    @OneToOne
    private Student student;

    public StudentContact() {
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
