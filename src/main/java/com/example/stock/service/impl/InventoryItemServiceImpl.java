package com.example.stock.service.impl;

import com.example.stock.entity.InventoryItem;
import com.example.stock.entity.InventoryItemCategory;

import com.example.stock.entity.Unit;
import com.example.stock.repository.InventoryItemRepository;
import com.example.stock.service.InventoryItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of InventoryItemService interface.
 * Handles all business logic for InventoryItem entity operations.
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
    
    @Override
    public InventoryItem createItem(InventoryItem item) {
        log.info("Creating new inventory item with name: {}", item.getName());
        
        validateInventoryItemData(item);
        
        // Check if item name already exists
        if (inventoryItemRepository.findByName(item.getName()).isPresent()) {
            throw new IllegalArgumentException("Inventory item with name '" + item.getName() + "' already exists");
        }
        
        item.setId(UUID.randomUUID().toString());
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        
        InventoryItem savedItem = inventoryItemRepository.save(item);
        log.info("Inventory item created successfully with ID: {}", savedItem.getId());
        
        return savedItem;
    }
    
    @Override
    public InventoryItem updateItem(String id, InventoryItem item) {
        log.info("Updating inventory item with ID: {}", id);
        
        InventoryItem existingItem = inventoryItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inventory item not found with ID: " + id));
        
        validateInventoryItemData(item);
        
        // Check if name is being changed and if new name already exists
        Optional<InventoryItem> itemWithSameName = inventoryItemRepository.findByName(item.getName());
        if (itemWithSameName.isPresent() && !itemWithSameName.get().getId().equals(id)) {
            throw new IllegalArgumentException("Inventory item with name '" + item.getName() + "' already exists");
        }
        
        existingItem.setName(item.getName());
        existingItem.setThresholdQuantity(item.getThresholdQuantity());
        existingItem.setReorderQuantity(item.getReorderQuantity());
        existingItem.setUnitPurchasePrice(item.getUnitPurchasePrice());
        existingItem.setCategory(item.getCategory());
        existingItem.setUnit(item.getUnit());
        existingItem.setUpdatedAt(LocalDateTime.now());
        
        InventoryItem updatedItem = inventoryItemRepository.save(existingItem);
        log.info("Inventory item updated successfully with ID: {}", updatedItem.getId());
        
        return updatedItem;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<InventoryItem> findById(String id) {
        log.debug("Finding inventory item by ID: {}", id);
        return inventoryItemRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<InventoryItem> findByName(String name) {
        log.debug("Finding inventory item by name: {}", name);
        return inventoryItemRepository.findByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InventoryItem> findByCategoryId(String categoryId) {
        log.debug("Finding inventory items by category ID: {}", categoryId);
        return inventoryItemRepository.findByCategoryId(categoryId);
    }
    

    
    @Override
    @Transactional(readOnly = true)
    public List<InventoryItem> findByUnit(Unit unit) {
        log.debug("Finding inventory items by unit: {}", unit != null ? unit.getName() : "null");
        return inventoryItemRepository.findByUnit(unit);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InventoryItem> searchByName(String name) {
        log.debug("Searching inventory items by name: {}", name);
        return inventoryItemRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InventoryItem> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.debug("Finding inventory items by price range: {} - {}", minPrice, maxPrice);
        
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        
        return inventoryItemRepository.findByUnitPurchasePriceBetween(minPrice, maxPrice);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InventoryItem> findItemsNearThreshold(int threshold) {
        log.debug("Finding inventory items near threshold: {}", threshold);
        
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold cannot be negative");
        }
        
        return inventoryItemRepository.findItemsNearThreshold(threshold);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InventoryItem> findAllOrderByName() {
        log.debug("Finding all inventory items ordered by name");
        return inventoryItemRepository.findAllOrderByName();
    }
    
    @Override
    public void deleteById(String id) {
        log.info("Deleting inventory item with ID: {}", id);
        
        if (!inventoryItemRepository.existsById(id)) {
            throw new RuntimeException("Inventory item not found with ID: " + id);
        }
        
        inventoryItemRepository.deleteById(id);
        log.info("Inventory item deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByCategory(InventoryItemCategory category) {
        log.debug("Counting inventory items by category: {}",
        category != null ? category.getName() : "null");
        return inventoryItemRepository.countByCategory(category);
    }
    
    /**
     * Validates inventory item data before creation or update.
     * 
     * @param item the inventory item to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateInventoryItemData(InventoryItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Inventory item cannot be null");
        }
        
        if (!StringUtils.hasText(item.getName())) {
            throw new IllegalArgumentException("Inventory item name cannot be empty");
        }
        
        if (item.getName().trim().length() < 2) {
            throw new IllegalArgumentException("Inventory item name must be at least 2 characters long");
        }
        
        if (item.getThresholdQuantity() < 0) {
            throw new IllegalArgumentException("Threshold quantity cannot be negative");
        }
        
        if (item.getReorderQuantity() < 0) {
            throw new IllegalArgumentException("Reorder quantity cannot be negative");
        }
        
        if (item.getUnitPurchasePrice() == null || item.getUnitPurchasePrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit purchase price cannot be null or negative");
        }
        
        if (item.getCategory() == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        
        if (item.getUnit() == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        
        // Brand can be null (optional)
        
        // Validate that reorder quantity is greater than threshold quantity
        if (item.getReorderQuantity() <= item.getThresholdQuantity()) {
            throw new IllegalArgumentException("Reorder quantity must be greater than threshold quantity");
        }
        
        // Validate price precision (15 digits total, 2 decimal places)
        if (item.getUnitPurchasePrice().scale() > 2) {
            throw new IllegalArgumentException("Unit purchase price cannot have more than 2 decimal places");
        }
        
        if (item.getUnitPurchasePrice().precision() > 15) {
            throw new IllegalArgumentException("Unit purchase price cannot have more than 15 digits in total");
        }
    }
}