package com.rubenmarin.climbingmanagementsb.repository;

import com.rubenmarin.climbingmanagementsb.record.CourseRecord;

import java.util.List;
import java.util.Optional;

public interface CourseRepositoryDto {

    List<CourseRecord> findAll();

    Optional<CourseRecord> findById(Long id);

    CourseRecord save(CourseRecord course);

    Optional<CourseRecord> update(Long Id, CourseRecord course);

    Optional<CourseRecord> delete(Long Id);

}
