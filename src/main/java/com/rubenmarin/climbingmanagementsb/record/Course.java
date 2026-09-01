package com.rubenmarin.climbingmanagementsb.record;

import com.rubenmarin.climbingmanagementsb.Difficulty;

public record Course(
        Long id,
        String name,
        Double price,
        Difficulty difficulty
) {
}