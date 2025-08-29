package com.example.stock.repository;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

// import com.example.stock.entity.InventoryStock;
// import com.example.stock.repository.projection.ItemAverageProjection;
// import java.math.BigDecimal;
// import java.util.Optional;

// public interface InventoryStockRepository extends JpaRepository<InventoryStock, String>, JpaSpecificationExecutor<InventoryStock> {
//     // -----------------------------
//     // Existence checks / basics
//     // -----------------------------
//     /**
//      * Check if a stock record exists for a given inventory item in a branch.
//      */
//     boolean existsByInventoryItemIdAndBranchId(String inventoryItemId, String branchId);

//     // -----------------------------
//     // Simple finders
//     // -----------------------------
//     /**
//      * Find all stock records for a specific inventory item.
//      */
//     java.util.List<InventoryStock> findByInventoryItemId(String inventoryItemId);
    
//     /**
//      * Find a specific stock record by inventory item ID and branch ID.
//      * @param inventoryItemId the ID of the inventory item
//      * @param branchId the ID of the branch
//      * @return an Optional containing the found stock, or empty if not found
//      */
//     Optional<InventoryStock> findByInventoryItemIdAndBranchId(String inventoryItemId, String branchId);

//     /**
//      * Find all stock records for a given branch with pagination.
//      */
//     Page<InventoryStock> findByBranchId(String branchId, Pageable pageable);

//     // -----------------------------
//     // Quantity-based queries
//     // -----------------------------
//     /**
//      * Find all stock records for a given branch where quantity is less than or equal to the specified threshold, with pagination.
//      * @param branchId the ID of the branch
//      * @param threshold the maximum quantity threshold
//      * @param pageable pagination information
//      * @return a Page of InventoryStock matching the criteria
//      */
//     Page<InventoryStock> findByBranchIdAndQuantityLessThanEqual(String branchId, double threshold, Pageable pageable);
    
//     // -----------------------------
//     // Search (case-insensitive) with pagination
//     // -----------------------------
//     /**
//      * Find stocks where the inventory item name OR inventory item ID OR branch ID contains the specified text (case-insensitive), paginated.
//      * Useful for global search on stock list.
//      */
//     Page<InventoryStock> findByInventoryItem_NameContainingIgnoreCaseOrInventoryItemIdContainingIgnoreCaseOrBranchIdContainingIgnoreCase(
//         String itemName,
//         String inventoryItemId,
//         String branchId,
//         Pageable pageable
//     );

//     /**
//      * Find stocks for a given branch where inventory item name contains the specified text (case-insensitive), paginated.
//      */
//     Page<InventoryStock> findByBranchIdAndInventoryItem_NameContainingIgnoreCase(String branchId, String itemName, Pageable pageable);

//     // -----------------------------
//     // Global sorting by updatedAt
//     // -----------------------------
//     /**
//      * Return all stocks sorted by most recently updated first.
//      */
//     java.util.List<InventoryStock> findAllByOrderByUpdatedAtDesc();

//     /**
//      * Return paginated stocks sorted by most recently updated first.
//      */
//     Page<InventoryStock> findAllByOrderByUpdatedAtDesc(Pageable pageable);

//     /**
//      * Return paginated stocks of a branch sorted by most recently updated first.
//      */
//     Page<InventoryStock> findByBranchIdOrderByUpdatedAtDesc(String branchId, Pageable pageable);

//     /**
//      * Search (case-insensitive) with enforced sorting by most recently updated first.
//      */
//     Page<InventoryStock> findByInventoryItem_NameContainingIgnoreCaseOrInventoryItemIdContainingIgnoreCaseOrBranchIdContainingIgnoreCaseOrderByUpdatedAtDesc(
//         String itemName,
//         String inventoryItemId,
//         String branchId,
//         Pageable pageable
//     );

//     // -----------------------------
//     // Aggregations (native SQL)
//     // -----------------------------
//     /**
//      * Compute weighted average unit purchase price per item, with optional filters.
//      * Returns one row per inventory item.
//      *
//      * @param branchId optional branch filter
//      * @param from optional created_at from (ISO-8601 string; casted to timestamp)
//      * @param to optional created_at to (ISO-8601 string; casted to timestamp)
//      */
//     @Query(value = """
//             SELECT s.inventory_item_id AS inventoryItemId,
//                    i.name AS name,
//                    COALESCE(SUM(s.unit_purchase_price * s.quantity) / NULLIF(SUM(s.quantity), 0), 0) AS moyenne
//             FROM inventory_stock s
//             LEFT JOIN inventory_item i ON i.id = s.inventory_item_id
//             WHERE s.unit_purchase_price IS NOT NULL
//               AND s.quantity IS NOT NULL
//               AND (:branchId IS NULL OR s.branch_id = :branchId)
//               AND (:from IS NULL OR s.created_at >= CAST(:from AS timestamp))
//               AND (:to IS NULL OR s.created_at <= CAST(:to AS timestamp))
//             GROUP BY s.inventory_item_id, i.name
//             """,
//             nativeQuery = true)
//     java.util.List<ItemAverageProjection> findItemWeightedAverages(
//             @Param("branchId") String branchId,
//             @Param("from") String from,
//             @Param("to") String to);

//     /**
//      * Calculate average purchase price for a specific inventory item.
//      */
//     @Query(value = """
//             SELECT COALESCE(SUM(unit_purchase_price * quantity) / NULLIF(SUM(quantity), 0), 0)
//             FROM inventory_stock
//             WHERE inventory_item_id = :inventoryItemId
//               AND unit_purchase_price IS NOT NULL
//               AND quantity > 0
//             """,
//             nativeQuery = true)
//     BigDecimal findAverageUnitPriceByInventoryItemId(@Param("inventoryItemId") String inventoryItemId);
// }

