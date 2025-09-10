package com.example.stock.specification;

import com.example.stock.entity.InventoryStock;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Dynamic Specifications for InventoryStock to support flexible search/filter.
 */
public final class InventoryStockSpecifications {

	private InventoryStockSpecifications() {}

	public static Specification<InventoryStock> hasBranchId(String branchId) {
		return (root, query, cb) ->
			(branchId == null || branchId.isBlank()) ? cb.conjunction() : cb.equal(root.get("branchId"), branchId);
	}

	public static Specification<InventoryStock> hasDepartmentId(String departmentId) {
		return (root, query, cb) ->
			(departmentId == null || departmentId.isBlank()) ? cb.conjunction() : cb.equal(root.get("departmentId"), departmentId);
	}

	public static Specification<InventoryStock> hasInventoryItemId(String inventoryItemId) {
		return (root, query, cb) ->
			(inventoryItemId == null || inventoryItemId.isBlank()) ? cb.conjunction() : cb.equal(root.get("inventoryItemId"), inventoryItemId);
	}

	// itemNameContains intentionally omitted: no InventoryItem relation on InventoryStock entity.

	public static Specification<InventoryStock> textSearch(String term) {
		return (root, query, cb) -> {
			if (term == null || term.isBlank()) return cb.conjunction();
			var lowered = "%" + term.toLowerCase() + "%";
			return cb.or(
				cb.like(cb.lower(root.get("inventoryItemId")), lowered),
				cb.like(cb.lower(root.get("branchId")), lowered)
			);
		};
	}

	public static Specification<InventoryStock> quantityMin(BigDecimal min) {
		return (root, query, cb) -> (min == null) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("currentQuantity"), min);
	}

	public static Specification<InventoryStock> quantityMax(BigDecimal max) {
		return (root, query, cb) -> (max == null) ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("currentQuantity"), max);
	}

	/**
	 * Build a dynamic specification from optional parameters.
	 */
	public static Specification<InventoryStock> build(
		String branchId,
		String departmentId,
	String inventoryItemId,
		String globalSearch,
		BigDecimal qtyMin,
		BigDecimal qtyMax
	) {
		return Specification.allOf(
			hasBranchId(branchId),
			hasDepartmentId(departmentId),
			hasInventoryItemId(inventoryItemId),
			textSearch(globalSearch),
			quantityMin(qtyMin),
			quantityMax(qtyMax)
		);
	}
}
