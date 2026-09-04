package com.rubenmarin.climbingmanagementsb.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class GlobalController {

    public GlobalController() {

    }

    @GetMapping("/hello")
    public String hello() {
        return "Climbing Management API";
    }

 }
