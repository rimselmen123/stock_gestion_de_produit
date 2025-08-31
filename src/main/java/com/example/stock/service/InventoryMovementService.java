package com.example.stock.service;

import com.example.stock.dto.inventorymouvement.InventoryMovementCreateDTO;
import com.example.stock.dto.inventorymouvement.InventoryMovementResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryMovementService {
    InventoryMovementResponseDTO createMovement(InventoryMovementCreateDTO dto);
    Page<InventoryMovementResponseDTO> getAllMovements(Pageable pageable);
    InventoryMovementResponseDTO getMovementById(String id);
}
