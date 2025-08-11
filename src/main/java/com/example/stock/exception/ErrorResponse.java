package com.example.stock.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response structure for the Stock Management System.
 * Provides consistent error information across all API endpoints.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Timestamp when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * HTTP status code.
     */
    private Integer status;

    /**
     * Error type/category.
     */
    private String error;

    /**
     * Human-readable error message.
     */
    private String message;

    /**
     * API path where the error occurred.
     */
    private String path;

    /**
     * Validation errors (field-level errors).
     * Only included for validation failures.
     */
    private Map<String, String> validationErrors;
}
