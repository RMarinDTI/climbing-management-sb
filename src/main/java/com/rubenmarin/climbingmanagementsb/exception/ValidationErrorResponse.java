package com.rubenmarin.climbingmanagementsb.exception;

import java.util.Map;




public class ValidationErrorResponse {

    private String timestamp;
    private int status;
    private String message;
    private Map<String, String> errors;

    public ValidationErrorResponse(String timestamp,int status, String message, Map<String, String> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
