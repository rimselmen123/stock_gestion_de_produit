package com.example.stock.dto.inventorystock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for inventory stock read responses.
 * Contains complete inventory stock information including embedded inventory item details.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStockResponseDTO {

    private String id;

    @JsonProperty("inventory_item_id")
    private String inventoryItemId;

    @JsonProperty("branch_id")
    private String branchId;

    private BigDecimal quantity;

    @JsonProperty("unit_purchase_price")
    private BigDecimal unitPurchasePrice;

    @JsonProperty("expiration_date")
    private LocalDate expirationDate;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    /**
     * Embedded inventory item information for convenience.
     * Contains minimal item details to avoid deep nesting.
     */
    @JsonProperty("inventory_item")
    private InventoryItemEmbeddedDTO inventoryItem;

    /**
     * Nested DTO for embedded inventory item information in stock responses.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InventoryItemEmbeddedDTO {
        private String id;
        private String name;
        
        @JsonProperty("threshold_quantity")
        private Integer thresholdQuantity;
    }
}
