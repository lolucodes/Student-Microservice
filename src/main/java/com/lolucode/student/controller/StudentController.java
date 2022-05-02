package com.lolucode.student.controller;

import com.lolucode.student.model.Student;
import com.lolucode.student.service.IntegrationService;
import com.lolucode.student.service.StudentService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StudentController {

    private final StudentService studentService;

    private final IntegrationService integrationService;

    public StudentController(StudentService studentService,
                             IntegrationService integrationService) {

        this.studentService = studentService;
        this.integrationService = integrationService;
    }

    @GetMapping("/api/students/{id}")
    @ResponseBody
    public EntityModel<Student> getStudentByJson(
            @PathVariable Long id) {
        return studentService.getStudentByIdJson(id);
    }

    @GetMapping("/students/{id}")
    public ModelAndView getStudent(@PathVariable Long id) {

        return studentService.getStudentById(id);
    }

    @GetMapping("/api/students")
    @ResponseBody
    public CollectionModel<EntityModel<Student>> getStudentsJson() {
        return studentService.getAllStudentsJson();
    }

    public EntityModel<Student> getStudentJson(@PathVariable Long id) {

        return studentService.getStudentByIdJson(id);
    }

    @PostMapping("/api/students/")
    ResponseEntity<EntityModel<Student>> createStudentJson(@RequestBody Student newStudent) {

        return studentService.createNewStudentJson(newStudent);
    }

    @PutMapping("/api/students/")
    ResponseEntity<?> editStudentJson(@PathVariable Long id, @RequestBody Student newStudent) {
        return studentService.updateStudentJson(id, newStudent);
    }
}
