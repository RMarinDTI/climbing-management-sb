package com.rubenmarin.climbingmanagementsb.test;

import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.record.Course;
import com.rubenmarin.climbingmanagementsb.repository.CourseRepository;
import com.rubenmarin.climbingmanagementsb.service.CourseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    //1. Mock
    @Mock
    CourseRepository courseRepository;
    @InjectMocks
    CourseService courseService;

    @Test
    void shouldFindCourseById() {
        Course course = new Course(1L, "Sport Climbing", 120.0, Difficulty.EASY);
        //2. Stubbing
        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        //3. Assertion
        Assertions.assertEquals(course, courseService.findById(1L));
    }

    @Test
    void shouldReturnNullWhenCourseDoesNotExist() {
        //2. Stubbing
        Mockito.when(courseRepository.findById(99L)).thenReturn(Optional.empty());
        //3. Assertions
        ResponseStatusException ex = Assertions.assertThrows(ResponseStatusException.class, () -> courseService.findById(99L));
        Assertions.assertEquals("Course not found", ex.getReason());
    }

    @Test
    void shouldCreateCourse() {
        Course course = new Course(null, "Trad Climbing", 150.0, Difficulty.MEDIUM);
        Course savedCourse = new Course(4L, "Trad Climbing", 150.0, Difficulty.MEDIUM);

        //2. Stubbing
        Mockito.when(courseRepository.save(course)).thenReturn(savedCourse);

        //3. Assertions
        Assertions.assertEquals(savedCourse, courseService.create(course));

        //4. Verifications
        Mockito.verify(courseRepository).save(course);
    }

    @Test
    void shouldUpdateCourse() {
        Course course = new Course(null, "Multi Pitch", 160.0, Difficulty.HARD);
        Course updatedCourse = new Course(2L, "Multi Pitch Updated", 160.0, Difficulty.HARD);

        //2. Stubbing
        Mockito.when(courseRepository.update(2L, course)).thenReturn(Optional.of(updatedCourse));

        //3. Assertions
        Assertions.assertEquals(updatedCourse, courseService.update(2L, course));

        //4. Verifications
        Mockito.verify(courseRepository).update(2L, course);
    }

    @Test
    void shouldThrowNotFoundWhenUpdatingNonExistingCourse() {
        Course course = new Course(null, "Sport Climbing", 120.0, Difficulty.EASY);

        //2. Stubbing
        Mockito.when(courseRepository.update(99L, course)).thenReturn(Optional.empty());

        //3. Assertion
        ResponseStatusException ex = Assertions.assertThrows(ResponseStatusException.class, () -> courseService.update(99L, course));
        Assertions.assertEquals("Course not found", ex.getReason());

        //4. Verifications
        Mockito.verify(courseRepository).update(99L, course);
    }

    @Test
    void shouldDeleteCourse() {
        Course course = new Course(2L, "Sport Climbing", 120.0, Difficulty.EASY);

        //2. Stubbing
        Mockito.when(courseRepository.delete(2L)).thenReturn(Optional.of(course));

        //3. Assertions
        Assertions.assertEquals(course, courseService.delete(2L));

        //4. Verifications
        Mockito.verify(courseRepository).delete(2L);

    }

    @Test
    void shouldThrowNotFoundWhenDeletingNonExistingCourse() {
        Course course = new Course(2L, "Sport Climbing", 120.0, Difficulty.EASY);

        //2. Stubbing
        Mockito.when(courseRepository.delete(99L)).thenReturn(Optional.empty());

        //3. Assertions
        ResponseStatusException ex = Assertions.assertThrows(ResponseStatusException.class, () -> courseService.delete(99L));
        Assertions.assertEquals("Course not found", ex.getReason());

        //4. Verifications
        Mockito.verify(courseRepository).delete(99L);

    }

    @Test
    void shouldFindAllCourses() {
        List<Course> courses = List.of(
                new Course(1L, "Sport Climbing", 120.0, Difficulty.EASY),
                new Course(2L, "Multi Pitch", 130.0, Difficulty.MEDIUM),
                new Course(3L, "Alpine Climbing", 140.0, Difficulty.HARD));

        //2. Stubbing
        Mockito.when(courseRepository.findAll()).thenReturn(courses);

        //3. Assertions
        Assertions.assertEquals(courses, courseRepository.findAll());

        //4. Verifications
        Mockito.verify(courseRepository).findAll();
    }

    @Test
    void findMostExpensive() {
        List<Course> courses = List.of(
                new Course(1L, "Sport Climbing", 120.0, Difficulty.EASY),
                new Course(2L, "Multi Pitch", 130.0, Difficulty.MEDIUM),
                new Course(3L, "Alpine Climbing", 140.0, Difficulty.HARD));

        Course mostExpensive = new Course(3L, "Alpine Climbing", 140.0, Difficulty.HARD);

        //2. Stubbing
        //La clase que estamos testeando no se mockea. !!!
        // Mockito.when(courseService.findMostExpensive()).thenReturn(mostExpensive);
        Mockito.when(courseRepository.findAll()).thenReturn(courses);

        //3. Assertions
        Assertions.assertEquals(mostExpensive, courseService.findMostExpensive());

        //4. Verifications
        Mockito.verify(courseRepository).findAll();
    }

    @Test
    void findMostExpensiveShouldThrowNotFoundWhenThereAreNoCourses() {
        //2. Stubbing
        Mockito.when(courseRepository.findAll()).thenReturn(List.of());

        //3. Assertions
        ResponseStatusException ex = Assertions.assertThrows(ResponseStatusException.class, () -> courseService.findMostExpensive());
        Assertions.assertEquals("Course not found", ex.getReason());

        //4. Verifications
        Mockito.verify(courseRepository).findAll();
    }

    @Test
    void shouldFindCoursesByDifficulty() {

        List<Course> courses = List.of(
                new Course(1L, "Sport Climbing", 120.0, Difficulty.EASY),
                new Course(2L, "Multi Pitch", 130.0, Difficulty.MEDIUM),
                new Course(3L, "Alpine Climbing", 140.0, Difficulty.HARD));

        Course expectedCourse = new Course(1L, "Sport Climbing", 120.0, Difficulty.EASY);

        //2. Stubbing
        Mockito.when(courseRepository.findAll()).thenReturn(courses);

        //3. Assertions
        List<Course> result = courseService.findByDifficulty(Difficulty.EASY);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(expectedCourse, result.getFirst());

        //4. Verifications
        Mockito.verify(courseRepository).findAll();
    }
}

//()


