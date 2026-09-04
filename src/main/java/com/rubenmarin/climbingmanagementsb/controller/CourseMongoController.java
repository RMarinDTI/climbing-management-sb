package com.rubenmarin.climbingmanagementsb.controller;

import com.rubenmarin.climbingmanagementsb.document.CourseMongoDocument;
import com.rubenmarin.climbingmanagementsb.service.CourseMongoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mongo/courses")
public class CourseMongoController {

    private final CourseMongoService courseMongoService;

    public CourseMongoController(CourseMongoService courseMongoService) {
        this.courseMongoService = courseMongoService;
    }



    @GetMapping
    public List<CourseMongoDocument> getCourses() {
        return courseMongoService.findAll();
    }

    @GetMapping("/{id}")
    public CourseMongoDocument getCourseById(@PathVariable String id) {
        return courseMongoService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CourseMongoDocument> createCourse(@RequestBody CourseMongoDocument course) {
        CourseMongoDocument created = courseMongoService.create(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public CourseMongoDocument updateCourse(@PathVariable String id, @RequestBody CourseMongoDocument course) {
        return courseMongoService.update(id, course);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        courseMongoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/database")
    public String getDatabaseName() {
        return courseMongoService.getDatabaseName();
    }

    @GetMapping("/config")
    public String getMongoUri() {
        return courseMongoService.getMongoUri();
    }

    @GetMapping("/connection")
    public String getMongoConnection() {
        return courseMongoService.getMongoConnectionDetails();
    }

    @GetMapping("/connection-class")
    public String getMongoConnectionDetailsClass() {
        return courseMongoService.getMongoConnectionDetailsClass();
    }

    @GetMapping("/mongo-properties")
    public String getMongoProperties() {
        return courseMongoService.getMongoProperties();
    }
}
