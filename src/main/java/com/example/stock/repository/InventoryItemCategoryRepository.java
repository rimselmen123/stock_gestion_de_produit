package com.example.stock.repository;

import com.example.stock.entity.InventoryItemCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
     * Find categories by name containing the specified text (case-insensitive) with pagination.
     * 
     * @param name the search term for name field
     * @param pageable pagination parameters
     * @return Page of matching InventoryItemCategory entities
     */
    @Query("SELECT c FROM InventoryItemCategory c WHERE " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<InventoryItemCategory> findByNameContainingIgnoreCase(
        @Param("name") String name, 
        Pageable pageable);
}