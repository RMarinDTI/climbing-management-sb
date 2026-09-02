package com.rubenmarin.climbingmanagementsb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.DateTimeException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        Map<String, String> errorsMap = fieldErrors.stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        fieldErrors.forEach(fieldError -> System.out.println("CUIDAAAAO!: " + fieldError.getField() + ": " + fieldError.getDefaultMessage()));

        ValidationErrorResponse response = new ValidationErrorResponse(new Date().toString(), HttpStatus.BAD_REQUEST.value(), "Validation failed", errorsMap);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleStatusException(ResponseStatusException ex) {

        ErrorResponse response = new ErrorResponse(new Date().toString(), ex.getStatusCode().value(), ex.getReason());

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

}
