package com.example.stock.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
// hethi  l creation mta3 l branche taw nchofoha
@Entity
@Table(name = "branch",
       indexes = {
           @Index(name = "idx_branch_name", columnList = "name", unique = true),
           @Index(name = "idx_branch_location", columnList = "location"),
           @Index(name = "idx_branch_code", columnList = "code", unique = true),
           @Index(name = "idx_branch_active", columnList = "is_active"),
           @Index(name = "idx_branch_active_name", columnList = "is_active, name")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branch {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "location")
    private String location; // facultatif, adresse ou ville

    @Column(name = "code", length = 20, unique = true)
    private String code; // Ex: "BR001", "PAR01"

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relations (read-only back references to reduce coupling)

    @OneToMany(mappedBy = "branch")
    private List<InventoryMovement> movements;

    @OneToMany(mappedBy = "branch")
    private List<Department> departments;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isActive == null) isActive = true;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper method
    public boolean isOperational() {
        return Boolean.TRUE.equals(isActive);
    }
}
