package com.example.stock.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inventory_item",
       indexes = {
           @Index(name = "idx_inventory_item_category", columnList = "inventory_item_category_id"),
           @Index(name = "idx_inventory_item_name", columnList = "name"),
           @Index(name = "idx_inventory_item_branch", columnList = "branch_id"),
           @Index(name = "idx_inventory_item_department", columnList = "department_id"),
           @Index(name = "idx_inventory_item_branch_dept", columnList = "branch_id, department_id"
           
           )
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "branch_id", nullable = false)
    private String branchId;

    @Column(name = "department_id", nullable = false)
    private String departmentId;

    @Column(name = "threshold_quantity", nullable = false)
    private int thresholdQuantity;

    @Column(name = "reorder_quantity", nullable = false)
    private int reorderQuantity;

    // currentQuantity removed in favor of InventoryStock aggregation
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relations
    @ManyToOne
    @JoinColumn(name = "inventory_item_category_id", nullable = false)
    private InventoryItemCategory category;

    @ManyToOne
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    // Read-only association to Branch (backed by branchId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", insertable = false, updatable = false)
    private Branch branch;

    // Read-only association to Department (backed by departmentId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    // Relation avec inventory movement (history). No cascade REMOVE from item side to preserve history.
    @OneToMany(mappedBy = "inventoryItem")
    private List<InventoryMovement> movements;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_id", nullable = false)
    private Tax tax;
    
    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
