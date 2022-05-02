package com.lolucode.student.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(unique = true)
    private String externalStudentId;

    private String surname;

    private String forename;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "course_student",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))

    @EqualsAndHashCode.Exclude
    @ToString.Exclude

    Set<Course> coursesEnrolledIn;
    public void enrollInCourse(Course course) {
        if (coursesEnrolledIn == null) {
            coursesEnrolledIn = new HashSet<>();
        }
        coursesEnrolledIn.add(course);
    }

}