package com.rubenmarin.climbingmanagementsb.repository;

import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.record.Course;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class InMemoryCourseRepository implements CourseRepository {

    @Override
    public List<Course> findAll() {
        return List.of(
                new Course(1L, "Sport Climbing", 120.0, Difficulty.EASY),
                new Course(2L, "Multi Pitch", 130.0, Difficulty.MEDIUM),
                new Course(3L, "Alpine Climbing", 140.0, Difficulty.HARD)
        );
    }

    @Override
    public Optional<Course> findById(Long id) {
        Stream<Course> st = this.findAll().stream().filter(course -> course.id().equals(id));
        Optional<Course> result = st.findAny();
        return result;
    }

}
