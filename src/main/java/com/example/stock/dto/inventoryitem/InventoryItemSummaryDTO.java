package com.example.stock.dto.inventoryitem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for InventoryItem summary information.
 * Contains minimal inventory item data for listings and dropdowns.
 * Optimized for performance when full details are not needed.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemSummaryDTO {

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
     * The unit purchase price of the item.
     */
    private BigDecimal unitPurchasePrice;

    /**
     * The threshold quantity for low stock alerts.
     */
    private Integer thresholdQuantity;

    /**
     * The category name this item belongs to.
     */
    private String categoryName;

    /**
     * The unit symbol for this item.
     */
    private String unitSymbol;



    /**
     * Indicates if the item is near threshold (low stock).
     */
    private Boolean isLowStock;
}
