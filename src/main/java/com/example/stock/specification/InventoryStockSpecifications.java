package com.example.stock.specification;

// import com.example.stock.entity.InventoryStock;
// import org.springframework.data.jpa.domain.Specification;

// import jakarta.persistence.criteria.JoinType;
// import java.math.BigDecimal;
// import java.time.LocalDate;

// /**
//  * Dynamic Specifications for InventoryStock to support flexible search/filter.
//  */
// public final class InventoryStockSpecifications {

//     private InventoryStockSpecifications() {}

//     public static Specification<InventoryStock> hasBranchId(String branchId) {
//         return (root, query, cb) ->
//             (branchId == null || branchId.isBlank()) ? cb.conjunction() : cb.equal(root.get("branchId"), branchId);
//     }

//     public static Specification<InventoryStock> hasInventoryItemId(String inventoryItemId) {
//         return (root, query, cb) ->
//             (inventoryItemId == null || inventoryItemId.isBlank()) ? cb.conjunction() : cb.equal(root.get("inventoryItemId"), inventoryItemId);
//     }

//     public static Specification<InventoryStock> itemNameContains(String name) {
//         return (root, query, cb) -> {
//             if (name == null || name.isBlank()) return cb.conjunction();
//             var itemJoin = root.join("inventoryItem", JoinType.LEFT);
//             return cb.like(cb.lower(itemJoin.get("name")), "%" + name.toLowerCase() + "%");
//         };
//     }

//     public static Specification<InventoryStock> textSearch(String term) {
//         return (root, query, cb) -> {
//             if (term == null || term.isBlank()) return cb.conjunction();
//             var lowered = "%" + term.toLowerCase() + "%";
//             var itemJoin = root.join("inventoryItem", JoinType.LEFT);
//             return cb.or(
//                 cb.like(cb.lower(itemJoin.get("name")), lowered),
//                 cb.like(cb.lower(root.get("inventoryItemId")), lowered),
//                 cb.like(cb.lower(root.get("branchId")), lowered)
//             );
//         };
//     }

//     public static Specification<InventoryStock> quantityMin(BigDecimal min) {
//         return (root, query, cb) -> (min == null) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("quantity"), min);
//     }

//     public static Specification<InventoryStock> quantityMax(BigDecimal max) {
//         return (root, query, cb) -> (max == null) ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("quantity"), max);
//     }

//     public static Specification<InventoryStock> expirationAfter(LocalDate after) {
//         return (root, query, cb) -> (after == null) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("expirationDate"), after);
//     }

//     public static Specification<InventoryStock> expirationBefore(LocalDate before) {
//         return (root, query, cb) -> (before == null) ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("expirationDate"), before);
//     }

//     /**
//      * Build a dynamic specification from optional parameters.
//      */
//     public static Specification<InventoryStock> build(
//         String branchId,
//         String inventoryItemId,
//         String itemName,
//         String globalSearch,
//         BigDecimal qtyMin,
//         BigDecimal qtyMax,
//         LocalDate expAfter,
//         LocalDate expBefore
//     ) {
//         return Specification.allOf(
//             hasBranchId(branchId),
//             hasInventoryItemId(inventoryItemId),
//             itemNameContains(itemName),
//             textSearch(globalSearch),
//             quantityMin(qtyMin),
//             quantityMax(qtyMax),
//             expirationAfter(expAfter),
//             expirationBefore(expBefore)
//         );
//     }
// }
