package com.example.stock.dto.branch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Branch filtering parameters.
 * Encapsulates all search and filter criteria.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchFilterDTO {
    
    /**
     * Global search term (searches in name, description, code)
     */
    private String search;
    
    /**
     * Filter by specific name
     */
    private String name;
    
    /**
     * Filter by specific description
     */
    private String description;
    
    /**
     * Filter by specific code
     */
    private String code;
    
    /**
     * Filter by active status (true = active, false = inactive, null = all)
     */
    private Boolean isActive;
    
    /**
     * Filter by creation date range - start
     */
    private LocalDateTime createdAfter;
    
    /**
     * Filter by creation date range - end
     */
    private LocalDateTime createdBefore;
    
    /**
     * Page number (0-based)
     */
    @Builder.Default
    private int page = 0;
    
    /**
     * Items per page
     */
    @Builder.Default
    private int perPage = 20;
    
    /**
     * Field to sort by
     */
    @Builder.Default
    private String sortField = "name";
    
    /**
     * Sort direction (asc/desc)
     */
    @Builder.Default
    private String sortDirection = "asc";
}
