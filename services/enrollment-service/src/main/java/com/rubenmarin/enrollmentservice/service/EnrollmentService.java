package com.rubenmarin.enrollmentservice.service;

import com.rubenmarin.enrollmentservice.model.Enrollment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EnrollmentService {

    private final List<Enrollment> enrollments = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong();

    public List<Enrollment> findAll() {
        return List.copyOf(enrollments);
    }

    public Enrollment create(Enrollment enrollment) {

        Enrollment created = new Enrollment(
                sequence.incrementAndGet(),
                enrollment.courseId(),
                enrollment.studentName()
        );

        enrollments.add(created);

        return created;
    }
}