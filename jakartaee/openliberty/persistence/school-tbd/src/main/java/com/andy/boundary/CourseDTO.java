package com.andy.boundary;

import com.andy.entity.Course;
import jakarta.json.bind.annotation.JsonbProperty;

public class CourseDTO {

    private Long id;

    @JsonbProperty("course_name")
    private String courseName;

    @JsonbProperty("professor_id")
    private Long professorId;

    public CourseDTO() {
    }

    public CourseDTO(Course course) {
        this.id = course.getId();
        this.courseName = course.getCourseName();
        this.professorId = course.getProfessor().getId();
    }



    // You can also provide static methods for converting between DTOs and entities
    public static CourseDTO fromEntity(Course course) {
        return new CourseDTO(course);
    }
}
