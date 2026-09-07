package com.rubenmarin.climbingmanagementsb.service;

import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.document.CourseMongoDocument;
import com.rubenmarin.climbingmanagementsb.dto.CourseMongoResponseDto;
import com.rubenmarin.climbingmanagementsb.exception.CourseNotFoundException;
import com.rubenmarin.climbingmanagementsb.exception.ExceptionMsg;
import com.rubenmarin.climbingmanagementsb.repository.CourseMongoRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseMongoService {

    private final CourseMongoRepository courseMongoRepository;


    public CourseMongoService(CourseMongoRepository courseMongoRepository) {
        this.courseMongoRepository = courseMongoRepository;

    }

    private CourseMongoResponseDto toDto(CourseMongoDocument course) {
        return new CourseMongoResponseDto(course.getId(), course.getName(), course.getPrice(), course.getDifficulty());
    }

    private CourseMongoDocument findDocumentById(String id) {
        return courseMongoRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(ExceptionMsg.COURSE_NOT_FOUND));
    }

    // Spring automatically reads: ?page=0&size=10&sort=price,asc
    // GET http://localhost:8080/mongo/courses?page=0&size=2
    // Cheapest → most expensive: GET http://localhost:8080/mongo/courses?page=0&size=10&sort=price,asc
    // Most expensive → cheapest: GET http://localhost:8080/mongo/courses?page=0&size=10&sort=price,desc
    // Alphabetical by name: GET http://localhost:8080/mongo/courses?page=0&size=10&sort=name,asc
    public Page<CourseMongoResponseDto> findAll(Pageable pageable) {
        return courseMongoRepository.findAll(pageable).map(this::toDto);
    }

    public CourseMongoResponseDto findById(String id) {
        CourseMongoDocument course = findDocumentById(id);
        return toDto(course);
    }

    public CourseMongoResponseDto create(CourseMongoDocument course) {
        CourseMongoDocument saved = courseMongoRepository.save(course);
        return toDto(saved);
    }

    public CourseMongoResponseDto update(String id, CourseMongoDocument course) {
        CourseMongoDocument existing = findDocumentById(id);

        existing.setName(course.getName());
        existing.setPrice(course.getPrice());
        existing.setDifficulty(course.getDifficulty());

        CourseMongoDocument saved = courseMongoRepository.save(existing);
        return toDto(saved);

    }

    public void delete(String id) {
        courseMongoRepository.deleteById(id);
    }

    public List<CourseMongoDocument> findByDifficulty(Difficulty difficulty) {
        return courseMongoRepository.findByDifficulty(difficulty);
    }

    public List<CourseMongoDocument> findByDifficultyAndPriceLessThan(Difficulty difficulty, Double price) {
        return courseMongoRepository.findByDifficultyAndPriceLessThan(difficulty, price);
    }

    public List<CourseMongoDocument> findCoursesByDifficultyAndMaxPriceQuery(Difficulty difficulty, Double price) {
        return courseMongoRepository.findCoursesByDifficultyAndMaxPriceQuery(difficulty, price);
    }

    public List<CourseMongoDocument> findCoursesWithMinimumPrice(Double price) {
        return courseMongoRepository.findCoursesWithMinimumPriceQuery(price);
    }

    public List<CourseMongoDocument> findByDifficultyIn(List<Difficulty> difficulties) {
        return courseMongoRepository.findByDifficultyIn(difficulties);
    }

    public List<CourseMongoDocument> findByDifficultyInQuery(List<Difficulty> difficulties) {
        return courseMongoRepository.findByDifficultyInQuery(difficulties);
    }

    public List<CourseMongoDocument> findByDifficultyNotInQuery(List<Difficulty> difficulties) {
        return courseMongoRepository.findByDifficultyNotInQuery(difficulties);
    }

    public List<CourseMongoDocument> findByNameContainingIgnoreCase(String name) {
        return courseMongoRepository.findByNameContainingIgnoreCase(name);
    }

    public Page<CourseMongoDocument> findByDifficultyAndPriceLessThanEqual(Difficulty difficulty, Double price, Pageable pageable) {
        return courseMongoRepository.findByDifficultyAndPriceLessThanEqual(difficulty, price, pageable);
    }
}
