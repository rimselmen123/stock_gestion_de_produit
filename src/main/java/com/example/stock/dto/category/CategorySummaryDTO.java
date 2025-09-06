package com.example.stock.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for InventoryItemCategory summary information.
 * Contains minimal category data for listings and dropdowns.
 * Optimized for performance when full details are not needed.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategorySummaryDTO {

    /**
     * The unique identifier of the category.
     * UUID format string.
     */
    private String id;

    /**
     * The name of the category.
     */
    private String name;

    /**
     * The department ID this category belongs to.
     */
    @JsonProperty("department_id")
    private String departmentId;
}
