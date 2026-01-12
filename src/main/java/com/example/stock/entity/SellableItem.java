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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "sellable_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellableItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "menu_item_snapshot_id", nullable = false)
    private Long menuItemSnapshotId;
    
    @Column(name = "menu_item_variation_snapshot_id")
    private Long menuItemVariationSnapshotId; // NULL si pas de variation
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_snapshot_id", insertable = false, updatable = false)
    private MenuItemSnapshot menuItemSnapshot;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_variation_snapshot_id", insertable = false, updatable = false)
    private MenuItemVariationSnapshot variation;
}