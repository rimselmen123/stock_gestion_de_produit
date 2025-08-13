package com.example.stock.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pagination metadata for paginated responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationInfo {
    
    /**
     * Current page number (1-based)
     */
    @JsonProperty("current_page")
    private int currentPage;
    
    /**
     * Number of items per page
     */
    @JsonProperty("per_page")
    private int perPage;
    
    /**
     * Total number of items across all pages
     */
    private long total;
    
    /**
     * Last page number
     */
    @JsonProperty("last_page")
    private int lastPage;
    
    /**
     * Create pagination info from page parameters and total count
     */
    public static PaginationInfo of(int page, int perPage, long total) {
        int lastPage = (int) Math.ceil((double) total / perPage);
        return new PaginationInfo(page, perPage, total, Math.max(1, lastPage));
    }
}