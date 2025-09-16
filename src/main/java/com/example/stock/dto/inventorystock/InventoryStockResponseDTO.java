package com.example.stock.dto.inventorystock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
    
    @JsonProperty("department_id")
    private String departmentId;
    
    @JsonProperty("current_quantity")
    private BigDecimal currentQuantity;

    @JsonProperty("average_unit_cost")
    private BigDecimal averageUnitCost;

    @JsonProperty("total_value")
    private BigDecimal totalValue;
/* 
    @JsonProperty("last_movement_date")
    private LocalDateTime lastMovementDate;
 */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    // Reserved for future stock status enrichment.

    /**
     * Embedded inventory item information.
     */
    @JsonProperty("inventory_item")
    private InventoryStockSummaryDTO inventoryItem;
}
