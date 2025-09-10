package com.example.stock.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "inventory_stock",
    uniqueConstraints = {
        // Si tu ne gères PAS department: enlève "department_id" de la contrainte
        @UniqueConstraint(name = "uk_stock_item_branch_dept",
            columnNames = {"inventory_item_id","branch_id","department_id"})
    },
    indexes = {
        @Index(name = "idx_stock_branch", columnList = "branch_id"),
        @Index(name = "idx_stock_dept", columnList = "department_id"),
        @Index(name = "idx_stock_item", columnList = "inventory_item_id"),
        @Index(name = "idx_stock_updated_at", columnList = "updated_at"),
        @Index(name = "idx_stock_last_mv", columnList = "last_movement_date"),
        @Index(name = "idx_stock_qty", columnList = "current_quantity")
    }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InventoryStock {

    @Id
    private String id;

    @Column(name = "inventory_item_id", nullable = false, length = 64)
    private String inventoryItemId;

    @Column(name = "branch_id", nullable = false, length = 64)
    private String branchId;

    // Rends-le nullable = true si tu ne veux pas cette dimension
    @Column(name = "department_id", nullable = false, length = 64)
    private String departmentId;

    @Column(name = "current_quantity", nullable = false, precision = 18, scale = 6)
    @Builder.Default
    private BigDecimal currentQuantity = BigDecimal.ZERO;

    @Column(name = "average_unit_cost", precision = 18, scale = 6)
    private BigDecimal averageUnitCost;

    @Column(name = "total_value", precision = 20, scale = 6)
    private BigDecimal totalValue;

    @Column(name = "last_movement_date")
    private LocalDateTime lastMovementDate;

    @Version
    private Long version;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        var now = LocalDateTime.now();
        createdAt = now; updatedAt = now;
        if (currentQuantity == null) currentQuantity = BigDecimal.ZERO;
        if (averageUnitCost == null) averageUnitCost = BigDecimal.ZERO;
        recalcTotal();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
        recalcTotal();
    }

    public void recalcTotal() {
        if (currentQuantity != null && averageUnitCost != null) {
            totalValue = currentQuantity.multiply(averageUnitCost);
        }
    }

    public boolean isOutOfStock() {
        return currentQuantity == null || currentQuantity.signum() <= 0;
    }
}