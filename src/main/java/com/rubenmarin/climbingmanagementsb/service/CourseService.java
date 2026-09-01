package com.rubenmarin.climbingmanagementsb.service;

import com.rubenmarin.climbingmanagementsb.record.Course;
import com.rubenmarin.climbingmanagementsb.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }
}
