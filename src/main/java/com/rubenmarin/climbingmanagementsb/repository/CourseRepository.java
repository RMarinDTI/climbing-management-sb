package com.rubenmarin.climbingmanagementsb.repository;

import com.rubenmarin.climbingmanagementsb.record.Course;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CourseRepository {

    List<Course> findAll();

}
