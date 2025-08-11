package com.example.stock.repository;

import com.example.stock.entity.InventoryItem;
import com.example.stock.entity.InventoryItemCategory;

import com.example.stock.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
     * Find inventory items by category.
     * 
     * @param category the InventoryItemCategory to search for
     * @return list of InventoryItem entities in the specified category
     */
    List<InventoryItem> findByCategory(InventoryItemCategory category);
    
    /**
     * Find inventory items by category ID.
     * 
     * @param categoryId the category ID to search for
     * @return list of InventoryItem entities in the specified category
     */
    @Query("SELECT i FROM InventoryItem i WHERE i.category.id = :categoryId")
    List<InventoryItem> findByCategoryId(@Param("categoryId") String categoryId);
    

    
    /**
     * Find inventory items by unit.
     * 
     * @param unit the Unit to search for
     * @return list of InventoryItem entities with the specified unit
     */
    List<InventoryItem> findByUnit(Unit unit);
    
    /**
     * Find inventory items by name containing the specified text (case-insensitive).
     * 
     * @param name the partial name to search for
     * @return list of matching InventoryItem entities
     */
    List<InventoryItem> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find inventory items with unit purchase price between specified range.
     * 
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return list of InventoryItem entities within the price range
     */
    List<InventoryItem> findByUnitPurchasePriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Find inventory items with threshold quantity less than or equal to specified value.
     * These items might need reordering soon.
     * 
     * @param threshold the threshold quantity
     * @return list of InventoryItem entities at or below threshold
     */
    @Query("SELECT i FROM InventoryItem i WHERE i.thresholdQuantity <= :threshold")
    List<InventoryItem> findItemsNearThreshold(@Param("threshold") int threshold);
    

    
    /**
     * Find an inventory item by name (exact match, case-sensitive).
     * 
     * @param name the exact item name
     * @return Optional containing the InventoryItem if found
     */
    Optional<InventoryItem> findByName(String name);
    
    /**
     * Count inventory items by category.
     * 
     * @param category the InventoryItemCategory
     * @return number of items in the specified category
     */
    long countByCategory(InventoryItemCategory category);
    
    /**
     * Find all inventory items ordered by name.
     * 
     * @return list of all InventoryItem entities ordered by name
     */
    @Query("SELECT i FROM InventoryItem i ORDER BY i.name ASC")
    List<InventoryItem> findAllOrderByName();
    
    /**
     * Find inventory items by category ID with pagination support.
     * Useful for large datasets.
     * 
     * @param categoryId the category ID
     * @return list of InventoryItem entities in the specified category
     */
    @Query("SELECT i FROM InventoryItem i JOIN i.category c WHERE c.id = :categoryId ORDER BY i.name ASC")
    List<InventoryItem> findByCategoryIdOrderByName(@Param("categoryId") String categoryId);
}