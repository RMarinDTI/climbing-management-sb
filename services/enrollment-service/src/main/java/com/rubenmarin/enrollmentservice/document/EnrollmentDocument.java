package com.rubenmarin.enrollmentservice.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "enrollments")
public class EnrollmentDocument {

    @Id
    private String id;

    private Long  courseId;

    private String studentName;

    public EnrollmentDocument() {
    }

    public EnrollmentDocument(Long  courseId, String studentName) {
        this.courseId = courseId;
        this.studentName = studentName;
    }

    public String getId() {
        return id;
    }

    public Long  getCourseId() {
        return courseId;
    }

    public String getStudentName() {
        return studentName;
    }
}