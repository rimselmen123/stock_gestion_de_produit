package com.example.stock.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inventory_item",
       indexes = {
           @Index(name = "idx_inventory_item_category", columnList = "inventory_item_category_id"),
           @Index(name = "idx_inventory_item_name", columnList = "name")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {

    @Id
    private String id;

    private String name;

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
    // Relation m3a inventory mouvement (history). No cascade REMOVE from item side to preserve history.
    @OneToMany(mappedBy = "inventoryItem")
    private List<InventoryMovement> movements;
    // Branch relationship handled via category.branchId and stock granularity

}
