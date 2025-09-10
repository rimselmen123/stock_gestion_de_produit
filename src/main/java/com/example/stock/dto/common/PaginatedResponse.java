package com.example.stock.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * Standard API response wrapper for paginated responses.
 * 
 * @param <T> The type of data being returned in the list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    
    /**
     * The list of data items for current page
     */
    private List<T> data;
    
    /**
     * Total number of elements across all pages
     */
    private long totalElements;
    
    /**
     * Total number of pages
     */
    private int totalPages;
    
    /**
     * Current page number (0-based)
     */
    private int currentPage;
    
    /**
     * Number of items per page
     */
    private int pageSize;
    
    /**
     * Whether there is a next page
     */
    private boolean hasNext;
    
    /**
     * Whether there is a previous page
     */
    private boolean hasPrevious;
    
    /**
     * Pagination metadata (for backward compatibility)
     */
    private PaginationInfo pagination;
    
    /**
     * Create a paginated response
     */
    public static <T> PaginatedResponse<T> of(List<T> data, PaginationInfo pagination) {
        return new PaginatedResponse<>(data, 0, 0, 0, 0, false, false, pagination);
    }
}