package com.rubenmarin.enrollmentservice.model;

public record Enrollment(
        String  id,
        Long  courseId,
        String studentName
) {
}
