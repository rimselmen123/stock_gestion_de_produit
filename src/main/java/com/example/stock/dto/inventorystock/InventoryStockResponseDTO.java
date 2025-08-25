package com.example.stock.dto.inventorystock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Complete DTO for inventory stock responses, aligned with frontend contract.
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

    // hethom lel calcule mtack stock wel alert w jaw heka lkol 
    // /**
    //  * Stock status (NORMAL, ALERTE_BASSE, RUPTURE)
    //  */
    // @JsonProperty("stock_status")
    // private String stockStatus;

    /**
     * Embedded inventory item information.
     */
    @JsonProperty("inventory_item")
    private InventoryItemEmbeddedDTO inventoryItem;

    /**
     * Nested DTO for embedded inventory item information.
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
