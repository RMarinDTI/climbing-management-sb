package com.rubenmarin.climbingmanagementsb.repository;

import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.document.CourseMongoDocument;
import com.rubenmarin.climbingmanagementsb.dto.CourseDifficultyStatsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CourseMongoCustomRepository {

    Page<CourseMongoDocument> search(String name, Difficulty difficulty, Double minPrice, Double maxPrice, Pageable pageable);

    List<CourseDifficultyStatsDto> getDifficultyStats();
}
