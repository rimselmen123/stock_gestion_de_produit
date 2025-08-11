package com.example.stock.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for InventoryItemCategory response data.
 * Contains all category information for detailed responses.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {

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
     * The description of the category.
     */
    private String description;

    /**
     * The branch ID this category belongs to.
     */
    private String branchId;

    /**
     * Timestamp when the category was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the category was last updated.
     */
    private LocalDateTime updatedAt;
}
