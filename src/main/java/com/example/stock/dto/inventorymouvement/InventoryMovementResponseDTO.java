    package com.example.stock.dto.inventorymouvement;

    import com.example.stock.entity.InventoryMovement;
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
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class InventoryMovementResponseDTO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("inventory_item_id")
        private String inventoryItemId;
        @JsonProperty("branch_id")
        private String branchId;
        @JsonProperty("transaction_type")
        private String transactionType;
        @JsonProperty("quantity")
        private BigDecimal quantity;
        @JsonProperty("unit_purchase_price")
        private BigDecimal unitPurchasePrice;
        @JsonProperty("supplier_id")
        private String supplierId;
        @JsonProperty("notes")
        private String notes;
        @JsonProperty("expiration_date")
        private LocalDate expirationDate;
    @JsonProperty("waste_reason")
    private String wasteReason;
    @JsonProperty("destination_branch_id")
    private String destinationBranchId;
        @JsonProperty("created_at")
        private LocalDateTime createdAt;
        @JsonProperty("updated_at")
        private LocalDateTime updatedAt;

        private SimpleDTO inventoryItem;
        private SimpleDTO supplier;

        public static InventoryMovementResponseDTO fromEntity(InventoryMovement entity) {
            return InventoryMovementResponseDTO.builder()
                    .id(entity.getId())
                    .inventoryItemId(entity.getInventoryItem().getId())
                    .branchId(entity.getBranchId())
                    .transactionType(entity.getTransactionType().name())
                    .quantity(entity.getQuantity())
                    .unitPurchasePrice(entity.getUnitPurchasePrice())
                    .supplierId(entity.getSupplier() != null ? entity.getSupplier().getId() : null)
                    .notes(entity.getNotes())
                    .expirationDate(entity.getExpirationDate())
                    .wasteReason(entity.getWasteReason())
                    .destinationBranchId(entity.getDestinationBranchId())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .inventoryItem(new SimpleDTO(entity.getInventoryItem().getId(), entity.getInventoryItem().getName()))
                    .supplier(entity.getSupplier() != null ?
                            new SimpleDTO(entity.getSupplier().getId(), entity.getSupplier().getName()) : null)
                    .build();
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SimpleDTO {
            @JsonProperty("id")
            private String id;
            @JsonProperty("name")
            private String name;
        }
    }
