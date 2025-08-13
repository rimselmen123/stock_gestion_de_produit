package com.example.stock.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API response wrapper for single entity responses.
 * 
 * @param <T> The type of data being returned
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    /**
     * The response data
     */
    private T data;
    
    /**
     * Success message
     */
    private String message;
    
    /**
     * Indicates if the operation was successful
     */
    private boolean success;
    
    /**
     * Create a successful response with data and message
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message, true);
    }
    
    /**
     * Create a successful response with data only
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, null, true);
    }
    
    /**
     * Create a successful response with message only
     */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(null, message, true);
    }
}