package com.example.stock.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "department",
       indexes = {
           @Index(name = "idx_department_branch", columnList = "branch_id"),
           @Index(name = "idx_department_name", columnList = "name")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    private String  id ;

    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "branch_id", nullable = false)
    private String branchId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Read-only association to Branch
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", insertable = false, updatable = false)
    private Branch branch;

    // Read-only back reference: a department has many categories
    @OneToMany(mappedBy = "department")
    private java.util.List<InventoryItemCategory> categories;
    
    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    

}
