package com.rubenmarin.climbingmanagementsb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {

    @GetMapping("/hello")
    public String hello() {
        return "Climbing Management API";
    }
}
