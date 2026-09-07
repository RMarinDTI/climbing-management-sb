package com.rubenmarin.climbingmanagementsb.repository;

import com.rubenmarin.climbingmanagementsb.record.CourseRecord;

import java.util.List;
import java.util.Optional;

public interface CourseRepositoryDto {
    // DTO = Data Transfer Object
    //
    // A DTO is an object used to transfer data between layers,
    // especially between the API (Controller) and the client.
    //
    // DTOs allow us to control which fields are exposed or accepted.
    //
    // Benefits of DTOs:
    //
    // - Avoid exposing internal fields
    // - Control the API contract
    // - Separate API models from database models
    // - Validate incoming data
    // - Avoid exposing sensitive information
    // - Make API changes easier without changing the database model

    List<CourseRecord> findAll();

    Optional<CourseRecord> findById(Long id);

    CourseRecord save(CourseRecord course);

    Optional<CourseRecord> update(Long Id, CourseRecord course);

    Optional<CourseRecord> delete(Long Id);

}
