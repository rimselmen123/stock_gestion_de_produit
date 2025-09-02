package com.example.stock.service;

import com.example.stock.dto.inventorymouvement.InventoryMovementCreateDTO;
import com.example.stock.dto.inventorymouvement.InventoryMovementResponseDTO;
import com.example.stock.dto.inventorymouvement.InventoryMovementUpdateDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface InventoryMovementService {
    InventoryMovementResponseDTO createMovement(InventoryMovementCreateDTO dto);
    Page<InventoryMovementResponseDTO> getAllMovements(Pageable pageable);
    InventoryMovementResponseDTO getMovementById(String id);

    record MovementSearchFilter(
        String branchId,
        String supplierId,
        String transactionType,
        String itemName,
        String globalSearch,
        java.math.BigDecimal qtyMin,
        java.math.BigDecimal qtyMax,
        java.math.BigDecimal priceMin,
        java.math.BigDecimal priceMax,
        java.time.LocalDate expAfter,
        java.time.LocalDate expBefore,
        java.time.LocalDateTime createdAfter,
        java.time.LocalDateTime createdBefore,
        java.time.LocalDateTime updatedAfter,
        java.time.LocalDateTime updatedBefore
    ) {}

    Page<InventoryMovementResponseDTO> searchMovements(MovementSearchFilter filter, Pageable pageable);
    InventoryMovementResponseDTO updateMovement(String id, InventoryMovementUpdateDTO dto);
    void delete(String id);
}
