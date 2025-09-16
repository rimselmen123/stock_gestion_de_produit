package com.example.stock.specification;

import com.example.stock.entity.InventoryMovement;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Dynamic Specifications for InventoryMovement to support flexible search/filter.
 */
public final class InventoryMovementSpecifications {

    private InventoryMovementSpecifications() {}

    public static Specification<InventoryMovement> hasBranchId(String branchId) {
        return (root, query, cb) ->
            (branchId == null || branchId.isBlank()) ? cb.conjunction() : cb.equal(root.get("branchId"), branchId);
    }

    public static Specification<InventoryMovement> hasSupplierId(String supplierId) {
        return (root, query, cb) ->
            (supplierId == null || supplierId.isBlank()) ? cb.conjunction() : cb.equal(root.get("supplierId"), supplierId);
    }

    public static Specification<InventoryMovement> hasTransactionType(InventoryMovement.TransactionType type) {
        return (root, query, cb) ->
            (type == null) ? cb.conjunction() : cb.equal(root.get("transactionType"), type);
    }

    public static Specification<InventoryMovement> hasDepartmentId(String departmentId) {
        return (root, query, cb) -> {
            if (departmentId == null || departmentId.isBlank()) return cb.conjunction();
            if (query != null) query.distinct(true);
            var itemJoin = root.join("inventoryItem", JoinType.LEFT);
            return cb.equal(itemJoin.get("departmentId"), departmentId);
        };
    }

    public static Specification<InventoryMovement> hasCategory(String categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null || categoryId.isBlank()) return cb.conjunction();
            if (query != null) query.distinct(true);
            var itemJoin = root.join("inventoryItem", JoinType.LEFT);
            return cb.equal(itemJoin.get("inventoryItemCategoryId"), categoryId);
        };
    }

    public static Specification<InventoryMovement> hasDateRange(String dateRange) {
        return (root, query, cb) -> {
            if (dateRange == null || dateRange.isBlank()) return cb.conjunction();
            
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime fromDate;
            
            switch (dateRange.toLowerCase()) {
                case "today":
                    fromDate = now.toLocalDate().atStartOfDay();
                    return cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
                case "week":
                    fromDate = now.minusWeeks(1);
                    return cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
                case "month":
                    fromDate = now.minusMonths(1);
                    return cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
                default:
                    return cb.conjunction();
            }
        };
    }

    public static Specification<InventoryMovement> itemNameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return cb.conjunction();
            if (query != null) query.distinct(true);
            var itemJoin = root.join("inventoryItem", JoinType.LEFT);
            return cb.like(cb.lower(itemJoin.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<InventoryMovement> textSearch(String term) {
        return (root, query, cb) -> {
            if (term == null || term.isBlank()) return cb.conjunction();
            if (query != null) query.distinct(true);
            var lowered = "%" + term.toLowerCase() + "%";
            var itemJoin = root.join("inventoryItem", JoinType.LEFT);
            return cb.or(
                cb.like(cb.lower(itemJoin.get("name")), lowered),
                cb.like(cb.lower(itemJoin.get("id")), lowered),
                cb.like(cb.lower(root.get("supplierId")), lowered),
                cb.like(cb.lower(root.get("branchId")), lowered),
                cb.like(cb.lower(root.get("notes")), lowered),
                cb.like(cb.lower(root.get("wasteReason")), lowered)
            );
        };
    }

    public static Specification<InventoryMovement> quantityMin(BigDecimal min) {
        return (root, query, cb) -> (min == null) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("quantity"), min);
    }

    public static Specification<InventoryMovement> quantityMax(BigDecimal max) {
        return (root, query, cb) -> (max == null) ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("quantity"), max);
    }

    public static Specification<InventoryMovement> unitPriceMin(BigDecimal min) {
        return (root, query, cb) -> (min == null) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("unitPurchasePrice"), min);
    }

    public static Specification<InventoryMovement> unitPriceMax(BigDecimal max) {
        return (root, query, cb) -> (max == null) ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("unitPurchasePrice"), max);
    }

    public static Specification<InventoryMovement> expirationAfter(LocalDate after) {
        return (root, query, cb) -> (after == null) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("expirationDate"), after);
    }

    public static Specification<InventoryMovement> expirationBefore(LocalDate before) {
        return (root, query, cb) -> (before == null) ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("expirationDate"), before);
    }

    public static Specification<InventoryMovement> createdAfter(LocalDateTime after) {
        return (root, query, cb) -> (after == null) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("createdAt"), after);
    }

    public static Specification<InventoryMovement> createdBefore(LocalDateTime before) {
        return (root, query, cb) -> (before == null) ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("createdAt"), before);
    }

    public static Specification<InventoryMovement> updatedAfter(LocalDateTime after) {
        return (root, query, cb) -> (after == null) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("updatedAt"), after);
    }

    public static Specification<InventoryMovement> updatedBefore(LocalDateTime before) {
        return (root, query, cb) -> (before == null) ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("updatedAt"), before);
    }

    /**
     * Build a dynamic specification from optional parameters.
     */
    public static Specification<InventoryMovement> build(
        String branchId,
        String departmentId,
        String supplierId,
        InventoryMovement.TransactionType transactionType,
        String categoryId,
        String dateRange,
        String itemName,
        String globalSearch,
        BigDecimal qtyMin,
        BigDecimal qtyMax,
        BigDecimal priceMin,
        BigDecimal priceMax,
        LocalDate expAfter,
        LocalDate expBefore,
        LocalDateTime createdAfter,
        LocalDateTime createdBefore,
        LocalDateTime updatedAfter,
        LocalDateTime updatedBefore
    ) {
        return Specification.allOf(
            hasBranchId(branchId),
            hasDepartmentId(departmentId),
            hasSupplierId(supplierId),
            hasTransactionType(transactionType),
            hasCategory(categoryId),
            hasDateRange(dateRange),
            itemNameContains(itemName),
            textSearch(globalSearch),
            quantityMin(qtyMin),
            quantityMax(qtyMax),
            unitPriceMin(priceMin),
            unitPriceMax(priceMax),
            expirationAfter(expAfter),
            expirationBefore(expBefore),
            createdAfter(createdAfter),
            createdBefore(createdBefore),
            updatedAfter(updatedAfter),
            updatedBefore(updatedBefore)
        );
    }
}
