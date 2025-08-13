package com.example.stock.service.impl;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.common.PaginationInfo;
import com.example.stock.dto.category.CategoryResponseDTO;
import com.example.stock.dto.inventoryitem.InventoryItemCreateDTO;
import com.example.stock.dto.inventoryitem.InventoryItemResponseDTO;
import com.example.stock.dto.inventoryitem.InventoryItemUpdateDTO;
import com.example.stock.dto.unit.UnitResponseDTO;
import com.example.stock.entity.InventoryItem;
import com.example.stock.entity.InventoryItemCategory;
import com.example.stock.entity.Unit;
import com.example.stock.exception.DeleteConstraintException;
import com.example.stock.exception.ForeignKeyConstraintException;
import com.example.stock.exception.ResourceNotFoundException;
import com.example.stock.repository.InventoryItemCategoryRepository;
import com.example.stock.repository.InventoryItemRepository;
import com.example.stock.repository.UnitRepository;
import com.example.stock.service.InventoryItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of InventoryItemService interface.
 * Handles all business logic for Inventory Item entity operations.
 * 
 * @author Generated
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryItemServiceImpl implements InventoryItemService {
    
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryItemCategoryRepository categoryRepository;
    private final UnitRepository unitRepository;
    
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<InventoryItemResponseDTO> findAllWithPagination(
            String search, int page, int perPage, String sortField, String sortDirection) {
        
        log.debug("Finding inventory items with pagination - search: {}, page: {}, perPage: {}", search, page, perPage);
        
        // Validate and adjust pagination parameters
        page = Math.max(1, page);
        perPage = Math.min(Math.max(1, perPage), 100);
        
        // Create sort object
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, mapSortField(sortField));
        
        // Create pageable (Spring uses 0-based indexing)
        Pageable pageable = PageRequest.of(page - 1, perPage, sort);
        
        // Execute query
        Page<InventoryItem> inventoryItemPage;
        if (search != null && !search.trim().isEmpty()) {
            inventoryItemPage = inventoryItemRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
        } else {
            inventoryItemPage = inventoryItemRepository.findAll(pageable);
        }
        
        // Convert to DTOs
        List<InventoryItemResponseDTO> inventoryItemDTOs = inventoryItemPage.getContent().stream()
            .map(this::convertToResponseDTO)
            .toList();
        
        // Create pagination info
        PaginationInfo paginationInfo = PaginationInfo.of(page, perPage, inventoryItemPage.getTotalElements());
        
        return PaginatedResponse.of(inventoryItemDTOs, paginationInfo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public InventoryItemResponseDTO findByIdOrThrow(String id) {
        log.debug("Finding inventory item by ID: {}", id);
        
        InventoryItem inventoryItem = inventoryItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory item", id));
        
        return convertToResponseDTO(inventoryItem);
    }
    
    @Override
    public InventoryItemResponseDTO create(InventoryItemCreateDTO createDTO) {
        log.info("Creating new inventory item with name: {}", createDTO.getName());
        
        // Validate foreign keys
        InventoryItemCategory category = categoryRepository.findById(createDTO.getInventoryItemCategoryId())
            .orElseThrow(() -> new ForeignKeyConstraintException("inventory_item_category_id", createDTO.getInventoryItemCategoryId()));
        
        Unit unit = unitRepository.findById(createDTO.getUnitId())
            .orElseThrow(() -> new ForeignKeyConstraintException("unit_id", createDTO.getUnitId()));
        
        // Create entity
        InventoryItem inventoryItem = InventoryItem.builder()
            .id(UUID.randomUUID().toString())
            .name(createDTO.getName())
            .thresholdQuantity(createDTO.getThresholdQuantity())
            .reorderQuantity(createDTO.getReorderQuantity())
            .category(category)
            .unit(unit)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem);
        log.info("Inventory item created successfully with ID: {}", savedInventoryItem.getId());
        
        return convertToResponseDTO(savedInventoryItem);
    }
    
    @Override
    public InventoryItemResponseDTO update(String id, InventoryItemUpdateDTO updateDTO) {
        log.info("Updating inventory item with ID: {}", id);
        
        InventoryItem existingInventoryItem = inventoryItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory item", id));
        
        // Validate foreign keys
        InventoryItemCategory category = categoryRepository.findById(updateDTO.getInventoryItemCategoryId())
            .orElseThrow(() -> new ForeignKeyConstraintException("inventory_item_category_id", updateDTO.getInventoryItemCategoryId()));
        
        Unit unit = unitRepository.findById(updateDTO.getUnitId())
            .orElseThrow(() -> new ForeignKeyConstraintException("unit_id", updateDTO.getUnitId()));
        
        // Update fields
        existingInventoryItem.setName(updateDTO.getName());
        existingInventoryItem.setThresholdQuantity(updateDTO.getThresholdQuantity());
        existingInventoryItem.setReorderQuantity(updateDTO.getReorderQuantity());
        existingInventoryItem.setCategory(category);
        existingInventoryItem.setUnit(unit);
        existingInventoryItem.setUpdatedAt(LocalDateTime.now());
        
        InventoryItem updatedInventoryItem = inventoryItemRepository.save(existingInventoryItem);
        log.info("Inventory item updated successfully with ID: {}", updatedInventoryItem.getId());
        
        return convertToResponseDTO(updatedInventoryItem);
    }
    
    @Override
    public void delete(String id) {
        log.info("Deleting inventory item with ID: {}", id);
        
        InventoryItem inventoryItem = inventoryItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory item", id));
        
        // Check if inventory item has stock records or movement history
        // For now, we'll just delete it - in a real system you'd check for references
        
        inventoryItemRepository.deleteById(id);
        log.info("Inventory item deleted successfully with ID: {}", id);
    }
    
    /**
     * Convert InventoryItem entity to InventoryItemResponseDTO.
     */
    private InventoryItemResponseDTO convertToResponseDTO(InventoryItem inventoryItem) {
        // Convert category
        CategoryResponseDTO categoryDTO = new CategoryResponseDTO(
            inventoryItem.getCategory().getId(),
            inventoryItem.getCategory().getName(),
            inventoryItem.getCategory().getBranchId(),
            inventoryItem.getCategory().getCreatedAt(),
            inventoryItem.getCategory().getUpdatedAt()
        );
        
        // Convert unit
        UnitResponseDTO unitDTO = new UnitResponseDTO(
            inventoryItem.getUnit().getId(),
            inventoryItem.getUnit().getName(),
            inventoryItem.getUnit().getSymbol(),
            inventoryItem.getUnit().getCreatedAt(),
            inventoryItem.getUnit().getUpdatedAt()
        );
        
        return new InventoryItemResponseDTO(
            inventoryItem.getId(),                    // id
            inventoryItem.getName(),                  // name
            inventoryItem.getCategory().getId(),      // inventoryItemCategoryId
            inventoryItem.getUnit().getId(),          // unitId
            inventoryItem.getThresholdQuantity(),     // thresholdQuantity
            inventoryItem.getReorderQuantity(),       // reorderQuantity
            inventoryItem.getCreatedAt(),             // createdAt
            inventoryItem.getUpdatedAt(),             // updatedAt
            null, unitDTO                                   // unit
        );
    }
    
    /**
     * Map API sort field names to entity field names.
     */
    private String mapSortField(String sortField) {
        return switch (sortField) {
            case "created_at" -> "createdAt";
            case "updated_at" -> "updatedAt";
            default -> sortField;
        };
    }
}