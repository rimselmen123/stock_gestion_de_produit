package com.example.stock.dto.inventoryitem;

import com.example.stock.dto.brand.BrandSummaryDTO;
import com.example.stock.dto.category.CategorySummaryDTO;
import com.example.stock.dto.unit.UnitSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
     * The threshold quantity for low stock alerts.
     */
    private Integer thresholdQuantity;

    /**
     * The quantity to reorder when stock is low.
     */
    private Integer reorderQuantity;

    /**
     * The unit purchase price of the item.
     */
    private BigDecimal unitPurchasePrice;

    /**
     * Timestamp when the inventory item was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the inventory item was last updated.
     */
    private LocalDateTime updatedAt;

    /**
     * The category this item belongs to.
     */
    private CategorySummaryDTO category;

    /**
     * The unit for this item.
     */
    private UnitSummaryDTO unit;

    /**
     * The brand for this item (optional).
     */
    private BrandSummaryDTO brand;

    /**
     * Indicates if the item is near threshold (low stock).
     * Calculated field for convenience.
     */
    private Boolean isLowStock;
}
