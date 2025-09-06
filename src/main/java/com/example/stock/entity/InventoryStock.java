package com.example.stock.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_stock",
	   uniqueConstraints = {
		   @UniqueConstraint(name = "uk_stock_item_branch_dept_lot",
							 columnNames = {"inventory_item_id", "branch_id", "department_id", "expiration_date"})
	   },
	   indexes = {
		   @Index(name = "idx_stock_item_branch", columnList = "inventory_item_id,branch_id"),
		   @Index(name = "idx_stock_item_branch_dept", columnList = "inventory_item_id,branch_id,department_id")
	   })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStock {

	@Id
	private String id;

	// Scalar FKs for low coupling
	@Column(name = "inventory_item_id", nullable = false)
	private String inventoryItemId;

	@Column(name = "branch_id", nullable = false)
	private String branchId;

	@Column(name = "department_id")
	private String departmentId;

	// Optional per-lot tracking (null = aggregated)
	@Column(name = "expiration_date")
	private LocalDate expirationDate;

	@Column(name = "quantity", nullable = false, precision = 18, scale = 3)
	private BigDecimal curentQuantity;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	// Read-only associations
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inventory_item_id", insertable = false, updatable = false)
	private InventoryItem inventoryItem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "branch_id", insertable = false, updatable = false)
	private Branch branch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", insertable = false, updatable = false)
	private Department department;

	// Optimistic locking
	@Version
	private Long version;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
		if (curentQuantity == null) curentQuantity = BigDecimal.ZERO;
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}
