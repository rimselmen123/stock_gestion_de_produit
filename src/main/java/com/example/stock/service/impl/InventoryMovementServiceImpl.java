package com.example.stock.service.impl;

import com.example.stock.dto.inventorymouvement.InventoryMovementCreateDTO;
import com.example.stock.dto.inventorymouvement.InventoryMovementResponseDTO;
import com.example.stock.entity.InventoryMovement;
import com.example.stock.entity.InventoryMovement.TransactionType;
import com.example.stock.entity.Suppliers;
import com.example.stock.exception.ResourceNotFoundException;
import com.example.stock.mapper.InventoryMovementMapper;
import com.example.stock.repository.InventoryMovementRepository;
import com.example.stock.repository.SuppliersRepository;
import com.example.stock.service.InventoryMovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryMovementServiceImpl implements InventoryMovementService {

    private final InventoryMovementRepository inventoryMovementRepository;
    private final SuppliersRepository suppliersRepository;
    private final InventoryMovementMapper inventoryMovementMapper;

    @Override
    @Transactional
    public InventoryMovementResponseDTO createEntry(InventoryMovementCreateDTO dto) {
        log.info("Creating inventory movement: item={}, branch={}, type={}, qty={}",
                dto.getInventoryItemId(), dto.getBranchId(), dto.getTransactionType(), dto.getQuantity());

        // Basic validations
        if (dto.getQuantity() == null || dto.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (dto.getTransactionType() == null) {
            throw new IllegalArgumentException("Transaction type is required");
        }

        final String itemId = dto.getInventoryItemId();
        final String branchId = dto.getBranchId();
        if (itemId == null || itemId.isBlank()) {
            throw new IllegalArgumentException("Inventory item ID is required");
        }
        if (branchId == null || branchId.isBlank()) {
            throw new IllegalArgumentException("branch_id is required");
        }

        // Validate specific transaction type requirements
        TransactionType type = dto.getTransactionType();
        switch (type) {
            case IN -> {
                if (dto.getUnitPurchasePrice() == null) {
                    throw new IllegalArgumentException("unit_purchase_price is required for IN");
                }
                if (dto.getSupplierId() == null || dto.getSupplierId().isBlank()) {
                    throw new IllegalArgumentException("supplier_id is required for IN");
                }
            }
            case WASTE -> {
                if (dto.getWasteReason() == null || dto.getWasteReason().isBlank()) {
                    throw new IllegalArgumentException("waste_reason is required for WASTE");
                }
            }
            case OUT -> {
                // No additional validation needed for OUT transactions
            }
            case TRANSFER -> {
                String destBranch = dto.getDestinationBranchId();
                if (destBranch == null || destBranch.isBlank()) {
                    throw new IllegalArgumentException("destination_branch_id is required for TRANSFER");
                }
                if (destBranch.equals(branchId)) {
                    throw new IllegalArgumentException("destination_branch_id must be different from branch_id");
                }
            }
        }

        // Create and save the movement record
        InventoryMovement movement = inventoryMovementMapper.toEntity(dto);
        movement.setId(UUID.randomUUID().toString());
        movement.setCreatedAt(LocalDateTime.now());
        movement.setUpdatedAt(LocalDateTime.now());

        // Set supplier relation if provided
        if (dto.getSupplierId() != null && !dto.getSupplierId().isBlank()) {
            try {
                Suppliers supplierRef = suppliersRepository.getReferenceById(dto.getSupplierId());
                movement.setSupplier(supplierRef);
            } catch (Exception ex) {
                throw new ResourceNotFoundException("Supplier", dto.getSupplierId());
            }
        }

        InventoryMovement savedMovement = inventoryMovementRepository.save(movement);
        return inventoryMovementMapper.toResponseDTO(savedMovement);
    }
}
