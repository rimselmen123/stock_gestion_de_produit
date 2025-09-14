package com.example.stock.dto.inventoryitem;

import com.example.stock.dto.category.CategoryResponseDTO;
import com.example.stock.dto.unit.UnitResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for InventoryItem response data.
 * Contains all inventory item information for detailed responses.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemResponseDTO {

    /**
     * The unique identifier of the inventory item.
     * UUID format string.
     */
    private String id;

    /**
     * The name of the inventory item.
     */
    private String name;

    /**
     * The branch ID this item belongs to.
     */
    @JsonProperty("branch_id")
    private String branchId;

    /**
     * The department ID this item belongs to.
     */
    @JsonProperty("department_id")
    private String departmentId;

    /**
     * The category ID this item belongs to.
     */
    @JsonProperty("inventory_item_category_id")
    private String inventoryItemCategoryId;

    /**
     * The unit ID for this item.
     */
    @JsonProperty("unit_id")
    private String unitId;

    /**
     * The threshold quantity for low stock alerts.
     */
    @JsonProperty("threshold_quantity")
    private Integer thresholdQuantity;

    /**
     * The quantity to reorder when stock is low.
     */
    @JsonProperty("reorder_quantity")
    private Integer reorderQuantity;
    /**
     * Timestamp when the inventory item was created.
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp when the inventory item was last updated.
     */
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    /**
     * The category this item belongs to.
     */
    private CategoryResponseDTO category;

    /**
     * The unit for this item.
     */
    private UnitResponseDTO unit;
}