package com.rubenmarin.climbingmanagementsb.controller;

import com.rubenmarin.climbingmanagementsb.record.Course;
import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Climbing Management API";
    }

    @GetMapping("/courses")
    public List<Course> getCourses() {
        return courseService.findAll();
    }

    @GetMapping("/courses/{id}")
    public Course getCourseById(@PathVariable Long id) {
        return courseService.findById(id);
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) {
        Course created = courseService.create(course);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping ("/courses/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @Valid @RequestBody Course course) {
        Course updated = courseService.update(id, course);

        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping ("/courses/{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable Long id) {
        Course deleted = courseService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
