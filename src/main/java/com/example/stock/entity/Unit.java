package com.example.stock.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "unit",
       indexes = {
           @Index(name = "idx_unit_branch", columnList = "branch_id"),
           @Index(name = "idx_unit_department", columnList = "department_id"),
           @Index(name = "idx_unit_branch_department", columnList = "branch_id, department_id"),
           @Index(name = "idx_unit_name", columnList = "name"),
           @Index(name = "idx_unit_symbol", columnList = "symbol", unique = true)
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Unit {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String symbol;

    @Column(name = "branch_id", nullable = false)
    private String branchId;

    @Column(name = "department_id", nullable = false)
    private String departmentId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relations Many-to-One
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", insertable = false, updatable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    // Relation avec InventoryItem
    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InventoryItem> inventoryItems;
}
