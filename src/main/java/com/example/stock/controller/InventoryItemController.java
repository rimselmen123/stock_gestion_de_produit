package com.example.stock.controller;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.inventoryitem.InventoryItemCreateDTO;
import com.example.stock.dto.inventoryitem.InventoryItemResponseDTO;
import com.example.stock.dto.inventoryitem.InventoryItemUpdateDTO;
import com.example.stock.service.InventoryItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    /**
     * Get all inventory items with advanced filtering and pagination.
     * 
     * @param search Search term for name field
     * @param name Filter by name (contains)
     * @param branchId Filter by branch ID
     * @param departmentId Filter by department ID
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
    @GetMapping
    @Operation(summary = "Get all inventory items with advanced filtering and pagination")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved inventory items")
    @ApiResponse(responseCode = "400", description = "Invalid parameters provided")
    public ResponseEntity<PaginatedResponse<InventoryItemResponseDTO>> getAllInventoryItems(
            @Parameter(description = "Search term for name field")
            @RequestParam(required = false) String search,
            
            @Parameter(description = "Filter by name (contains)")
            @RequestParam(required = false) String name,
            
            @Parameter(description = "Filter by branch ID")
            @RequestParam(name = "branch_id", required = false) String branchId,
            
            @Parameter(description = "Filter by department ID")
            @RequestParam(name = "department_id", required = false) String departmentId,
            
            @Parameter(description = "Filter by category ID")
            @RequestParam(name = "category_id", required = false) String categoryId,
            
            @Parameter(description = "Filter by unit ID")
            @RequestParam(name = "unit_id", required = false) String unitId,
            
            @Parameter(description = "Filter by minimum threshold quantity")
            @RequestParam(name = "min_threshold", required = false) Integer minThreshold,
            
            @Parameter(description = "Filter by maximum threshold quantity")
            @RequestParam(name = "max_threshold", required = false) Integer maxThreshold,
            
            @Parameter(description = "Filter by minimum reorder quantity")
            @RequestParam(name = "min_reorder", required = false) Integer minReorder,
            
            @Parameter(description = "Filter by maximum reorder quantity")
            @RequestParam(name = "max_reorder", required = false) Integer maxReorder,
            
            @Parameter(description = "Filter created_at from (ISO-8601)")
            @RequestParam(name = "created_from", required = false) String createdFrom,
            
            @Parameter(description = "Filter created_at to (ISO-8601)")
            @RequestParam(name = "created_to", required = false) String createdTo,
            
            @Parameter(description = "Filter updated_at from (ISO-8601)")
            @RequestParam(name = "updated_from", required = false) String updatedFrom,
            
            @Parameter(description = "Filter updated_at to (ISO-8601)")
            @RequestParam(name = "updated_to", required = false) String updatedTo,
            
            @Parameter(description = "Page number (1-based)", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "5") int perPage,
            @RequestParam(name = "sort_field", defaultValue = "created_at") String sortField,
            @RequestParam(name = "sort_direction", defaultValue = "desc") String sortDirection) {
        
        log.debug("""
                Getting inventory items with filters - 
                search: {}, name: {}, branchId: {}, departmentId: {}, categoryId: {}, unitId: {}, 
                minThreshold: {}, maxThreshold: {}, minReorder: {}, maxReorder: {},
                createdFrom: {}, createdTo: {}, updatedFrom: {}, updatedTo: {},
                page: {}, perPage: {}, sortField: {}, sortDirection: {}""",
                search, name, branchId, departmentId, categoryId, unitId, minThreshold, maxThreshold, minReorder, maxReorder,
                createdFrom, createdTo, updatedFrom, updatedTo, page, perPage, sortField, sortDirection);
        
        PaginatedResponse<InventoryItemResponseDTO> response = inventoryItemService.findAllWithFilters(
                search, name, branchId, departmentId, categoryId, unitId, 
                minThreshold, maxThreshold, minReorder, maxReorder,
                createdFrom, createdTo, updatedFrom, updatedTo,
                page, perPage, sortField, sortDirection);
        
        response.setMessage("Inventory items retrieved successfully");
        response.setSuccess(true);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific inventory item by ID.
     * 
     * @param id Inventory item ID
     * @return Inventory item data or 404 if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a specific inventory item by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved inventory item")
    @ApiResponse(responseCode = "404", description = "Inventory item not found")
    public ResponseEntity<InventoryItemResponseDTO> getInventoryItemById(
            @Parameter(description = "Inventory item ID") @PathVariable String id) {
        
        log.debug("Getting inventory item with ID: {}", id);
        
        InventoryItemResponseDTO inventoryItem = inventoryItemService.findByIdOrThrow(id);
        return ResponseEntity.ok(inventoryItem);
    }

    /**
     * Create a new inventory item.
     * 
     * @param createDTO Inventory item creation data
     * @return Created inventory item data
     */
    @PostMapping
    @Operation(summary = "Create a new inventory item")
    @ApiResponse(responseCode = "201", description = "Inventory item created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<InventoryItemResponseDTO> createInventoryItem(
            @Valid @RequestBody InventoryItemCreateDTO createDTO) {
        
        log.info("Creating new inventory item with name: {}", createDTO.getName());
        
        InventoryItemResponseDTO createdInventoryItem = inventoryItemService.create(createDTO);
        return new ResponseEntity<>(createdInventoryItem, HttpStatus.CREATED);
    }

    /**
     * Update an existing inventory item.
     * 
     * @param id Inventory item ID to update
     * @param updateDTO Inventory item update data
     * @return Updated inventory item data
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing inventory item")
    @ApiResponse(responseCode = "200", description = "Inventory item updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Inventory item not found")
    public ResponseEntity<InventoryItemResponseDTO> updateInventoryItem(
            @Parameter(description = "Inventory item ID to update") @PathVariable String id,
            @Valid @RequestBody InventoryItemUpdateDTO updateDTO) {
        
        log.info("Updating inventory item with ID: {}", id);
        
        InventoryItemResponseDTO updatedInventoryItem = inventoryItemService.update(id, updateDTO);
        return ResponseEntity.ok(updatedInventoryItem);
    }

    /**
     * Delete an inventory item by ID.
     * 
     * @param id Inventory item ID to delete
     * @return Success message or error if inventory item is in use
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an inventory item by ID")
    @ApiResponse(responseCode = "200", description = "Inventory item deleted successfully")
    @ApiResponse(responseCode = "404", description = "Inventory item not found")
    public ResponseEntity<Void> deleteInventoryItem(
            @Parameter(description = "Inventory item ID to delete") @PathVariable String id) {
        
        log.info("Deleting inventory item with ID: {}", id);
        
        inventoryItemService.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Get all inventory items in a specific branch.
     * 
     * @param branchId Branch ID
     * @param page Page number (1-based)
     * @param perPage Items per page
     * @return Paginated response with inventory items in the branch
     */
    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get all inventory items in a specific branch")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved inventory items for branch")
    @ApiResponse(responseCode = "400", description = "Invalid parameters provided")
    public ResponseEntity<PaginatedResponse<InventoryItemResponseDTO>> getInventoryItemsByBranch(
            @Parameter(description = "Branch ID") @PathVariable String branchId,
            @Parameter(description = "Page number (1-based)", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "10") int perPage) {
        
        log.debug("Getting inventory items for branch ID: {}, page: {}, perPage: {}", branchId, page, perPage);
        
        PaginatedResponse<InventoryItemResponseDTO> response = 
                inventoryItemService.findByBranchId(branchId, page, perPage);
        
        response.setMessage("Inventory items for branch retrieved successfully");
        response.setSuccess(true);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get all inventory items in a specific department.
     * 
     * @param departmentId Department ID
     * @param page Page number (1-based)
     * @param perPage Items per page
     * @return Paginated response with inventory items in the department
     */
    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get all inventory items in a specific department")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved inventory items for department")
    @ApiResponse(responseCode = "400", description = "Invalid parameters provided")
    public ResponseEntity<PaginatedResponse<InventoryItemResponseDTO>> getInventoryItemsByDepartment(
            @Parameter(description = "Department ID") @PathVariable String departmentId,
            @Parameter(description = "Page number (1-based)", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "10") int perPage) {
        
        log.debug("Getting inventory items for department ID: {}, page: {}, perPage: {}", departmentId, page, perPage);
        
        PaginatedResponse<InventoryItemResponseDTO> response = 
                inventoryItemService.findByDepartmentId(departmentId, page, perPage);
        
        response.setMessage("Inventory items for department retrieved successfully");
        response.setSuccess(true);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get all inventory items in a specific branch and department.
     * 
     * @param branchId Branch ID
     * @param departmentId Department ID
     * @param page Page number (1-based)
     * @param perPage Items per page
     * @return Paginated response with inventory items in the branch and department
     */
    @GetMapping("/branch/{branchId}/department/{departmentId}")
    @Operation(summary = "Get all inventory items in a specific branch and department")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved inventory items for branch and department")
    @ApiResponse(responseCode = "400", description = "Invalid parameters provided")
    public ResponseEntity<PaginatedResponse<InventoryItemResponseDTO>> getInventoryItemsByBranchAndDepartment(
            @Parameter(description = "Branch ID") @PathVariable String branchId,
            @Parameter(description = "Department ID") @PathVariable String departmentId,
            @Parameter(description = "Page number (1-based)", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "10") int perPage) {
        
        log.debug("Getting inventory items for branch ID: {} and department ID: {}, page: {}, perPage: {}", 
                branchId, departmentId, page, perPage);
        
        PaginatedResponse<InventoryItemResponseDTO> response = 
                inventoryItemService.findByBranchIdAndDepartmentId(branchId, departmentId, page, perPage);
        
        response.setMessage("Inventory items for branch and department retrieved successfully");
        response.setSuccess(true);
        
        return ResponseEntity.ok(response);
    }
}