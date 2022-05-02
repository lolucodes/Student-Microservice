package com.lolucode.student.model;

import com.lolucode.student.controller.StudentController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StudentModelAssembler implements RepresentationModelAssembler<Student, EntityModel<Student>> {
    @Override
    public EntityModel<Student> toModel(Student student) {
        if (student.getId() == null || student.getId() == 0) {
            throw new RuntimeException("Student not valid.");
        }
        return EntityModel.of(student,
                linkTo(methodOn(StudentController.class).getStudentJson(student.getId())).withSelfRel(),
                linkTo(methodOn(StudentController.class).getStudentsJson()).withRel("students"));
    }
}
