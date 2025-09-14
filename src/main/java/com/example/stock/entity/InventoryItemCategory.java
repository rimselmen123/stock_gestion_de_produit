package com.example.stock.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inventory_item_category",
       indexes = {
           @Index(name = "idx_item_category_branch", columnList = "branch_id"),
           @Index(name = "idx_item_category_department", columnList = "department_id"),
           @Index(name = "idx_item_category_branch_dept", columnList = "branch_id, department_id")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemCategory {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "branch_id", nullable = false)
    private String branchId;

    @Column(name = "department_id", nullable = false)
    private String departmentId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relations avec d'autres entités
    @OneToMany(mappedBy = "category")
    private List<InventoryItem> inventoryItems;

    // Read-only association to Branch (backed by branchId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", insertable = false, updatable = false)
    private Branch branch;

    // Read-only association to Department (backed by departmentId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
