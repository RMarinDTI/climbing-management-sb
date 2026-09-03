package com.rubenmarin.climbingmanagementsb.service;

import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.entity.CourseEntity;
import com.rubenmarin.climbingmanagementsb.record.CourseRecord;
import com.rubenmarin.climbingmanagementsb.repository.CourseRepositoryDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
public class CourseServiceDto {
   // private final CourseRepository courseRepository;

    private final CourseRepositoryDto courseRepository;


    private CourseRecord toRecord(CourseEntity entity) {
        return new CourseRecord(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getDifficulty()
        );
    }

    private CourseEntity toEntity(CourseRecord record) {
        return new CourseEntity(
                record.name(),
                record.price(),
                record.difficulty()
        );
    }



    public CourseServiceDto(CourseRepositoryDto courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseRecord> findAll() {
        return courseRepository.findAll();
    }

    public CourseRecord findById(Long id) {
        return courseRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));
    }

    public CourseRecord create(@Valid @RequestBody CourseRecord course) {
        return courseRepository.save(course);
    }

    public CourseRecord update(Long id , @Valid @RequestBody CourseRecord course) {
        return courseRepository.update(id, course).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));
    }

    public CourseRecord delete(Long id ) {
        return courseRepository.delete(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));
    }

    public CourseRecord findMostExpensive() {
        List<CourseRecord> courses = courseRepository.findAll();
        CourseRecord mostExpensive = courses.stream().max(Comparator.comparing(CourseRecord:: price)).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));
        return mostExpensive;
    }

    public  List<CourseRecord> findByDifficulty(Difficulty difficulty) {
        List<CourseRecord> coursesByDif = courseRepository.findAll().stream().filter(course -> course.difficulty().equals(difficulty)).toList();
        return coursesByDif;
    }

}
