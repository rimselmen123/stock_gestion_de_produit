package com.example.stock.dto.inventoryitem;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * The threshold quantity for low stock alerts.
     */
    @JsonProperty("threshold_quantity")
    private Integer thresholdQuantity;

    /**
     * The category name this item belongs to.
     */
    @JsonProperty("category_name")
    private String categoryName;

    /**
     * The unit symbol for this item.
     */
    @JsonProperty("unit_symbol")
    private String unitSymbol;
}
