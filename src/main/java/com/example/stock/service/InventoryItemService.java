package com.example.stock.service;

import com.example.stock.entity.InventoryItem;
import com.example.stock.entity.InventoryItemCategory;
import com.example.stock.entity.Brand;
import com.example.stock.entity.Unit;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for InventoryItem entity operations.
 * Defines business logic operations for Inventory Item management.
 * 
 * @author Generated
 * @since 1.0
 */
public interface InventoryItemService {
    
    /**
     * Create a new inventory item.
     * 
     * @param item the inventory item entity to create
     * @return the created inventory item
     * @throws IllegalArgumentException if item data is invalid
     */
    InventoryItem createItem(InventoryItem item);
    
    /**
     * Update an existing inventory item.
     * 
     * @param id the item ID
     * @param item the updated item data
     * @return the updated inventory item
     * @throws RuntimeException if item not found
     */
    InventoryItem updateItem(String id, InventoryItem item);
    
    /**
     * Find inventory item by ID.
     * 
     * @param id the item ID
     * @return Optional containing the item if found
     */
    Optional<InventoryItem> findById(String id);
    
    /**
     * Find inventory item by name.
     * 
     * @param name the item name
     * @return Optional containing the item if found
     */
    Optional<InventoryItem> findByName(String name);
    
    /**
     * Find inventory items by category.
     * 
     * @param categoryId the category ID
     * @return list of items in the category
     */
    List<InventoryItem> findByCategoryId(String categoryId);
    
    /**
     * Find inventory items by brand.
     * 
     * @param brand the brand entity
     * @return list of items of the brand
     */
    List<InventoryItem> findByBrand(Brand brand);
    
    /**
     * Find inventory items by unit.
     * 
     * @param unit the unit entity
     * @return list of items with the unit
     */
    List<InventoryItem> findByUnit(Unit unit);
    
    /**
     * Search inventory items by name.
     * 
     * @param name the partial name to search
     * @return list of matching items
     */
    List<InventoryItem> searchByName(String name);
    
    /**
     * Find items by price range.
     * 
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return list of items within price range
     */
    List<InventoryItem> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Find items near threshold (low stock items).
     * 
     * @param threshold the threshold quantity
     * @return list of items at or below threshold
     */
    List<InventoryItem> findItemsNearThreshold(int threshold);
    
    /**
     * Get all inventory items ordered by name.
     * 
     * @return list of all items ordered by name
     */
    List<InventoryItem> findAllOrderByName();
    
    /**
     * Delete inventory item by ID.
     * 
     * @param id the item ID to delete
     * @throws RuntimeException if item not found
     */
    void deleteById(String id);
    
    /**
     * Count items by category.
     * 
     * @param category the category entity
     * @return number of items in the category
     */
    long countByCategory(InventoryItemCategory category);
}
