package com.rubenmarin.climbingmanagementsb.service;

import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.entity.CourseEntity;
import com.rubenmarin.climbingmanagementsb.record.CourseRecord;
import com.rubenmarin.climbingmanagementsb.repository.CourseRepositoryJpa;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
public class CourseServiceJpa {


    private final CourseRepositoryJpa courseRepositoryJpa;

    public CourseServiceJpa(CourseRepositoryJpa courseRepositoryJpa) {
        this.courseRepositoryJpa = courseRepositoryJpa;
    }


    // Entity → Record
    private CourseRecord toRecord(CourseEntity entity) {
        return new CourseRecord(entity.getId(), entity.getName(), entity.getPrice(), entity.getDifficulty());
    }

    // Record → Entity
    private CourseEntity toEntity(CourseRecord record) {
        return new CourseEntity(record.name(), record.price(), record.difficulty());
    }

    public List<CourseRecord> findAll() {

        //ANTES
//        return courseRepositoryJpa.findAll().stream()
//                .map(entity ->
//                        new CourseRecord(
//                                entity.getId(),
//                                entity.getName(),
//                                entity.getPrice(),
//                                entity.getDifficulty()
//                        )
//                ).toList();
        //AHORA
        return courseRepositoryJpa.findAll().stream().map(this::toRecord).toList();
    }

    public CourseRecord findById(Long id) {
        return courseRepositoryJpa.findById(id)
                .map(this::toRecord)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course not found"
                        )
                );
    }

    public CourseRecord create(CourseRecord courseRecord) {
        CourseEntity entity = toEntity(courseRecord);
        CourseEntity saved = courseRepositoryJpa.save(entity);
        return toRecord(saved);
    }

    public CourseRecord update(Long id, CourseRecord courseRecord) {

        CourseEntity existing = courseRepositoryJpa.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course not found"
                        )
                );

        existing.setName(courseRecord.name());
        existing.setPrice(courseRecord.price());
        existing.setDifficulty(courseRecord.difficulty());

        CourseEntity updated = courseRepositoryJpa.save(existing);

        return toRecord(updated);
    }

    public CourseRecord delete(Long id) {

        CourseEntity existing = courseRepositoryJpa.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course not found"
                        )
                );

        courseRepositoryJpa.delete(existing);

        return toRecord(existing);
    }

    public CourseRecord findMostExpensive() {

//        CourseEntity mostExpensive = courseRepositoryJpa.findAll()
//                .stream()
//                .max(Comparator.comparing(CourseEntity::getPrice))
//                .orElseThrow(() ->
//                        new ResponseStatusException(
//                                HttpStatus.NOT_FOUND,
//                                "Course not found"
//                        )
//                );


// Repository → Service → probar que Hibernate genera una consulta ORDER BY price DESC LIMIT 1.

        CourseEntity mostExpensive = courseRepositoryJpa.findTopByOrderByPriceDesc()
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course not found"
                        )
                );

        return toRecord(mostExpensive);
    }

    public List<CourseRecord> findByDifficulty(Difficulty difficulty) {
        return courseRepositoryJpa.findByDifficulty(difficulty)
                .stream()
                .map(this::toRecord)
                .toList();
    }

    public List<CourseRecord> findByDifficultyAndPriceLessThan(Difficulty difficulty, Double price) {
        return courseRepositoryJpa
                .searchCourses(difficulty, price)
                //.findByDifficultyAndPriceLessThan(difficulty, price)
                .stream()
                .map(this::toRecord)
                .toList();
    }
}


