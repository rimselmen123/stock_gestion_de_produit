package com.example.stock.service;

// import com.example.stock.dto.common.PaginatedResponse;
// import com.example.stock.dto.inventorystock.InventoryStockCreateDTO;
// import com.example.stock.dto.inventorystock.InventoryStockResponseDTO;
// import com.example.stock.dto.inventorystock.InventoryStockUpdateDTO;

// import java.time.LocalDate;

// /**
//  * Service interface for InventoryStock entity operations.
//  * Defines business logic operations for Inventory Stock management.
//  * 
//  * @author Generated
//  * @since 1.0
//  */
// public interface InventoryStockService {
    
//     /**
//      * Find all inventory stocks with advanced filtering and pagination.
//      *
//      * @param search Search term for general search across multiple fields
//      * @param branchId Filter by branch ID
//      * @param inventoryItemId Filter by inventory item ID
//      * @param minQuantity Filter by minimum quantity
//      * @param maxQuantity Filter by maximum quantity
//      * @param minUnitPrice Filter by minimum unit price
//      * @param maxUnitPrice Filter by maximum unit price
//      * @param expiredOnly Filter to show only expired items
//      * @param expiringSoon Filter to show items expiring soon (within 30 days)
//      * @param createdFrom Filter created_at from (ISO-8601)
//      * @param createdTo Filter created_at to (ISO-8601)
//      * @param updatedFrom Filter updated_at from (ISO-8601)
//      * @param updatedTo Filter updated_at to (ISO-8601)
//      * @param page Page number (1-based)
//      * @param size Number of items per page
//      * @param sortField Field to sort by
//      * @param sortDirection Sort direction (asc/desc)
//      * @return Paginated response with inventory stocks
//      */
//     PaginatedResponse<InventoryStockResponseDTO> findAllWithFilters(
//             String search,
//             String branchId,
//             String inventoryItemId,
//             Double minQuantity,
//             Double maxQuantity,
//             Double minUnitPrice,
//             Double maxUnitPrice,
//             Boolean expiredOnly,
//             Boolean expiringSoon,
//             LocalDate createdFrom,
//             LocalDate createdTo,
//             LocalDate updatedFrom,
//             LocalDate updatedTo,
//             int page,
//             int size,
//             String sortField,
//             String sortDirection
//     );
    
//     /**
//      * Get a specific inventory stock by ID.
//      *
//      * @param id The ID of the inventory stock to retrieve
//      * @return The inventory stock DTO if found
//      * @throws com.example.stock.exception.ResourceNotFoundException if inventory stock not found
//      */
//     InventoryStockResponseDTO findById(String id);
    
//     /**
//      * Get a specific inventory stock by ID or throw an exception if not found.
//      *
//      * @param id The ID of the inventory stock to retrieve
//      * @return The inventory stock DTO if found
//      * @throws com.example.stock.exception.ResourceNotFoundException if inventory stock not found
//      */
//     InventoryStockResponseDTO findByIdOrThrow(String id);
    
//     /**
//      * Create a new inventory stock.
//      *
//      * @param createDTO The DTO containing inventory stock data
//      * @return The created inventory stock DTO
//      */
//     InventoryStockResponseDTO create(InventoryStockCreateDTO createDTO);
    
//     /**
//      * Update an existing inventory stock.
//      *
//      * @param id The ID of the inventory stock to update
//      * @param updateDTO The DTO containing updated inventory stock data
//      * @return The updated inventory stock DTO
//      * @throws com.example.stock.exception.ResourceNotFoundException if inventory stock not found
//      */
//     InventoryStockResponseDTO update(String id, InventoryStockUpdateDTO updateDTO);
    
//     /**
//      * Delete an inventory stock by ID.
//      *
//      * @param id The ID of the inventory stock to delete
//      * @throws com.example.stock.exception.ResourceNotFoundException if inventory stock not found
//      * @throws com.example.stock.exception.OperationNotAllowedException if the stock has associated movements
//      */
//     void delete(String id);
    
//     /**
//      * Get the current stock level for a specific item in a branch.
//      *
//      * @param itemId The ID of the inventory item
//      * @param branchId The ID of the branch
//      * @return The current stock quantity, or 0 if no stock record exists
//      */
//     double getCurrentStockLevel(String itemId, String branchId);
    
//     /**
//      * Find low stock items for a specific branch with pagination.
//      * Low stock items are those with quantity less than or equal to the threshold.
//      *
//      * @param branchId The ID of the branch to check for low stock items
//      * @param page The page number (1-based)
//      * @param size The number of items per page
//      * @return Paginated response with low stock items
//      */
//     PaginatedResponse<InventoryStockResponseDTO> findLowStockItems(String branchId, int page, int size);
    
//     /**
//      * Check if an item is below its threshold level in a specific branch.
//      *
//      * @param itemId The ID of the inventory item
//      * @param branchId The ID of the branch
//      * @return true if the stock is below the threshold, false otherwise
//      */
//     boolean isBelowThreshold(String itemId, String branchId);
   
// }
