package com.example.stock.exception;

import com.example.stock.dto.common.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global exception handler for the Stock Management System.
 * Handles all exceptions and provides consistent error responses.
 * 
 * @author Generated
 * @since 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles validation errors from @Valid annotations.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        log.warn("Validation error: {}", ex.getMessage());
        
        Map<String, List<String>> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });

        // Convert to Map<String, Object> expected by ErrorResponse
        Map<String, Object> details = new HashMap<>();
        fieldErrors.forEach(details::put);

        ErrorResponse errorResponse = ErrorResponse.of(
            "Validation failed", 
            "VALIDATION_ERROR", 
            details
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    /**
     * Handles resource not found exceptions.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        log.warn("Resource not found: {}", ex.getMessage());
        
        String errorCode = ex.getResourceType().toUpperCase() + "_NOT_FOUND";
        ErrorResponse errorResponse = ErrorResponse.of(ex.getMessage(), errorCode);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handles delete constraint exceptions.
     */
    @ExceptionHandler(DeleteConstraintException.class)
    public ResponseEntity<ErrorResponse> handleDeleteConstraintException(
            DeleteConstraintException ex, WebRequest request) {
        
        log.warn("Delete constraint violation: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(ex.getMessage(), "DELETE_CONSTRAINT");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles foreign key constraint exceptions.
     */
    @ExceptionHandler(ForeignKeyConstraintException.class)
    public ResponseEntity<ErrorResponse> handleForeignKeyConstraintException(
            ForeignKeyConstraintException ex, WebRequest request) {
        
        log.warn("Foreign key constraint violation: {}", ex.getMessage());
        
        Map<String, Object> details = new HashMap<>();
        List<String> errors = new ArrayList<>();
        errors.add("Selected " + ex.getFieldName().replace("_id", "").replace("_", " ") + " does not exist");
        details.put(ex.getFieldName(), errors);
        
        ErrorResponse errorResponse = ErrorResponse.of(
            "Validation failed", 
            "VALIDATION_ERROR", 
            details
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    /**
     * Handles IllegalArgumentException (business logic errors).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        log.warn("Business logic error: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(ex.getMessage(), "BAD_REQUEST");

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles all other exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, WebRequest request) {
        
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.of(
            "An unexpected error occurred", 
            "INTERNAL_SERVER_ERROR"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}