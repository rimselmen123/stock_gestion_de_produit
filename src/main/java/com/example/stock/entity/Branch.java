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
           @Index(name = "idx_branch_location", columnList = "location")
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
    private String location ; // facultatif, adresse ou ville

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relations (read-only back references to reduce coupling)

    @OneToMany(mappedBy = "branch")
    private List<InventoryMovement> movements;

    @OneToMany(mappedBy = "branch")
    private List<Department> departments;
}
