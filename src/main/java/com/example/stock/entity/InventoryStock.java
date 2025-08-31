package com.example.stock.entity;

// import jakarta.persistence.*;
// import lombok.*;

// import java.math.BigDecimal;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.util.List;

// @Entity
// @Table(name = "inventory_stock")
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class InventoryStock {

//     @Id
//     private String id;

//     @Column(name = "inventory_item_id", nullable = false, insertable = false, updatable = false)
//     private String inventoryItemId;

//     @Column(name = "branch_id", nullable = false)
//     private String branchId;

//     @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
//     private BigDecimal quantity;

//     @Column(name = "unit_purchase_price", precision = 10, scale = 2)
//     private BigDecimal unitPurchasePrice;

//     @Column(name = "expiration_date")
//     private LocalDate expirationDate;

//     @Column(name = "created_at", nullable = false)
//     private LocalDateTime createdAt;

//     @Column(name = "updated_at", nullable = false)
//     private LocalDateTime updatedAt;

//     // Relations
//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "inventory_item_id")
//     private InventoryItem inventoryItem;

//     // Relation avec l’historique
//     @OneToMany(mappedBy = "inventoryStock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//     private List<InventoryMovement> movements;

//     @PrePersist
//     protected void onCreate() {
//         createdAt = LocalDateTime.now();
//         updatedAt = LocalDateTime.now();
//     }

//     @PreUpdate
//     protected void onUpdate() {
//         updatedAt = LocalDateTime.now();
//     }
// }
