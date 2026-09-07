package com.rubenmarin.climbingmanagementsb.dto;

public class CourseDifficultyStatsDto {

    private String difficulty;
    private Double averagePrice;
    private Long courseCount;

    public CourseDifficultyStatsDto(
            String difficulty,
            Double averagePrice,
            Long courseCount
    ) {
        this.difficulty = difficulty;
        this.averagePrice = averagePrice;
        this.courseCount = courseCount;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }

    public Long getCourseCount() {
        return courseCount;
    }
}
