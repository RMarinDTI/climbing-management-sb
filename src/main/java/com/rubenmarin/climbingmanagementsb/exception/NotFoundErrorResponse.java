package com.rubenmarin.climbingmanagementsb.exception;

import java.util.Map;

public record NotFoundErrorResponse(
        String timestamp,
        int status,
        String message
) {
}
