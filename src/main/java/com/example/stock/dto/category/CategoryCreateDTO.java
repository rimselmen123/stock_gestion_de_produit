package com.example.stock.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new InventoryItemCategory.
 * Contains only the fields required for category creation.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryCreateDTO {

    /**
     * The name of the category.
     * Must not be blank and cannot exceed 255 characters.
     */
    @NotBlank(message = "Category name is required")
    @Size(max = 255, message = "Category name must not exceed 255 characters")
    private String name;

    /**
     * The branch this category belongs to.
     */
    @JsonProperty("branch_id")
    @NotBlank(message = "branch_id is required")
    private String branchId;

    /**
     * The department this category belongs to.
     */
    @JsonProperty("department_id")
    @NotBlank(message = "department_id is required")
    private String departmentId;
}