package com.example.stock.dto.category;

import lombok.AllArgsConstructor;
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
     * The branch ID this category belongs to.
     */
    private String branchId;
}
