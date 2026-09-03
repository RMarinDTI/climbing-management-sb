package com.rubenmarin.climbingmanagementsb.controller;

import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.record.CourseRecord;
import com.rubenmarin.climbingmanagementsb.service.CourseServiceJpa;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jpa/courses")
public class CourseControllerJpa {

    private final CourseServiceJpa courseServiceJpa;

    public CourseControllerJpa(CourseServiceJpa courseServiceJpa) {
        this.courseServiceJpa = courseServiceJpa;
    }

    @GetMapping
    public List<CourseRecord> getCourses() {
        return courseServiceJpa.findAll();
    }

    @GetMapping("/{id}")
    public CourseRecord getCourseById(@PathVariable Long id) {
        return courseServiceJpa.findById(id);
    }

    @PostMapping
    public ResponseEntity<CourseRecord> createCourse(
            @Valid @RequestBody CourseRecord courseRecord) {

        CourseRecord created = courseServiceJpa.create(courseRecord);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseRecord> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRecord courseRecord) {

        CourseRecord updated = courseServiceJpa.update(id, courseRecord);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseServiceJpa.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/most-expensive")
    public ResponseEntity<CourseRecord> findMostExpensiveCourse() {
        CourseRecord mostExpensive =                courseServiceJpa.findMostExpensive();
        return ResponseEntity.ok(mostExpensive);
    }

    @GetMapping("/difficulty/{difficulty}")
    public List<CourseRecord> findByDifficulty(            @PathVariable Difficulty difficulty) {
        return courseServiceJpa.findByDifficulty(difficulty);
    }

    @GetMapping("/difficulty/{difficulty}/price/{price}")
    public List<CourseRecord> findByDifficultyAndPriceLessThan(@PathVariable Difficulty difficulty, @PathVariable Double price) {
        return courseServiceJpa.findByDifficultyAndPriceLessThan(difficulty, price);
    }
}
