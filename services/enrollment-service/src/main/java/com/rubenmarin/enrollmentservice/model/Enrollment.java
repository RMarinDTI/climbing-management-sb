package com.rubenmarin.enrollmentservice.model;

public record Enrollment(
        Long id,
        Long courseId,
        String studentName
) {
}
