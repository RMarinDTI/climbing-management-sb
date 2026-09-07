package com.rubenmarin.climbingmanagementsb.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "enrollments")
public class EnrollmentMongoDocument {

    @Id
    private String id;

    private String courseId;

    private String studentName;

    protected EnrollmentMongoDocument() {
    }

    public EnrollmentMongoDocument(String courseId, String studentName) {
        this.courseId = courseId;
        this.studentName = studentName;
    }

    public String getId() {
        return id;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getStudentName() {
        return studentName;
    }
}