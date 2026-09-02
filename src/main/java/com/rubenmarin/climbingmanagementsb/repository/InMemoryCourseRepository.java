package com.rubenmarin.climbingmanagementsb.repository;

import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.record.Course;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Repository
public class InMemoryCourseRepository implements CourseRepository {

    private final List<Course> courses = new ArrayList<Course>();

    public InMemoryCourseRepository() {
        courses.add(new Course(1L, "Sport Climbing", 120.0, Difficulty.EASY));
        courses.add(new Course(2L, "Multi Pitch", 130.0, Difficulty.MEDIUM));
        courses.add(new Course(3L, "Alpine Climbing", 140.0, Difficulty.HARD));
    }

    @Override
    public List<Course> findAll() {
        return courses;
    }

    @Override
    public Optional<Course> findById(Long id) {
        Stream<Course> st = this.findAll().stream().filter(course -> course.id().equals(id));
        Optional<Course> result = st.findFirst();
        return result;
    }

    @Override
    public Course save(Course course) {

        Long lastId = courses.stream().max(Comparator.comparing(Course::id)).map(Course::id).orElse(0L);

        Course newCourse = new Course(lastId + 1L, course.name(), course.price(), course.difficulty());

        courses.add(newCourse);

        return newCourse;
    }

    @Override
    public Optional<Course> update(Long id, Course course) {

        Optional<Course> found = this.findById(id);

        if (found.isEmpty()) {
            return Optional.empty();
        }
            Course newCourse = new Course(id, course.name(), course.price(), course.difficulty());
            int posicion = -1;
            List<Course> allCourses = this.findAll();
            for (int i = 0; i < allCourses.size(); i++) {
                if (allCourses.get(i).id().equals(id)) {
                    posicion = i;
                    break;
                }
            }

            allCourses.set(posicion, newCourse);

        return Optional.of(newCourse);



    }

}
