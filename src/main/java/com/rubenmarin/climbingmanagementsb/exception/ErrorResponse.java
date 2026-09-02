package com.rubenmarin.climbingmanagementsb.exception;

public record ErrorResponse(

        String timestamp,
        int status,
        String message
) {
}
