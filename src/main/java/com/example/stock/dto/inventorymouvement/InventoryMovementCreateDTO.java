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
     * DTO générique pour créer un mouvement de stock.
     * Validation de base (syntaxe, format, null, etc.) est ici.
     * Les règles conditionnelles dépendent du transaction_type et seront validées dans la couche service.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class InventoryMovementCreateDTO {
    
        @NotBlank(message = "Inventory item ID is required")
        @JsonProperty("inventory_item_id")
        private String inventoryItemId;
    
        @NotBlank(message = "Branch ID is required")
        @JsonProperty("branch_id")
        private String branchId;
    
        @NotNull(message = "Transaction type is required")
        @JsonProperty("transaction_type")
        private TransactionType transactionType;
    
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than 0")
        private BigDecimal quantity;
    
        // Pour IN
        @JsonProperty("unit_purchase_price")
        private BigDecimal unitPurchasePrice;
    
        @JsonProperty("supplier_id")
        private String supplierId;
    
        @JsonProperty("expiration_date")
        private LocalDate expirationDate;
    
        // Pour WASTE
        @JsonProperty("waste_reason")
        private String wasteReason;
    
        // Pour TRANSFER
        @JsonProperty("destination_branch_id")
        private String destinationBranchId;
    
        // Optionnel
        private String notes;
    }
    