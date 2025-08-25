package com.example.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.stock.entity.InventoryStock;

public interface InventoryStockRepository extends JpaRepository<InventoryStock, String>, JpaSpecificationExecutor<InventoryStock> {
    // -----------------------------
    // Existence checks / basics
    // -----------------------------
    /**
     * Check if a stock record exists for a given inventory item in a branch.
     */
    boolean existsByInventoryItemIdAndBranchId(String inventoryItemId, String branchId);

    // -----------------------------
    // Simple finders
    // -----------------------------
    /**
     * Find all stock records for a specific inventory item.
     */
    java.util.List<InventoryStock> findByInventoryItemId(String inventoryItemId);

    /**
     * Find all stock records for a given branch with pagination.
     */
    Page<InventoryStock> findByBranchId(String branchId, Pageable pageable);

    // -----------------------------
    // Search (case-insensitive) with pagination
    // -----------------------------
    /**
     * Find stocks where the inventory item name OR inventory item ID OR branch ID contains the specified text (case-insensitive), paginated.
     * Useful for global search on stock list.
     */
    Page<InventoryStock> findByInventoryItem_NameContainingIgnoreCaseOrInventoryItemIdContainingIgnoreCaseOrBranchIdContainingIgnoreCase(
        String itemName,
        String inventoryItemId,
        String branchId,
        Pageable pageable
    );

    /**
     * Find stocks for a given branch where inventory item name contains the specified text (case-insensitive), paginated.
     */
    Page<InventoryStock> findByBranchIdAndInventoryItem_NameContainingIgnoreCase(String branchId, String itemName, Pageable pageable);

    // -----------------------------
    // Global sorting by updatedAt
    // -----------------------------
    /**
     * Return all stocks sorted by most recently updated first.
     */
    java.util.List<InventoryStock> findAllByOrderByUpdatedAtDesc();

    /**
     * Return paginated stocks sorted by most recently updated first.
     */
    Page<InventoryStock> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    /**
     * Return paginated stocks of a branch sorted by most recently updated first.
     */
    Page<InventoryStock> findByBranchIdOrderByUpdatedAtDesc(String branchId, Pageable pageable);

    /**
     * Search (case-insensitive) with enforced sorting by most recently updated first.
     */
    Page<InventoryStock> findByInventoryItem_NameContainingIgnoreCaseOrInventoryItemIdContainingIgnoreCaseOrBranchIdContainingIgnoreCaseOrderByUpdatedAtDesc(
        String itemName,
        String inventoryItemId,
        String branchId,
        Pageable pageable
    );
}
