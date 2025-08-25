package com.example.stock.dto.inventorymouvement;

import com.example.stock.entity.InventoryMovement.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for creating an inventory movement (alias: stock entry).
 * Matches frontend contract fields and names exactly.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryMovementCreateDTO {
    @NotBlank(message = "Inventory item ID is required")
    @JsonProperty("inventory_item_id")
    private String inventoryItemId;

    @NotNull(message = "Transaction type is required")
    @JsonProperty("transaction_type")
    private TransactionType transactionType;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private BigDecimal quantity;

    @JsonProperty("unit_purchase_price")
    private BigDecimal unitPurchasePrice;

    @JsonProperty("supplier_id")
    private String supplierId;

    @JsonProperty("branch_id")
    private String branchId;

    @JsonProperty("waste_reason")
    private String wasteReason;

    private String notes;

    @JsonProperty("expiration_date")
    private LocalDate expirationDate;
}
