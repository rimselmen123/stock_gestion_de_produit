package com.example.stock.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inventory_item_category",
       indexes = {
           @Index(name = "idx_item_category_department", columnList = "department_id")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemCategory {

    @Id
    private String id;

    private String name;

    // Each category belongs to a department
    @Column(name = "department_id", nullable = false)
    private String departmentId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relation avec InventoryItem (read-only back-reference)
    @OneToMany(mappedBy = "category")
    private List<InventoryItem> inventoryItems;

    // Read-only association to Department (backed by departmentId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
