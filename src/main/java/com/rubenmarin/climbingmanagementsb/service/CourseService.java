package com.rubenmarin.climbingmanagementsb.service;

import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.record.Course;
import com.rubenmarin.climbingmanagementsb.repository.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));
    }

    public Course create( @Valid @RequestBody Course course) {
        return courseRepository.save(course);
    }

    public Course update( Long id , @Valid @RequestBody Course course) {
        return courseRepository.update(id, course).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));
    }

    public Course delete( Long id ) {
        return courseRepository.delete(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));
    }

    public Course findMostExpensive() {
        List<Course> courses = courseRepository.findAll();
        Course mostExpensive = courses.stream().max(Comparator.comparing(Course :: price)).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));
        return mostExpensive;
    }

    public  List<Course> findByDifficulty(Difficulty difficulty) {
        List<Course> coursesByDif = courseRepository.findAll().stream().filter(course -> course.difficulty().equals(difficulty)).toList();
        return coursesByDif;
    }

}
