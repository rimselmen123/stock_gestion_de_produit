package com.example.stock.repository;

import com.example.stock.entity.InventoryStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface InventoryStockRepository
        extends JpaRepository<InventoryStock, String>, JpaSpecificationExecutor<InventoryStock> {
    // -----------------------------
    // Existence checks / basics
    // -----------------------------
    boolean existsByInventoryItemIdAndBranchIdAndDepartmentId(String inventoryItemId, String branchId,
            String departmentId);

    // -----------------------------
    // Simple finders
    // -----------------------------
    List<InventoryStock> findByInventoryItemId(String inventoryItemId);

    Optional<InventoryStock> findByInventoryItemIdAndBranchIdAndDepartmentId(String inventoryItemId, String branchId,
            String departmentId);

    Page<InventoryStock> findByBranchId(String branchId, Pageable pageable);

        // For department deletion validation
        boolean existsByDepartmentId(String departmentId);

    // -----------------------------
    // Locking for safe concurrent updates
    // -----------------------------
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from InventoryStock s where s.inventoryItemId = :inventoryItemId and s.branchId = :branchId and s.departmentId = :departmentId")
    Optional<InventoryStock> lockByItemBranchDept(@Param("inventoryItemId") String inventoryItemId,
            @Param("branchId") String branchId,
            @Param("departmentId") String departmentId);
}
