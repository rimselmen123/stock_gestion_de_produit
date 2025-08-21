package com.example.stock.dto.inventorystock;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new inventory stock.
 * Contains only the fields required for inventory stock creation.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStockCreateDTO {

    @NotBlank(message = "Inventory item ID is required")
    @JsonProperty("inventory_item_id")
    private String inventoryItemId;

    @NotBlank(message = "Branch ID is required")
    @JsonProperty("branch_id")
    private String branchId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    @JsonProperty("unit_purchase_price")
    private BigDecimal unitPurchasePrice;

    @JsonProperty("expiration_date")
    private LocalDate expirationDate;
}
