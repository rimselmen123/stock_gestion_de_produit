package com.example.stock.dto.inventorystock;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for lightweight inventory stock listings.
 * Contains essential information for list views and search results.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStockSummaryDTO {

    private String id;

    @JsonProperty("inventory_item_id")
    private String inventoryItemId;

    @JsonProperty("branch_id")
    private String branchId;

    private BigDecimal quantity;

    @JsonProperty("expiration_date")
    private LocalDate expirationDate;

    /**
     * Item name for display purposes in summary lists.
     */
    @JsonProperty("inventory_item_name")
    private String inventoryItemName;
}
