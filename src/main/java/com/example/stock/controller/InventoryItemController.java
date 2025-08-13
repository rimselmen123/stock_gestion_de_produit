package com.example.stock.controller;

import com.example.stock.dto.common.ApiResponse;
import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.inventoryitem.InventoryItemCreateDTO;
import com.example.stock.dto.inventoryitem.InventoryItemResponseDTO;
import com.example.stock.dto.inventoryitem.InventoryItemUpdateDTO;
import com.example.stock.service.InventoryItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Inventory Item management operations.
 * Provides CRUD endpoints for inventory item entities with proper validation and error handling.
 * 
 * @author Generated
 * @since 1.0
 */
@RestController
@RequestMapping("/api/inventory-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    /**
     * Get all inventory items with filtering and pagination.
     * 
     * @param search Search term for name field
     * @param page Page number (1-based)
     * @param perPage Items per page
     * @param sortField Field to sort by
     * @param sortDirection Sort direction (asc/desc)
     * @return Paginated response with inventory items
     */
    @GetMapping
    public ResponseEntity<PaginatedResponse<InventoryItemResponseDTO>> getAllInventoryItems(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "5") int perPage,
            @RequestParam(name = "sort_field", defaultValue = "created_at") String sortField,
            @RequestParam(name = "sort_direction", defaultValue = "desc") String sortDirection) {
        
        log.debug("Getting inventory items with search: {}, page: {}, perPage: {}, sortField: {}, sortDirection: {}", 
                  search, page, perPage, sortField, sortDirection);
        
        PaginatedResponse<InventoryItemResponseDTO> response = inventoryItemService.findAllWithPagination(
            search, page, perPage, sortField, sortDirection);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific inventory item by ID.
     * 
     * @param id Inventory item ID
     * @return Inventory item data or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryItemResponseDTO>> getInventoryItemById(@PathVariable String id) {
        
        log.debug("Getting inventory item with ID: {}", id);
        
        InventoryItemResponseDTO inventoryItem = inventoryItemService.findByIdOrThrow(id);
        ApiResponse<InventoryItemResponseDTO> response = ApiResponse.success(inventoryItem);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new inventory item.
     * 
     * @param createDTO Inventory item creation data
     * @return Created inventory item data
     */
    @PostMapping
    public ResponseEntity<ApiResponse<InventoryItemResponseDTO>> createInventoryItem(
            @Valid @RequestBody InventoryItemCreateDTO createDTO) {
        
        log.info("Creating new inventory item with name: {}", createDTO.getName());
        
        InventoryItemResponseDTO createdInventoryItem = inventoryItemService.create(createDTO);
        ApiResponse<InventoryItemResponseDTO> response = ApiResponse.success(
            createdInventoryItem, "Inventory item created successfully");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing inventory item.
     * 
     * @param id Inventory item ID to update
     * @param updateDTO Inventory item update data
     * @return Updated inventory item data
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryItemResponseDTO>> updateInventoryItem(
            @PathVariable String id,
            @Valid @RequestBody InventoryItemUpdateDTO updateDTO) {
        
        log.info("Updating inventory item with ID: {}", id);
        
        InventoryItemResponseDTO updatedInventoryItem = inventoryItemService.update(id, updateDTO);
        ApiResponse<InventoryItemResponseDTO> response = ApiResponse.success(
            updatedInventoryItem, "Inventory item updated successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an inventory item by ID.
     * 
     * @param id Inventory item ID to delete
     * @return Success message or error if inventory item is in use
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInventoryItem(@PathVariable String id) {
        
        log.info("Deleting inventory item with ID: {}", id);
        
        inventoryItemService.delete(id);
        ApiResponse<Void> response = ApiResponse.success("Inventory item deleted successfully");
        
        return ResponseEntity.ok(response);
    }
}