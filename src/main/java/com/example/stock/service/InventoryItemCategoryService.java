package com.example.stock.service;

import com.example.stock.entity.InventoryItemCategory;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for InventoryItemCategory entity operations.
 * Defines business logic operations for Category management.
 * 
 * @author Generated
 * @since 1.0
 */
public interface InventoryItemCategoryService {
    
    /**
     * Create a new category.
     * 
     * @param category the category entity to create
     * @return the created category
     * @throws IllegalArgumentException if category data is invalid
     */
    InventoryItemCategory createCategory(InventoryItemCategory category);
    
    /**
     * Update an existing category.
     * 
     * @param id the category ID
     * @param category the updated category data
     * @return the updated category
     * @throws RuntimeException if category not found
     */
    InventoryItemCategory updateCategory(String id, InventoryItemCategory category);
    
    /**
     * Find category by ID.
     * 
     * @param id the category ID
     * @return Optional containing the category if found
     */
    Optional<InventoryItemCategory> findById(String id);
    
    /**
     * Find categories by branch ID.
     * 
     * @param branchId the branch ID
     * @return list of categories for the branch
     */
    List<InventoryItemCategory> findByBranchId(String branchId);
    
    /**
     * Find category by name and branch ID.
     * 
     * @param name the category name
     * @param branchId the branch ID
     * @return Optional containing the category if found
     */
    Optional<InventoryItemCategory> findByNameAndBranchId(String name, String branchId);
    
    /**
     * Search categories by name for a specific branch.
     * 
     * @param name the partial name to search
     * @param branchId the branch ID
     * @return list of matching categories
     */
    List<InventoryItemCategory> searchByNameAndBranchId(String name, String branchId);
    
    /**
     * Get all categories ordered by name.
     * 
     * @return list of all categories ordered by name
     */
    List<InventoryItemCategory> findAll();
    
    /**
     * Delete category by ID.
     * 
     * @param id the category ID to delete
     * @throws RuntimeException if category not found or has dependencies
     */
    void deleteById(String id);
    
    /**
     * Count categories by branch.
     * 
     * @param branchId the branch ID
     * @return number of categories in the branch
     */
    long countByBranchId(String branchId);
    
    /**
     * Check if category exists by name and branch.
     * 
     * @param name the category name
     * @param branchId the branch ID
     * @return true if exists, false otherwise
     */
    boolean existsByNameAndBranchId(String name, String branchId);
}