package com.example.stock.dto.inventorystock;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating an existing inventory stock.
 * All fields are optional; only provided ones will be processed by service layer.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStockUpdateDTO {

    @JsonProperty("branch_id")
    private String branchId;

    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    @JsonProperty("unit_purchase_price")
    private BigDecimal unitPurchasePrice;

    @JsonProperty("expiration_date")
    private LocalDate expirationDate;
}
