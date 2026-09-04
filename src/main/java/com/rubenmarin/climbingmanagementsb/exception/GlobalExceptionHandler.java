package com.rubenmarin.climbingmanagementsb.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /*
     * GLOBAL EXCEPTION HANDLER
     *
     * @RestControllerAdvice allows us to handle exceptions thrown
     * by any controller in one centralized place.
     *
     * This keeps exception-handling logic out of our controllers.
     */


    /*
     * VALIDATION EXCEPTIONS
     *
     * This exception is thrown when @Valid fails on a @RequestBody.
     *
     * Example: POST /jpa/courses with invalid data.
     *
     * Instead of returning Spring's default error response, we create our own consistent API response.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );
        }

        ValidationErrorResponse response = new ValidationErrorResponse(
                Instant.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    /*
     * COURSE NOT FOUND
     *
     * Handles CourseNotFoundException thrown by the service layer.
     *
     * The service is responsible for detecting the business condition (the course does not exist),
     * while the exception handler is responsible for translating it into the appropriate HTTP response.
     *
     * In this case:
     *
     * CourseNotFoundException → HTTP 404 NOT_FOUND
     */
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCourseNotFoundException(CourseNotFoundException ex) {

        ErrorResponse response = new ErrorResponse(
                Instant.now().toString(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    /*
     * UNEXPECTED EXCEPTIONS
     *
     * This is the fallback handler for unexpected exceptions that are not handled by a more specific @ExceptionHandler.
     * We return a generic 500 INTERNAL_SERVER_ERROR response instead of exposing internal implementation details to the client.
     * The real exception should be logged internally for debugging, but sensitive technical details should not be returned in the API.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        log.error("Unexpected error", ex);
        ErrorResponse response = new ErrorResponse(
                Instant.now().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ExceptionMsg.UNEXPECTED_ERROR
        );


        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}

