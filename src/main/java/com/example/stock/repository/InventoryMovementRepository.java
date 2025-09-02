package com.example.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.*;
import com.example.stock.entity.InventoryMovement;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, String>, JpaSpecificationExecutor<InventoryMovement> {
    // -----------------------------
    // Basics / Existence
    // -----------------------------
    boolean existsBySupplierId(String supplierId);
    boolean existsByBranchId(String branchId);

    // -----------------------------
    // Simple finders with pagination
    // -----------------------------
    Page<InventoryMovement> findByBranchId(String branchId, Pageable pageable);
    Page<InventoryMovement> findBySupplierId(String supplierId, Pageable pageable);
    Page<InventoryMovement> findByTransactionType(InventoryMovement.TransactionType transactionType, Pageable pageable);

    // -----------------------------
    // Global sorting by updatedAt
    // -----------------------------
    List<InventoryMovement> findAllByOrderByUpdatedAtDesc();
    Page<InventoryMovement> findAllByOrderByUpdatedAtDesc(Pageable pageable);
    Page<InventoryMovement> findByBranchIdOrderByUpdatedAtDesc(String branchId, Pageable pageable);
}
