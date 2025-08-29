package com.example.stock.service;

import com.example.stock.dto.inventorymouvement.InventoryMovementCreateDTO;
import com.example.stock.dto.inventorymouvement.InventoryMovementResponseDTO;

public interface InventoryMovementService {
    /**
     * Create an inventory movement record.
     * @param createDTO The DTO containing movement details
     * @return The created movement record
     */
    InventoryMovementResponseDTO createEntry(InventoryMovementCreateDTO createDTO);
}
