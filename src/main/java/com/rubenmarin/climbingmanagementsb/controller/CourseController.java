package com.rubenmarin.climbingmanagementsb.controller;

import com.rubenmarin.climbingmanagementsb.record.Course;
import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.service.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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



}
