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
 * Response DTO for inventory movements, aligned with frontend contract.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryMovementResponseDTO {
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

    @JsonProperty("destination_branch_id")
    private String destinationBranchId;

    @JsonProperty("waste_reason")
    private String wasteReason;

    private String notes;

    @JsonProperty("expiration_date")
    private LocalDate expirationDate;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("inventory_item")
    private InventoryItemEmbeddedDTO inventoryItem;

    private SupplierEmbeddedDTO supplier;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InventoryItemEmbeddedDTO {
        private String id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SupplierEmbeddedDTO {
        private String id;
        private String name;
    }
}
