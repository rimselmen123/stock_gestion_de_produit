package com.example.stock.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_item")
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
}
