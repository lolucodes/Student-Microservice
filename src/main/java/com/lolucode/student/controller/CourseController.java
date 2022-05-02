package com.lolucode.student.controller;

import com.lolucode.student.model.Course;
import com.lolucode.student.service.CourseService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {

        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public ModelAndView getCourses(){
        List<Course> courseList = courseService.getAllCourses();
        ModelAndView modelAndView = new ModelAndView("courses");
        modelAndView.addObject("courses",courseList);
        return modelAndView;
    }

    @GetMapping("/api/courses")
    @ResponseBody
    public CollectionModel<EntityModel<Course>> getCoursesJson() {

        return courseService.getAllCoursesJson();
    }

    @GetMapping("/api/courses/{id}")
    @ResponseBody
    public EntityModel<Course> getCourseJson(@PathVariable Long id) {

        return courseService.getCourseByIdJson(id);
    }

    @PostMapping("/api/courses/")
    ResponseEntity<EntityModel<Course>> createCourseJson(@RequestBody Course newCourse ) {
        return courseService.createNewCourseJson(newCourse);
    }

    @PutMapping("/api/courses/{id}")
    ResponseEntity<?> editCourseJson(@PathVariable Long id, @RequestBody Course newCourse) {
        return courseService.updateCourseJson(id,newCourse);
    }
}

