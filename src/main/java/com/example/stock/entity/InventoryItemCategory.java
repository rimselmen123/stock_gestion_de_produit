package com.example.stock.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inventory_item_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemCategory {

    @Id
    private String id;

    private String name;

    @Column(name = "branch_id", nullable = false)
    private String branchId;    

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relation avec InventoryItem
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<InventoryItem> inventoryItems;
    //relation jdida m3a l branche 
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "branch_id", nullable = false)
    //@private Branch branch;

}
