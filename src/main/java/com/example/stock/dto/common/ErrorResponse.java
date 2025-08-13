package com.example.stock.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Standard error response structure.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    /**
     * Error details
     */
    private ErrorDetails error;
    
    /**
     * Always false for error responses
     */
    private boolean success = false;
    
    /**
     * Create an error response
     */
    public static ErrorResponse of(String message, String code, Map<String, Object> details) {
        ErrorDetails errorDetails = new ErrorDetails(message, code, details);
        return new ErrorResponse(errorDetails, false);
    }
    
    /**
     * Create an error response without details
     */
    public static ErrorResponse of(String message, String code) {
        return of(message, code, null);
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorDetails {
        private String message;
        private String code;
        private Map<String, Object> details;
    }
}