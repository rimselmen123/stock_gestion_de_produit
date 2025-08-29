package com.example.stock.controller;

// import com.example.stock.dto.common.ApiResponse;
// import com.example.stock.dto.common.PaginatedResponse;
// import com.example.stock.dto.inventorymouvement.InventoryMovementCreateDTO;
// import com.example.stock.dto.inventorystock.InventoryStockCreateDTO;
// import com.example.stock.dto.inventorystock.InventoryStockResponseDTO;
// import com.example.stock.dto.inventorystock.InventoryStockUpdateDTO;
// import com.example.stock.service.InventoryStockService;
// import com.example.stock.service.InventoryMovementService;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.math.BigDecimal;
// import java.time.LocalDate;

// /**
//  * REST Controller for Inventory Stock management operations.
//  * Provides CRUD endpoints for inventory stock entities with proper validation and error handling.
//  * 
//  * @author Generated
//  * @since 1.0
//  */
// @RestController
// @RequestMapping("/api/inventory-stock")
// @RequiredArgsConstructor
// @CrossOrigin(origins = "*")
// @Slf4j
// public class InventoryStockController {

//     private final InventoryStockService inventoryStockService;
//     private final InventoryMovementService inventoryMovementService;

//     /**
//      * Get all inventory stocks with comprehensive filtering and pagination.
//      * 
//      * @param search Search term for item name, inventory item ID, or branch ID
//      * @param branchId Filter by branch ID
//      * @param inventoryItemId Filter by inventory item ID
//      * @param itemName Filter by item name (contains)
//      * @param minQuantity Filter by minimum quantity
//      * @param maxQuantity Filter by maximum quantity
//      * @param expirationAfter Filter expiration date after (YYYY-MM-DD)
//      * @param expirationBefore Filter expiration date before (YYYY-MM-DD)
//      * @param createdFrom Filter created_at from (ISO-8601)
//      * @param createdTo Filter created_at to (ISO-8601)
//      * @param page Page number (1-based)
//      * @param perPage Items per page
//      * @param sortField Field to sort by
//      * @param sortDirection Sort direction (asc/desc)
//      * @return Paginated response with inventory stocks
//      */
//     @GetMapping
//     @Operation(summary = "Get all inventory stocks with comprehensive filtering and pagination")
//     public ResponseEntity<PaginatedResponse<InventoryStockResponseDTO>> getAllInventoryStocks(
//             @Parameter(description = "Search term for item name, inventory item ID, or branch ID")
//             @RequestParam(required = false) String search,
            
//             @Parameter(description = "Filter by branch ID")
//             @RequestParam(name = "branch_id", required = false) String branchId,
            
//             @Parameter(description = "Filter by inventory item ID")
//             @RequestParam(name = "inventory_item_id", required = false) String inventoryItemId,
            
//             @Parameter(description = "Filter by item name (contains)")
//             @RequestParam(name = "item_name", required = false) String itemName,
            
//             @Parameter(description = "Filter by minimum quantity")
//             @RequestParam(name = "min_quantity", required = false) BigDecimal minQuantity,
            
//             @Parameter(description = "Filter by maximum quantity")
//             @RequestParam(name = "max_quantity", required = false) BigDecimal maxQuantity,
            
//             @Parameter(description = "Filter expiration date after (YYYY-MM-DD)")
//             @RequestParam(name = "expiration_after", required = false) String expirationAfter,
            
//             @Parameter(description = "Filter expiration date before (YYYY-MM-DD)")
//             @RequestParam(name = "expiration_before", required = false) String expirationBefore,
            
//             @Parameter(description = "Filter created_at from (ISO-8601)")
//             @RequestParam(name = "created_from", required = false) String createdFrom,
            
//             @Parameter(description = "Filter created_at to (ISO-8601)")
//             @RequestParam(name = "created_to", required = false) String createdTo,
            
//             @Parameter(description = "Page number (1-based)", example = "1")
//             @RequestParam(defaultValue = "1") int page,
            
//             @Parameter(description = "Items per page", example = "20")
//             @RequestParam(name = "per_page", defaultValue = "20") int perPage,
            
//             @Parameter(description = "Field to sort by")
//             @RequestParam(name = "sort_field", defaultValue = "created_at") String sortField,
            
//             @Parameter(description = "Sort direction (asc/desc)")
//             @RequestParam(name = "sort_direction", defaultValue = "desc") String sortDirection) {
        
//         log.debug("Getting inventory stocks with filters - " +
//                 "search: {}, branchId: {}, inventoryItemId: {}, itemName: {}, " +
//                 "minQuantity: {}, maxQuantity: {}, expirationAfter: {}, expirationBefore: {}, " +
//                 "createdFrom: {}, createdTo: {}, page: {}, perPage: {}, sortField: {}, sortDirection: {}",
//                 search, branchId, inventoryItemId, itemName, minQuantity, maxQuantity,
//                 expirationAfter, expirationBefore, createdFrom, createdTo,
//                 page, perPage, sortField, sortDirection);
        
//         // Validate date parameters format
//         try {
//             if (expirationAfter != null && !expirationAfter.trim().isEmpty()) {
//                 LocalDate.parse(expirationAfter.trim()); // Just validate format
//             }
//             if (expirationBefore != null && !expirationBefore.trim().isEmpty()) {
//                 LocalDate.parse(expirationBefore.trim()); // Just validate format
//             }
//         } catch (Exception e) {
//             log.warn("Invalid date format in expiration filters: {}", e.getMessage());
//             return ResponseEntity.badRequest().build();
//         }
        
//         // Convert string dates to LocalDate and handle nulls
//         LocalDate createdFromDate = createdFrom != null ? LocalDate.parse(createdFrom) : null;
//         LocalDate createdToDate = createdTo != null ? LocalDate.parse(createdTo) : null;
        
//         // Call service with all parameters
//         PaginatedResponse<InventoryStockResponseDTO> response = inventoryStockService.findAllWithFilters(
//                 search, // search
//                 branchId, // branchId
//                 inventoryItemId, // inventoryItemId
//                 minQuantity != null ? minQuantity.doubleValue() : null, // minQuantity
//                 maxQuantity != null ? maxQuantity.doubleValue() : null, // maxQuantity
//                 null, // minUnitPrice
//                 null, // maxUnitPrice
//                 null, // expiredOnly
//                 null, // expiringSoon
//                 createdFromDate, // createdFrom
//                 createdToDate, // createdTo
//                 null, // updatedFrom
//                 null, // updatedTo
//                 page, // page
//                 perPage, // size
//                 sortField, // sortField
//                 sortDirection // sortDirection
//         );
        
//         return ResponseEntity.ok(response);
//     }

//     /**
//      * Get a specific inventory stock by ID.
//      * 
//      * @param id Inventory stock ID
//      * @return Inventory stock data or 404 if not found
//      */
//     @GetMapping("/{id}")
//     @Operation(summary = "Get a specific inventory stock by ID")
//     public ResponseEntity<ApiResponse<InventoryStockResponseDTO>> getInventoryStockById(
//             @Parameter(description = "Inventory stock ID") @PathVariable String id) {
        
//         log.debug("Getting inventory stock with ID: {}", id);
        
//         InventoryStockResponseDTO inventoryStock = inventoryStockService.findByIdOrThrow(id);
//         return ResponseEntity.ok(ApiResponse.success(inventoryStock));
//     }

//     /**
//      * Create a new inventory stock.
//      * 
//      * @param createDTO Inventory stock creation data
//      * @return Created inventory stock data
//      */
//     @PostMapping
//     @Operation(summary = "Create a new inventory stock")
//     public ResponseEntity<ApiResponse<InventoryStockResponseDTO>> createInventoryStock(
//             @Valid @RequestBody InventoryStockCreateDTO createDTO) {
        
//         log.info("Creating new inventory stock for item: {} in branch: {}", 
//                 createDTO.getInventoryItemId(), createDTO.getBranchId());
        
//         // Validate input data
//         if (createDTO.getQuantity() != null && createDTO.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
//             log.warn("Invalid quantity provided: {}", createDTO.getQuantity());
//             return ResponseEntity.badRequest().build();
//         }
//         if (createDTO.getUnitPurchasePrice() != null && createDTO.getUnitPurchasePrice().compareTo(BigDecimal.ZERO) < 0) {
//             log.warn("Invalid unit purchase price provided: {}", createDTO.getUnitPurchasePrice());
//             return ResponseEntity.badRequest().build();
//         }
        
//         InventoryStockResponseDTO createdStock = inventoryStockService.create(createDTO);
//         return new ResponseEntity<>(ApiResponse.success(createdStock), HttpStatus.CREATED);
//     }

//     /**
//      * Update an existing inventory stock.
//      * 
//      * @param id Inventory stock ID to update
//      * @param updateDTO Inventory stock update data
//      * @return Updated inventory stock data
//      */
//     @PutMapping("/{id}")
//     @Operation(summary = "Update an existing inventory stock")
//     public ResponseEntity<ApiResponse<InventoryStockResponseDTO>> updateInventoryStock(
//             @Parameter(description = "Inventory stock ID to update") @PathVariable String id,
//             @Valid @RequestBody InventoryStockUpdateDTO updateDTO) {
        
//         log.info("Updating inventory stock with ID: {}", id);
        
//         // Validate input data
//         if (updateDTO.getQuantity() != null && updateDTO.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
//             log.warn("Invalid quantity provided for update: {}", updateDTO.getQuantity());
//             return ResponseEntity.badRequest().build();
//         }
//         if (updateDTO.getUnitPurchasePrice() != null && updateDTO.getUnitPurchasePrice().compareTo(BigDecimal.ZERO) < 0) {
//             log.warn("Invalid unit purchase price provided for update: {}", updateDTO.getUnitPurchasePrice());
//             return ResponseEntity.badRequest().build();
//         }
        
//         InventoryStockResponseDTO updatedStock = inventoryStockService.update(id, updateDTO);
//         return ResponseEntity.ok(ApiResponse.success(updatedStock));
//     }

//     /**
//      * Delete an inventory stock by ID.
//      * 
//      * @param id Inventory stock ID to delete
//      * @return Success response or error if stock is in use
//      */
//     @DeleteMapping("/{id}")
//     @Operation(summary = "Delete an inventory stock by ID")
//     public ResponseEntity<ApiResponse<String>> deleteInventoryStock(
//             @Parameter(description = "Inventory stock ID to delete") @PathVariable String id) {
        
//         log.info("Deleting inventory stock with ID: {}", id);
        
//         inventoryStockService.delete(id);
//         return ResponseEntity.ok(ApiResponse.success("Inventory stock deleted successfully"));
//     }

//     /**
//      * Create an inventory stock entry (movement + stock effect) in a single transactional call.
//      * Matches frontend contract: POST /api/inventory-stock/entries
//      * Returns the updated stock row for the source branch.
//      */
//     @PostMapping("/entries")
//     @Operation(summary = "Create an inventory stock entry (movement + stock update)")
//     public ResponseEntity<ApiResponse<InventoryStockResponseDTO>> createInventoryEntry(
//             @Valid @RequestBody InventoryMovementCreateDTO createDTO) {
//         log.info("Creating inventory entry for item {} in branch {} with type {} and qty {}",
//                 createDTO.getInventoryItemId(), createDTO.getBranchId(), createDTO.getTransactionType(), createDTO.getQuantity());
//         InventoryStockResponseDTO updated = inventoryMovementService.createEntry(createDTO);
//         return new ResponseEntity<>(ApiResponse.success(updated), HttpStatus.CREATED);
//     }
// }
