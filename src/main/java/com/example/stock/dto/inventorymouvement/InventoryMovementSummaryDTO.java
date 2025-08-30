package com.example.stock.dto.inventorymouvement;

import com.example.stock.entity.InventoryMovement.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Lightweight DTO for inventory movement listings, per frontend contract.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryMovementSummaryDTO {
    private String id;

    @JsonProperty("inventory_item_id")
    private String inventoryItemId;

    @JsonProperty("branch_id")
    private String branchId;

    @JsonProperty("transaction_type")
    private TransactionType transactionType;

    private BigDecimal quantity;

    @JsonProperty("unit_purchase_price")
    private BigDecimal unitPurchasePrice;

    @JsonProperty("supplier_id")
    private String supplierId;

    private String notes;

    @JsonProperty("expiration_date")
    private LocalDate expirationDate;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("inventory_item")
    private InventoryMovementResponseDTO.SimpleDTO  inventoryItem;

    private InventoryMovementResponseDTO.SimpleDTO supplier;
}
