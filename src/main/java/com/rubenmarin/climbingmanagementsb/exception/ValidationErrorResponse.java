package com.rubenmarin.climbingmanagementsb.exception;

import java.util.Map;

public record ValidationErrorResponse(
        String timestamp,
        int status,
        String message,
        Map<String, String> errors
) {
}
