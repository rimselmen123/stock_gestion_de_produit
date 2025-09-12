package com.example.stock.service;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.inventoryitem.InventoryItemCreateDTO;
import com.example.stock.dto.inventoryitem.InventoryItemResponseDTO;
import com.example.stock.dto.inventoryitem.InventoryItemUpdateDTO;

/**
 * Service interface for InventoryItem entity operations.
 * Defines business logic operations for Inventory Item management.
 * 
 * @author Generated
 * @since 1.0
 */
public interface InventoryItemService {
    
    /**
     * Find all inventory items with pagination, search, and sorting.
     * 
     * @param search Search term for name field
     * @param page Page number (1-based)
     * @param perPage Items per page
     * @param sortField Field to sort by
     * @param sortDirection Sort direction (asc/desc)
     * @return Paginated response with inventory items
     */
    /**
     * Find all inventory items with advanced filtering and pagination.
     *
     * @param search Search term for name field
     * @param name Filter by name (contains)
     * @param categoryId Filter by category ID
     * @param unitId Filter by unit ID
     * @param minThreshold Filter by minimum threshold quantity
     * @param maxThreshold Filter by maximum threshold quantity
     * @param minReorder Filter by minimum reorder quantity
     * @param maxReorder Filter by maximum reorder quantity
     * @param createdFrom Filter created_at from (ISO-8601)
     * @param createdTo Filter created_at to (ISO-8601)
     * @param updatedFrom Filter updated_at from (ISO-8601)
     * @param updatedTo Filter updated_at to (ISO-8601)
     * @param page Page number (1-based)
     * @param perPage Items per page
     * @param sortField Field to sort by
     * @param sortDirection Sort direction (asc/desc)
     * @return Paginated response with inventory items
     */
    PaginatedResponse<InventoryItemResponseDTO> findAllWithFilters(
            String search, String name, String categoryId, String unitId,
            Integer minThreshold, Integer maxThreshold, Integer minReorder, Integer maxReorder,
            String createdFrom, String createdTo, String updatedFrom, String updatedTo,
            int page, int perPage, String sortField, String sortDirection);

    /**
     * Find all inventory items with pagination, search, and sorting.
     *
     * @param search Search term for name field
     * @param page Page number (1-based)
     * @param perPage Items per page
     * @param sortField Field to sort by
     * @param sortDirection Sort direction (asc/desc)
     * @return Paginated response with inventory items
     */

    /**
     * Search inventory items with advanced filtering options.
     * 
     * @param name Optional item name (partial match, case-insensitive)
     * @param unit Optional unit ID (exact match)
     * @param unitName Optional unit name (partial match, case-insensitive)
     * @param category Optional category ID (exact match)
     * @param categoryName Optional category name (partial match, case-insensitive)
     * @param page Page number (1-based)
     * @param size Number of items per page
     * @param sortBy Sort field (e.g., 'name', 'createdAt')
     * @param sortDirection Sort direction ('asc' or 'desc')
     * @return Paginated response with filtered items
     */
    PaginatedResponse<InventoryItemResponseDTO> searchItems(
            String name,
            String unit,
            String unitName,
            String category,
            String categoryName,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );
    
    /**
     * Find inventory item by ID or throw exception if not found.
     * 
     * @param id Inventory item ID
     * @return Inventory item response DTO
     * @throws ResourceNotFoundException if inventory item not found
     */
    InventoryItemResponseDTO findByIdOrThrow(String id);
    
    /**
     * Create a new inventory item.
     * 
     * @param createDTO Inventory item creation data
     * @return Created inventory item response DTO
     * @throws IllegalArgumentException if inventory item data is invalid
     * @throws ForeignKeyConstraintException if referenced category or unit doesn't exist
     */
    InventoryItemResponseDTO create(InventoryItemCreateDTO createDTO);
    
    /**
     * Update an existing inventory item.
     * 
     * @param id Inventory item ID
     * @param updateDTO Inventory item update data
     * @return Updated inventory item response DTO
     * @throws ResourceNotFoundException if inventory item not found
     * @throws ForeignKeyConstraintException if referenced category or unit doesn't exist
     */
    InventoryItemResponseDTO update(String id, InventoryItemUpdateDTO updateDTO);
    
    /**
     * Delete inventory item by ID.
     * 
     * @param id Inventory item ID to delete
     * @throws ResourceNotFoundException if inventory item not found
     * @throws DeleteConstraintException if inventory item has stock records or movement history
     */
    void delete(String id);
}