package com.rubenmarin.climbingmanagementsb.controller;

import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.document.CourseMongoDocument;
import com.rubenmarin.climbingmanagementsb.dto.CourseMongoResponseDto;
import com.rubenmarin.climbingmanagementsb.service.CourseMongoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // GET http://localhost:8080/mongo/courses
    @GetMapping
    public Page<CourseMongoResponseDto> getCourses(Pageable pageable) {
        return courseMongoService.findAll(pageable);
    }

    // GET http://localhost:8080/mongo/courses/6a9ada74aed9b79d82d16295
    @GetMapping("/{id}")
    public CourseMongoResponseDto getCourseById(@PathVariable String id) {
        return courseMongoService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CourseMongoResponseDto> createCourse(@RequestBody CourseMongoDocument course) {
        CourseMongoResponseDto created = courseMongoService.create(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public CourseMongoResponseDto updateCourse(@PathVariable String id, @RequestBody CourseMongoDocument course) {
        return courseMongoService.update(id, course);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        courseMongoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET http://localhost:8080/mongo/courses/difficulty/EASY
    @GetMapping("/difficulty/{difficulty}")
    public List<CourseMongoDocument> findByDifficulty(@PathVariable Difficulty difficulty) {
        return courseMongoService.findByDifficulty(difficulty);
    }

    // GET http://localhost:8080/mongo/courses/difficulty-lt-price/EASY/100
    @GetMapping("/difficulty-lt-price/{difficulty}/{price}")
    public List<CourseMongoDocument> findByDifficultyAndPriceLessThan(@PathVariable Difficulty difficulty, @PathVariable Double price) {
        return courseMongoService.findByDifficultyAndPriceLessThan(difficulty, price);
    }

    // GET http://localhost:8080/mongo/courses/difficulty-max-price-query?difficulty=EASY&price=100
    @GetMapping("/difficulty-max-price-query")
    public List<CourseMongoDocument> findCoursesByDifficultyAndMaxPriceQuery(@RequestParam Difficulty difficulty, @RequestParam Double price) {
        return courseMongoService.findCoursesByDifficultyAndMaxPriceQuery(difficulty, price);
    }

    // GET http://localhost:8080/mongo/courses/minimum-price?price=90
    @GetMapping("/minimum-price")
    public List<CourseMongoDocument> findCoursesWithMinimumPrice(@RequestParam Double price) {
        return courseMongoService.findCoursesWithMinimumPrice(price);
    }

    //GET http://localhost:8080/mongo/courses/difficultyIn?difficulties=EASY,MEDIUM
    @GetMapping("/difficultyIn")
    public List<CourseMongoDocument> findByDifficultyIn(@RequestParam List<Difficulty> difficulties) {
        return courseMongoService.findByDifficultyIn(difficulties);
    }

    // GET http://localhost:8080/mongo/courses/difficultyIn-query?difficulties=EASY,MEDIUM
    @GetMapping("/difficultyIn-query")
    public List<CourseMongoDocument> findByDifficultyInQuery(@RequestParam List<Difficulty> difficulties) {
        return courseMongoService.findByDifficultyInQuery(difficulties);
    }

    //http://localhost:8080/mongo/courses/difficultyNotIn-query?difficulties=HARD,MEDIUM
    @GetMapping("/difficultyNotIn-query")
    public List<CourseMongoDocument> findByDifficultyNotInQuery(@RequestParam List<Difficulty> difficulties) {
        return courseMongoService.findByDifficultyNotInQuery(difficulties);
    }

    // GET http://localhost:8080/mongo/courses/name-contains?name=ferr
    @GetMapping("/name-contains")
    public List<CourseMongoDocument> findByNameContainingIgnoreCase(@RequestParam String name) {
        return courseMongoService.findByNameContainingIgnoreCase(name);
    }

    //GET http://localhost:8080/mongo/courses/search?difficulty=MEDIUM&maxPrice=160&page=0&size=2&sort=price,asc
    @GetMapping("/search")
    public Page<CourseMongoDocument> searchCourses(@RequestParam Difficulty difficulty, @RequestParam Double maxPrice, Pageable pageable) {
        return courseMongoService.findByDifficultyAndPriceLessThanEqual(difficulty, maxPrice, pageable);
    }


}