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
    PaginatedResponse<InventoryItemResponseDTO> findAllWithPagination(
        String search, int page, int perPage, String sortField, String sortDirection);
    
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