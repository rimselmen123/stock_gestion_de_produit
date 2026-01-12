package com.example.stock.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Index;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ingredients",
       indexes = {
           @Index(name = "idx_ingredient_recipe", columnList = "recipe_id"),
           @Index(name = "idx_ingredient_inventory_item", columnList = "inventory_item_id"),
           @Index(name = "idx_ingredient_recipe_item", columnList = "recipe_id, inventory_item_id")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {

    @Id
    private String id;
    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;
    @Column(name = "recipe_id", nullable = false)
    private String recipeId;
    @Column(name = "inventory_item_id", nullable = false)
    private String inventoryItemId;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", insertable = false, updatable = false)
    private Recipes recipe;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", insertable = false, updatable = false)
    private InventoryItem inventoryItem;
    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
