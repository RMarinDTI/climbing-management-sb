package com.rubenmarin.climbingmanagementsb.dto;

public class EnrollmentDto {

    private String studentName;

    public EnrollmentDto(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }
}
