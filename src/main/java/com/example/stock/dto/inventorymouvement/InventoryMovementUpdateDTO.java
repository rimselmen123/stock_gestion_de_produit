package com.example.stock.dto.inventorymouvement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Update DTO for inventory movement. Only fields that can be edited after creation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryMovementUpdateDTO {
    private String notes;
    @JsonProperty("quantity")
    private BigDecimal quantity;
    @JsonProperty("waste_reason")
    private String wasteReason;

    @JsonProperty("expiration_date")
    private LocalDate expirationDate;
    
    @JsonProperty("supplier_id")
    private String supplierId;
    
    @JsonProperty("destination_branch_id")
    private String destinationBranchId;
}
