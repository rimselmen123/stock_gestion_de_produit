package com.example.stock.service.impl;

import com.example.stock.entity.InventoryItemCategory;
import com.example.stock.repository.InventoryItemCategoryRepository;
import com.example.stock.service.InventoryItemCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of InventoryItemCategoryService interface.
 * Handles all business logic for InventoryItemCategory entity operations.
 * 
 * @author Generated
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryItemCategoryServiceImpl implements InventoryItemCategoryService {
    
    private final InventoryItemCategoryRepository categoryRepository;
    
    @Override
    public InventoryItemCategory createCategory(InventoryItemCategory category) {
        log.info("Creating new category with name: {} for branch: {}", 
                category.getName(), category.getBranchId());
        
        validateCategoryData(category);
        
        if (existsByNameAndBranchId(category.getName(), category.getBranchId())) {
            throw new IllegalArgumentException("Category with name '" + category.getName() + 
                "' already exists in branch '" + category.getBranchId() + "'");
        }
        
        category.setId(UUID.randomUUID().toString());
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        
        InventoryItemCategory savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with ID: {}", savedCategory.getId());
        
        return savedCategory;
    }
    
    @Override
    public InventoryItemCategory updateCategory(String id, InventoryItemCategory category) {
        log.info("Updating category with ID: {}", id);
        
        InventoryItemCategory existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
        
        validateCategoryData(category);
        
        // Check if name is being changed and if new name already exists in the same branch
        if (!existingCategory.getName().equals(category.getName()) && 
            existsByNameAndBranchId(category.getName(), existingCategory.getBranchId())) {
            throw new IllegalArgumentException("Category with name '" + category.getName() + 
                "' already exists in branch '" + existingCategory.getBranchId() + "'");
        }
        
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        existingCategory.setUpdatedAt(LocalDateTime.now());
        
        InventoryItemCategory updatedCategory = categoryRepository.save(existingCategory);
        log.info("Category updated successfully with ID: {}", updatedCategory.getId());
        
        return updatedCategory;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<InventoryItemCategory> findById(String id) {
        log.debug("Finding category by ID: {}", id);
        return categoryRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InventoryItemCategory> findByBranchId(String branchId) {
        log.debug("Finding categories by branch ID: {}", branchId);
        return categoryRepository.findByBranchIdOrderByName(branchId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<InventoryItemCategory> findByNameAndBranchId(String name, String branchId) {
        log.debug("Finding category by name: {} and branch ID: {}", name, branchId);
        return categoryRepository.findByNameAndBranchId(name, branchId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InventoryItemCategory> searchByNameAndBranchId(String name, String branchId) {
        log.debug("Searching categories by name: {} and branch ID: {}", name, branchId);
        return categoryRepository.findByNameContainingIgnoreCaseAndBranchId(name, branchId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InventoryItemCategory> findAll() {
        log.debug("Finding all categories");
        return categoryRepository.findAll();
    }
    
    @Override
    public void deleteById(String id) {
        log.info("Deleting category with ID: {}", id);
        
        InventoryItemCategory category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
        
        // Check if category has associated inventory items
        if (category.getInventoryItems() != null && !category.getInventoryItems().isEmpty()) {
            throw new RuntimeException("Cannot delete category. It has associated inventory items.");
        }
        
        categoryRepository.deleteById(id);
        log.info("Category deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByBranchId(String branchId) {
        log.debug("Counting categories by branch ID: {}", branchId);
        return categoryRepository.countByBranchId(branchId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndBranchId(String name, String branchId) {
        return categoryRepository.existsByNameAndBranchId(name, branchId);
    }
    
    private void validateCategoryData(InventoryItemCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        
        if (!StringUtils.hasText(category.getName())) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        
        if (!StringUtils.hasText(category.getBranchId())) {
            throw new IllegalArgumentException("Branch ID cannot be empty");
        }
        
        if (category.getName().trim().length() < 2) {
            throw new IllegalArgumentException("Category name must be at least 2 characters long");
        }
    }
}
