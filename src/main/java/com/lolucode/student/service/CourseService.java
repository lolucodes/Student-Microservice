package com.lolucode.student.service;

import com.lolucode.student.controller.CourseController;
import com.lolucode.student.model.Course;
import com.lolucode.student.repository.CourseRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CourseService {
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public CollectionModel<EntityModel<Course>> getAllCoursesJson(){
        List<EntityModel<Course>> courseList = courseRepository.findAll()
                .stream()
                .map(this::getCourseEntityModel)
                .collect(Collectors.toList());
        return CollectionModel.of(courseList, linkTo(methodOn(CourseController.class)
                .getCoursesJson())
                .withSelfRel());
    }

    public EntityModel<Course> getCourseByIdJson(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course with id" + id + " not found. "));
        return getCourseEntityModel(course);

    }

    public ResponseEntity<EntityModel<Course>> createNewCourseJson(Course newCourse) {
        Course savedCourse = courseRepository.save(newCourse);
        EntityModel<Course> entityModel = getCourseEntityModel(savedCourse);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    public ResponseEntity<EntityModel<Course>> updateCourseJson(Long id, Course newCourse) {
        Course existingCourse = courseRepository.findById(id).orElseThrow(RuntimeException::new);
        existingCourse.setTitle(newCourse.getTitle());
        existingCourse.setDescription(newCourse.getDescription());
        existingCourse.setFee(newCourse.getFee());
        courseRepository.save(existingCourse);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(getCourseEntityModel(existingCourse));
    }

    private EntityModel<Course> getCourseEntityModel(Course course) {
        return EntityModel.of(course,
                linkTo(methodOn(CourseController.class).getCourseJson(course.getId())).withSelfRel(),
                linkTo(methodOn(CourseController.class).getCoursesJson()).withRel("courses"));
    }
}
