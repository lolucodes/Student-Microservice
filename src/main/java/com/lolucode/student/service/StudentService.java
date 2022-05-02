package com.lolucode.student.service;

import com.lolucode.student.controller.CourseController;
import com.lolucode.student.controller.StudentController;
import com.lolucode.student.model.Student;
import com.lolucode.student.model.StudentModelAssembler;
import com.lolucode.student.repository.StudentRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentModelAssembler studentModelAssembler;

    public StudentService(StudentRepository studentRepository, StudentModelAssembler studentModelAssembler) {
        this.studentRepository = studentRepository;
        this.studentModelAssembler = studentModelAssembler;

    }


    public CollectionModel<EntityModel<Student>> getAllStudentsJson() {
        List<EntityModel<Student>> studentList = studentRepository.findAll()
                .stream()
                .map(EntityModel::of)
                .collect(Collectors.toList());
        return CollectionModel.of(studentList, linkTo(methodOn(StudentController.class)
                .getStudentsJson())
                .withSelfRel());
    }


    public EntityModel<Student> getStudentByIdJson(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("student with id" + id + "not found"));
        return EntityModel.of(student,
                linkTo(methodOn(StudentController.class)
                        .getStudentByJson(student.getId())).withSelfRel());
    }


    public ResponseEntity<EntityModel<Student>> createNewStudentJson(Student newStudent) {
        Student savedStudent = studentRepository.save(newStudent);
        EntityModel<Student> entityModel = studentModelAssembler.toModel(savedStudent);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    public ResponseEntity<EntityModel<Student>> updateStudentJson(Long id, Student newStudent) {
        Student existingStudent = studentRepository.findById(id).orElseThrow(RuntimeException::new);
        existingStudent.setSurname(newStudent.getSurname());
        existingStudent.setForename(newStudent.getForename());
        existingStudent.setCoursesEnrolledIn(newStudent.getCoursesEnrolledIn());
        existingStudent.setExternalStudentId(newStudent.getExternalStudentId());
        studentRepository.save(existingStudent);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(getStudentEntityModel(existingStudent));
    }

    public ModelAndView getStudentById(Long id) {
        ModelAndView modelAndView = new ModelAndView("student");
        modelAndView.addObject(studentRepository.findById(id)
                .orElseThrow(RuntimeException::new));
        return modelAndView;

    }

    private EntityModel<Student> getStudentEntityModel(Student student) {
        return EntityModel.of(student,
                linkTo(methodOn(CourseController.class).getCourseJson(student.getId())).withSelfRel(),
                linkTo(methodOn(CourseController.class).getCoursesJson()).withRel("student"));


}
}
