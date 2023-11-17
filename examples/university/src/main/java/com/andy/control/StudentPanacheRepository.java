package com.andy.control;

import com.andy.entity.Student;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.UUID;

public class StudentPanacheRepository implements PanacheRepositoryBase<Student, UUID> {


}
