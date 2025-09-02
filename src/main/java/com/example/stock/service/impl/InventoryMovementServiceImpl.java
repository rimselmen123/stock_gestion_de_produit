package com.example.stock.service.impl;

import com.example.stock.dto.inventorymouvement.InventoryMovementCreateDTO;
import com.example.stock.dto.inventorymouvement.InventoryMovementResponseDTO;
import com.example.stock.dto.inventorymouvement.InventoryMovementUpdateDTO;
import com.example.stock.entity.InventoryMovement;
import com.example.stock.entity.InventoryMovement.TransactionType;
import com.example.stock.exception.ForeignKeyConstraintException;
import com.example.stock.exception.ResourceNotFoundException;
import com.example.stock.mapper.InventoryMovementMapper;
import com.example.stock.repository.InventoryItemRepository;
import com.example.stock.repository.InventoryMovementRepository;
import com.example.stock.repository.SuppliersRepository;
import com.example.stock.service.InventoryMovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryMovementServiceImpl implements InventoryMovementService {

    private static final String ENTITY_NAME = "InventoryMovement";

    private final InventoryMovementRepository inventoryMovementRepository;
    private final InventoryMovementMapper inventoryMovementMapper;
    private final InventoryItemRepository inventoryItemRepository;
    private final SuppliersRepository suppliersRepository;

    @Override
    @Transactional
    public InventoryMovementResponseDTO createMovement(InventoryMovementCreateDTO dto) {
        log.info("Creating inventory movement: item={}, branch={}, type={}, qty={}",
                dto.getInventoryItemId(), dto.getBranchId(), dto.getTransactionType(), dto.getQuantity());
    validateSyntactic(dto);
    validateForeignKeys(dto);
    validateByType(dto);

        // Create and save the movement record
    InventoryMovement movement = inventoryMovementMapper.toEntity(dto);
    // Relations (inventoryItem & supplier) are lazy via FK; scalar IDs already copied.
    // Additional domain logic (stock adjustment) can be inserted here later.
        movement.setId(UUID.randomUUID().toString());
        movement.setCreatedAt(LocalDateTime.now());
        movement.setUpdatedAt(LocalDateTime.now());

    InventoryMovement savedMovement = inventoryMovementRepository.save(movement);
    return inventoryMovementMapper.toResponseDTO(savedMovement);
    }

    private void validateSyntactic(InventoryMovementCreateDTO dto) {
        if (dto.getQuantity() == null || dto.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
        if (dto.getTransactionType() == null) {
            throw new IllegalArgumentException("transaction_type is required");
        }
        if (dto.getInventoryItemId() == null || dto.getInventoryItemId().isBlank()) {
            throw new IllegalArgumentException("inventory_item_id is required");
        }
        if (dto.getBranchId() == null || dto.getBranchId().isBlank()) {
            throw new IllegalArgumentException("branch_id is required");
        }
    }

    private void validateForeignKeys(InventoryMovementCreateDTO dto) {
        String itemId = dto.getInventoryItemId();
        if (!inventoryItemRepository.existsById(itemId)) {
            throw new ForeignKeyConstraintException("inventory_item_id", itemId);
        }
        if (dto.getSupplierId() != null && !dto.getSupplierId().isBlank() && !suppliersRepository.existsById(dto.getSupplierId())) {
            throw new ForeignKeyConstraintException("supplier_id", dto.getSupplierId());
        }
    }

    private void validateByType(InventoryMovementCreateDTO dto) {
        TransactionType type = dto.getTransactionType();
        switch (type) {
            case IN -> validateIn(dto);
            case WASTE -> validateWaste(dto);
                case OUT -> validateOut();
            case TRANSFER -> validateTransfer(dto);
        }
    }

    private void validateIn(InventoryMovementCreateDTO dto) {
        if (dto.getUnitPurchasePrice() == null) {
            throw new IllegalArgumentException("unit_purchase_price is required for IN");
        }
        if (dto.getSupplierId() == null || dto.getSupplierId().isBlank()) {
            throw new IllegalArgumentException("supplier_id is required for IN");
        }
    }

    private void validateWaste(InventoryMovementCreateDTO dto) {
        if (dto.getWasteReason() == null || dto.getWasteReason().isBlank()) {
            throw new IllegalArgumentException("waste_reason is required for WASTE");
        }
    }

    private void validateOut() { /* future: validate available stock */ }

    private void validateTransfer(InventoryMovementCreateDTO dto) {
        String destBranch = dto.getDestinationBranchId();
        if (destBranch == null || destBranch.isBlank()) {
            throw new IllegalArgumentException("destination_branch_id is required for TRANSFER");
        }
        if (destBranch.equals(dto.getBranchId())) {
            throw new IllegalArgumentException("destination_branch_id must be different from branch_id");
        }
    }

    @Override
    public Page<InventoryMovementResponseDTO> getAllMovements(Pageable pageable) {
    return inventoryMovementRepository.findAll(pageable)
        .map(inventoryMovementMapper::toResponseDTO);
    }

    @Override
    public InventoryMovementResponseDTO getMovementById(String id) {
    InventoryMovement movement = inventoryMovementRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, id));
    return inventoryMovementMapper.toResponseDTO(movement);
    }

    @Override
    @Transactional
    public InventoryMovementResponseDTO updateMovement(String id, InventoryMovementUpdateDTO dto) {
        InventoryMovement movement = inventoryMovementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, id));

        // Business rules per transaction type
        switch (movement.getTransactionType()) {
            case IN -> validateUpdateIn(movement, dto);
            case OUT -> validateUpdateOut(movement, dto);
            case WASTE -> validateUpdateWaste(movement, dto);
            case TRANSFER -> validateUpdateTransfer(movement, dto);
        }

        // Apply allowed mutable fields (mapper ignores restricted ones)
        inventoryMovementMapper.updateEntityFromDTO(dto, movement);
        movement.setUpdatedAt(LocalDateTime.now());
        InventoryMovement saved = inventoryMovementRepository.save(movement);
        return inventoryMovementMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public void delete(String id) {
        InventoryMovement movement = inventoryMovementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, id));
        inventoryMovementRepository.delete(movement);
    }

    // --- Update validation helpers ---
    private void validateUpdateIn(InventoryMovement movement, InventoryMovementUpdateDTO dto) {
        // Quantity for IN generally shouldn't decrease after stock accounted; forbid quantity change
        if (dto.getQuantity() != null && dto.getQuantity().compareTo(movement.getQuantity()) != 0) {
            throw new IllegalArgumentException("quantity cannot be changed for IN movements");
        }
        // Allow supplier change? Usually no once received; forbid if different
        if (dto.getSupplierId() != null && !dto.getSupplierId().equals(movement.getSupplierId())) {
            throw new IllegalArgumentException("supplier_id cannot be changed for IN movements");
        }
    }

    private void validateUpdateOut(InventoryMovement movement, InventoryMovementUpdateDTO dto) {
        // OUT: quantity can increase only if sufficient stock (stock check not yet implemented)
        if (dto.getQuantity() != null && dto.getQuantity().compareTo(movement.getQuantity()) < 0) {
            throw new IllegalArgumentException("quantity decrease not allowed for OUT movements (auditing)");
        }
    }

    private void validateUpdateWaste(InventoryMovement movement, InventoryMovementUpdateDTO dto) {
        if (dto.getWasteReason() != null && dto.getWasteReason().isBlank()) {
            throw new IllegalArgumentException("waste_reason cannot be blank");
        }
        if (dto.getQuantity() != null && dto.getQuantity().compareTo(movement.getQuantity()) != 0) {
            throw new IllegalArgumentException("quantity cannot be changed for WASTE movements");
        }
    }

    private void validateUpdateTransfer(InventoryMovement movement, InventoryMovementUpdateDTO dto) {
        if (dto.getDestinationBranchId() != null && dto.getDestinationBranchId().equals(movement.getBranchId())) {
            throw new IllegalArgumentException("destination_branch_id must differ from source branch for TRANSFER");
        }
        if (dto.getQuantity() != null && dto.getQuantity().compareTo(movement.getQuantity()) != 0) {
            throw new IllegalArgumentException("quantity cannot be changed for TRANSFER movements once created");
        }
    }

    @Override
    public Page<InventoryMovementResponseDTO> searchMovements(MovementSearchFilter filter, Pageable pageable) {
        Specification<InventoryMovement> spec = com.example.stock.specification.InventoryMovementSpecifications.build(
                filter.branchId(),
                filter.supplierId(),
                parseType(filter.transactionType()),
                filter.itemName(),
                filter.globalSearch(),
                filter.qtyMin(),
                filter.qtyMax(),
                filter.priceMin(),
                filter.priceMax(),
                filter.expAfter(),
                filter.expBefore(),
                filter.createdAfter(),
                filter.createdBefore(),
                filter.updatedAfter(),
                filter.updatedBefore()
        );
        return inventoryMovementRepository.findAll(spec, pageable)
                .map(inventoryMovementMapper::toResponseDTO);
    }

    private TransactionType parseType(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return TransactionType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid transaction_type: " + value);
        }
    }
}
