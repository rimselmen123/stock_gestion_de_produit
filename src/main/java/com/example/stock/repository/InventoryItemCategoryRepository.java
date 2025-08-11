package com.example.stock.repository;

import com.example.stock.entity.InventoryItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for InventoryItemCategory entity operations.
 * Provides CRUD operations and custom query methods for InventoryItemCategory entities.
 * 
 * @author Generated
 * @since 1.0
 */
@Repository
public interface InventoryItemCategoryRepository extends JpaRepository<InventoryItemCategory, String> {
    
    /**
     * Find categories by branch ID.
     * 
     * @param branchId the branch ID to search for
     * @return list of InventoryItemCategory entities for the specified branch
     */
    List<InventoryItemCategory> findByBranchId(String branchId);
    
    /**
     * Find a category by name and branch ID.
     * 
     * @param name the category name
     * @param branchId the branch ID
     * @return Optional containing the InventoryItemCategory if found
     */
    Optional<InventoryItemCategory> findByNameAndBranchId(String name, String branchId);
    
    /**
     * Find categories by name containing the specified text (case-insensitive) for a specific branch.
     * 
     * @param name the partial name to search for
     * @param branchId the branch ID
     * @return list of matching InventoryItemCategory entities
     */
    List<InventoryItemCategory> findByNameContainingIgnoreCaseAndBranchId(String name, String branchId);
    
    /**
     * Check if a category exists by name and branch ID.
     * 
     * @param name the category name
     * @param branchId the branch ID
     * @return true if category exists, false otherwise
     */
    boolean existsByNameAndBranchId(String name, String branchId);
    
    /**
     * Count categories by branch ID.
     * 
     * @param branchId the branch ID
     * @return number of categories in the specified branch
     */
    long countByBranchId(String branchId);
    
    /**
     * Find all categories for a branch ordered by name.
     * 
     * @param branchId the branch ID
     * @return list of InventoryItemCategory entities ordered by name
     */
    @Query("SELECT c FROM InventoryItemCategory c WHERE c.branchId = :branchId ORDER BY c.name ASC")
    List<InventoryItemCategory> findByBranchIdOrderByName(@Param("branchId") String branchId);
}