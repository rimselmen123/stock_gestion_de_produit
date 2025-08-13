package com.example.stock.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Standard API response wrapper for paginated responses.
 * 
 * @param <T> The type of data being returned in the list
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    
    /**
     * The list of data items for current page
     */
    private List<T> data;
    
    /**
     * Pagination metadata
     */
    private PaginationInfo pagination;
    
    /**
     * Create a paginated response
     */
    public static <T> PaginatedResponse<T> of(List<T> data, PaginationInfo pagination) {
        return new PaginatedResponse<>(data, pagination);
    }
}