package com.example.stock.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating an existing InventoryItemCategory.
 * Contains only the fields that can be updated.
 * Note: branchId cannot be changed after creation.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDTO {

    /**
     * The updated name of the category.
     * Must be unique per branch and between 2-100 characters.
     */
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String name;

    /**
     * Updated description of the category.
     * Maximum 500 characters.
     */
    @Size(max = 500, message = "Category description cannot exceed 500 characters")
    private String description;
}
