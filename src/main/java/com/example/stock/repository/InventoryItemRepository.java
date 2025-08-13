package com.example.stock.repository;

import com.example.stock.entity.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for InventoryItem entity operations.
 * Provides CRUD operations and custom query methods for InventoryItem entities.
 * 
 * @author Generated
 * @since 1.0
 */
@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, String> {
    
    /**
     * Find inventory items by name containing the specified text (case-insensitive) with pagination.
     * 
     * @param name the search term for name field
     * @param pageable pagination parameters
     * @return Page of matching InventoryItem entities
     */
    @Query("SELECT i FROM InventoryItem i WHERE " +
           "LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<InventoryItem> findByNameContainingIgnoreCase(
        @Param("name") String name, 
        Pageable pageable);
}