package com.example.stock.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "menu_item_variation_snapshot")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemVariationSnapshot {
    @Id
    @Column(name = "id", nullable = false)
    private Long id; // ID du POS
    
    @Column(name = "menu_item_snapshot_id", nullable = false)
    private Long menuItemSnapshotId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_snapshot_id", insertable = false, updatable = false)
    private MenuItemSnapshot menuItemSnapshot;
}