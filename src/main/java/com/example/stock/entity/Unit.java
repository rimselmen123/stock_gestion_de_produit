package com.example.stock.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "unit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Unit {

    @Id
    private String id;

    private String name;

    private String symbol;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relation avec InventoryItem
    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL)
    private List<InventoryItem> inventoryItems;
}
