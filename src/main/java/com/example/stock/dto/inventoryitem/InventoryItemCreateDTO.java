package com.example.stock.dto.inventoryitem;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new InventoryItem.
 * Contains only the fields required for inventory item creation.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder    
public class InventoryItemCreateDTO {

    /**
     * The name of the inventory item.
     * Must not be blank and cannot exceed 255 characters.
     */
    @JsonProperty("name")
    @NotBlank(message = "Inventory item name is required")
    @Size(max = 255, message = "Inventory item name must not exceed 255 characters")
    private String name;

    /**
     * The branch this item belongs to.
     */
    @JsonProperty("branch_id")
    @NotBlank(message = "branch_id is required")
    private String branchId;

    /**
     * The department this item belongs to.
     */
    @JsonProperty("department_id")
    @NotBlank(message = "department_id is required")
    private String departmentId;

    /**
     * The category ID this item belongs to.
     * Must exist in categories table.
     */
    @JsonProperty("inventory_item_category_id")
    @NotBlank(message = "Category ID is required")
    private String inventoryItemCategoryId;

    /**
     * The unit ID for this item.
     * Must exist in units table.
     */
    @JsonProperty("unit_id")
    @NotBlank(message = "Unit ID is required")
    private String unitId;

    /**
     * The threshold quantity for low stock alerts.
     * Must be a positive number.
     */
    @JsonProperty("threshold_quantity")
    @NotNull(message = "Threshold quantity is required")
    @Positive(message = "Threshold quantity must be positive")
    private Integer thresholdQuantity;

    /**
     * The quantity to reorder when stock is low.
     * Must be a positive number.
     */
    @JsonProperty("reorder_quantity")
    @NotNull(message = "Reorder quantity is required")
    @Positive(message = "Reorder quantity must be positive")
    private Integer reorderQuantity;
}