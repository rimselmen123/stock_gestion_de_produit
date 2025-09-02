package com.example.stock.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_movement")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// la class de l'inventory management joueer  le role de inventory management et inventory stock en meme temps
public class InventoryMovement {

    @Id
    private String id;
    
    // Foreign keys (scalar) kept for simpler writes & specifications
    @Column(name = "inventory_item_id", nullable = false)
    private String inventoryItemId;

    @Column(name = "supplier_id")
    private String supplierId;
    //khalihe branche id  telab nafs  branche name  w branche id
    @Column(name = "branch_id", nullable = false)
    private String branchId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unit_purchase_price", precision = 10, scale = 2)
    private BigDecimal unitPurchasePrice;
    @Column(name = "notes")
    private String notes;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "destination_branch_id")
    private String destinationBranchId;

    @Column(name = "waste_reason")
    private String wasteReason;
    //relation m3a inventory item (read-only association set via FK field)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", insertable = false, updatable = false)
    private InventoryItem inventoryItem;

    // relation m3a supplier (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", insertable = false, updatable = false)
    private Suppliers supplier;


    //ba3ed  hethom les  insersions
   /*  @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;  // branche source

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_branch_id")
    private Branch destinationBranch; // branche destination (utile pour TRANSFER)
 */
    public enum TransactionType {
        IN, OUT, WASTE, TRANSFER
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
