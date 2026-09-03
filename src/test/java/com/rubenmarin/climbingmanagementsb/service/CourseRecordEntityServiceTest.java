
package com.rubenmarin.climbingmanagementsb.service;

import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.record.CourseRecord;
import com.rubenmarin.climbingmanagementsb.repository.CourseRepositoryDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CourseRecordEntityServiceTest {
    //1. Mock
    @Mock
    CourseRepositoryDto courseRepository;
    @InjectMocks
    CourseServiceDto courseService;

    @Test
    void shouldFindCourseById() {
        CourseRecord course = new CourseRecord(1L, "Sport Climbing", 120.0, Difficulty.EASY);
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
        CourseRecord course = new CourseRecord(null, "Trad Climbing", 150.0, Difficulty.MEDIUM);
        CourseRecord savedCourse = new CourseRecord(4L, "Trad Climbing", 150.0, Difficulty.MEDIUM);

        //2. Stubbing
        Mockito.when(courseRepository.save(course)).thenReturn(savedCourse);

        //3. Assertions
        Assertions.assertEquals(savedCourse, courseService.create(course));

        //4. Verifications
        Mockito.verify(courseRepository).save(course);
    }

    @Test
    void shouldUpdateCourse() {
        CourseRecord course = new CourseRecord(null, "Multi Pitch", 160.0, Difficulty.HARD);
        CourseRecord updatedCourse = new CourseRecord(2L, "Multi Pitch Updated", 160.0, Difficulty.HARD);

        //2. Stubbing
        Mockito.when(courseRepository.update(2L, course)).thenReturn(Optional.of(updatedCourse));

        //3. Assertions
        Assertions.assertEquals(updatedCourse, courseService.update(2L, course));

        //4. Verifications
        Mockito.verify(courseRepository).update(2L, course);
    }

    @Test
    void shouldThrowNotFoundWhenUpdatingNonExistingCourse() {
        CourseRecord course = new CourseRecord(null, "Sport Climbing", 120.0, Difficulty.EASY);

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
        CourseRecord course = new CourseRecord(2L, "Sport Climbing", 120.0, Difficulty.EASY);

        //2. Stubbing
        Mockito.when(courseRepository.delete(2L)).thenReturn(Optional.of(course));

        //3. Assertions
        Assertions.assertEquals(course, courseService.delete(2L));

        //4. Verifications
        Mockito.verify(courseRepository).delete(2L);

    }

    @Test
    void shouldThrowNotFoundWhenDeletingNonExistingCourse() {
        CourseRecord course = new CourseRecord(2L, "Sport Climbing", 120.0, Difficulty.EASY);

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
        List<CourseRecord> courses = List.of(
                new CourseRecord(1L, "Sport Climbing", 120.0, Difficulty.EASY),
                new CourseRecord(2L, "Multi Pitch", 130.0, Difficulty.MEDIUM),
                new CourseRecord(3L, "Alpine Climbing", 140.0, Difficulty.HARD));

        //2. Stubbing
        Mockito.when(courseRepository.findAll()).thenReturn(courses);

        //3. Assertions
        Assertions.assertEquals(courses, courseRepository.findAll());

        //4. Verifications
        Mockito.verify(courseRepository).findAll();
    }

    @Test
    void findMostExpensive() {
        List<CourseRecord> courses = List.of(
                new CourseRecord(1L, "Sport Climbing", 120.0, Difficulty.EASY),
                new CourseRecord(2L, "Multi Pitch", 130.0, Difficulty.MEDIUM),
                new CourseRecord(3L, "Alpine Climbing", 140.0, Difficulty.HARD));

        CourseRecord mostExpensive = new CourseRecord(3L, "Alpine Climbing", 140.0, Difficulty.HARD);

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

        List<CourseRecord> courses = List.of(
                new CourseRecord(1L, "Sport Climbing", 120.0, Difficulty.EASY),
                new CourseRecord(2L, "Multi Pitch", 130.0, Difficulty.MEDIUM),
                new CourseRecord(3L, "Alpine Climbing", 140.0, Difficulty.HARD));

        CourseRecord expectedCourse = new CourseRecord(1L, "Sport Climbing", 120.0, Difficulty.EASY);

        //2. Stubbing
        Mockito.when(courseRepository.findAll()).thenReturn(courses);

        //3. Assertions
        List<CourseRecord> result = courseService.findByDifficulty(Difficulty.EASY);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(expectedCourse, result.getFirst());

        //4. Verifications
        Mockito.verify(courseRepository).findAll();
    }
}

//()


