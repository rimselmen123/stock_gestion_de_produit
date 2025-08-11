package com.example.stock.controller;

import com.example.stock.dto.inventoryitem.InventoryItemCreateDTO;
import com.example.stock.dto.inventoryitem.InventoryItemResponseDTO;
import com.example.stock.dto.inventoryitem.InventoryItemSummaryDTO;
import com.example.stock.dto.inventoryitem.InventoryItemUpdateDTO;
import com.example.stock.entity.InventoryItem;
import com.example.stock.entity.InventoryItemCategory;
import com.example.stock.entity.Brand;
import com.example.stock.entity.Unit;
import com.example.stock.mapper.InventoryItemMapper;
import com.example.stock.service.InventoryItemService;
import com.example.stock.service.InventoryItemCategoryService;
import com.example.stock.service.BrandService;
import com.example.stock.service.UnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for InventoryItem management operations.
 * Provides CRUD endpoints for inventory item entities with proper validation and error handling.
 * 
 * @author Generated
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/inventory-items")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Inventory Item Management", description = "APIs for managing inventory items")
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;
    private final InventoryItemCategoryService categoryService;
    private final BrandService brandService;
    private final UnitService unitService;
    private final InventoryItemMapper inventoryItemMapper;

    /**
     * Create a new inventory item.
     * 
     * @param createDTO the inventory item creation data
     * @return ResponseEntity with created inventory item data
     */
    @PostMapping
    @Operation(summary = "Create a new inventory item", description = "Creates a new inventory item with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Inventory item created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Inventory item with same name already exists")
    })
    public ResponseEntity<InventoryItemResponseDTO> createInventoryItem(
            @Valid @RequestBody InventoryItemCreateDTO createDTO) {
        
        log.info("Creating new inventory item with name: {}", createDTO.getName());
        
        // Validate reorder quantity > threshold quantity
        if (createDTO.getReorderQuantity() <= createDTO.getThresholdQuantity()) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            InventoryItem inventoryItemEntity = inventoryItemMapper.toEntity(createDTO);
            
            // Set relationships
            setEntityRelationships(inventoryItemEntity, createDTO.getCategoryId(), 
                                 createDTO.getUnitId(), createDTO.getBrandId());
            
            InventoryItem createdItem = inventoryItemService.createItem(inventoryItemEntity);
            InventoryItemResponseDTO responseDTO = inventoryItemMapper.toResponseDTO(createdItem);
            
            log.info("Inventory item created successfully with ID: {}", createdItem.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid data for inventory item creation: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Error creating inventory item: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Get all inventory items.
     * 
     * @return ResponseEntity with list of all inventory items
     */
    @GetMapping
    @Operation(summary = "Get all inventory items", description = "Retrieves a list of all inventory items")
    @ApiResponse(responseCode = "200", description = "Inventory items retrieved successfully")
    public ResponseEntity<List<InventoryItemResponseDTO>> getAllInventoryItems() {
        
        log.debug("Retrieving all inventory items");
        
        List<InventoryItem> items = inventoryItemService.findAllOrderByName();
        List<InventoryItemResponseDTO> responseDTOs = inventoryItemMapper.toResponseDTOList(items);
        
        log.debug("Retrieved {} inventory items", items.size());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Get inventory items summary for listings.
     *
     * @return ResponseEntity with list of inventory item summaries
     */
    @GetMapping("/summary")
    @Operation(summary = "Get inventory items summary", description = "Retrieves a lightweight list of inventory items for dropdowns and listings")
    @ApiResponse(responseCode = "200", description = "Inventory item summaries retrieved successfully")
    public ResponseEntity<List<InventoryItemSummaryDTO>> getInventoryItemsSummary() {

        log.debug("Retrieving inventory items summary");

        List<InventoryItem> items = inventoryItemService.findAllOrderByName();
        List<InventoryItemSummaryDTO> summaryDTOs = inventoryItemMapper.toSummaryDTOList(items);

        log.debug("Retrieved {} inventory item summaries", items.size());
        return ResponseEntity.ok(summaryDTOs);
    }

    /**
     * Get inventory item by ID.
     *
     * @param id the inventory item ID
     * @return ResponseEntity with inventory item data or 404 if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get inventory item by ID", description = "Retrieves a specific inventory item by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventory item found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Inventory item not found")
    })
    public ResponseEntity<InventoryItemResponseDTO> getInventoryItemById(
            @Parameter(description = "Inventory item ID", required = true)
            @PathVariable String id) {

        log.debug("Retrieving inventory item with ID: {}", id);

        Optional<InventoryItem> itemOptional = inventoryItemService.findById(id);

        if (itemOptional.isPresent()) {
            InventoryItemResponseDTO responseDTO = inventoryItemMapper.toResponseDTO(itemOptional.get());
            log.debug("Inventory item found with ID: {}", id);
            return ResponseEntity.ok(responseDTO);
        } else {
            log.warn("Inventory item not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get inventory items by category.
     *
     * @param categoryId the category ID
     * @return ResponseEntity with list of inventory items in the category
     */
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get inventory items by category", description = "Retrieves all inventory items in a specific category")
    @ApiResponse(responseCode = "200", description = "Inventory items retrieved successfully")
    public ResponseEntity<List<InventoryItemResponseDTO>> getInventoryItemsByCategory(
            @Parameter(description = "Category ID", required = true)
            @PathVariable String categoryId) {

        log.debug("Retrieving inventory items for category: {}", categoryId);

        List<InventoryItem> items = inventoryItemService.findByCategoryId(categoryId);
        List<InventoryItemResponseDTO> responseDTOs = inventoryItemMapper.toResponseDTOList(items);

        log.debug("Retrieved {} inventory items for category: {}", items.size(), categoryId);
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Update an existing inventory item.
     *
     * @param id the inventory item ID to update
     * @param updateDTO the inventory item update data
     * @return ResponseEntity with updated inventory item data
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update inventory item", description = "Updates an existing inventory item with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventory item updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Inventory item not found"),
        @ApiResponse(responseCode = "409", description = "Inventory item with same name already exists")
    })
    public ResponseEntity<InventoryItemResponseDTO> updateInventoryItem(
            @Parameter(description = "Inventory item ID", required = true)
            @PathVariable String id,
            @Valid @RequestBody InventoryItemUpdateDTO updateDTO) {

        log.info("Updating inventory item with ID: {}", id);

        // Validate reorder quantity > threshold quantity
        if (updateDTO.getReorderQuantity() <= updateDTO.getThresholdQuantity()) {
            return ResponseEntity.badRequest().build();
        }

        // Check if inventory item exists
        Optional<InventoryItem> existingItemOptional = inventoryItemService.findById(id);
        if (existingItemOptional.isEmpty()) {
            log.warn("Inventory item not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        try {
            InventoryItem itemToUpdate = inventoryItemMapper.toEntity(new InventoryItemCreateDTO(
                updateDTO.getName(),
                updateDTO.getThresholdQuantity(),
                updateDTO.getReorderQuantity(),
                updateDTO.getUnitPurchasePrice(),
                updateDTO.getCategoryId(),
                updateDTO.getUnitId(),
                updateDTO.getBrandId()
            ));

            // Set relationships
            setEntityRelationships(itemToUpdate, updateDTO.getCategoryId(),
                                 updateDTO.getUnitId(), updateDTO.getBrandId());

            InventoryItem updatedItem = inventoryItemService.updateItem(id, itemToUpdate);
            InventoryItemResponseDTO responseDTO = inventoryItemMapper.toResponseDTO(updatedItem);

            log.info("Inventory item updated successfully with ID: {}", id);
            return ResponseEntity.ok(responseDTO);

        } catch (IllegalArgumentException e) {
            log.warn("Invalid data for inventory item update: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Error updating inventory item: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Delete an inventory item by ID.
     *
     * @param id the inventory item ID to delete
     * @return ResponseEntity with no content or error status
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete inventory item", description = "Deletes an inventory item by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Inventory item deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Inventory item not found")
    })
    public ResponseEntity<Void> deleteInventoryItem(
            @Parameter(description = "Inventory item ID", required = true)
            @PathVariable String id) {

        log.info("Deleting inventory item with ID: {}", id);

        // Check if inventory item exists
        Optional<InventoryItem> itemOptional = inventoryItemService.findById(id);
        if (itemOptional.isEmpty()) {
            log.warn("Inventory item not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        try {
            inventoryItemService.deleteById(id);
            log.info("Inventory item deleted successfully with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting inventory item with ID: {}, Error: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Search inventory items by name.
     *
     * @param name the partial name to search for
     * @return ResponseEntity with matching inventory items
     */
    @GetMapping("/search")
    @Operation(summary = "Search inventory items by name", description = "Finds inventory items by partial name match")
    @ApiResponse(responseCode = "200", description = "Inventory items found")
    public ResponseEntity<List<InventoryItemResponseDTO>> searchInventoryItemsByName(
            @Parameter(description = "Partial name to search for", required = true)
            @RequestParam String name) {

        log.debug("Searching for inventory items with name: {}", name);

        List<InventoryItem> items = inventoryItemService.searchByName(name);
        List<InventoryItemResponseDTO> responseDTOs = inventoryItemMapper.toResponseDTOList(items);

        log.debug("Found {} inventory items matching name: {}", items.size(), name);
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Get inventory items by price range.
     *
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return ResponseEntity with inventory items in price range
     */
    @GetMapping("/price-range")
    @Operation(summary = "Get inventory items by price range", description = "Finds inventory items within a specific price range")
    @ApiResponse(responseCode = "200", description = "Inventory items found")
    public ResponseEntity<List<InventoryItemResponseDTO>> getInventoryItemsByPriceRange(
            @Parameter(description = "Minimum price (inclusive)", required = true)
            @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price (inclusive)", required = true)
            @RequestParam BigDecimal maxPrice) {

        log.debug("Searching for inventory items with price range: {} - {}", minPrice, maxPrice);

        List<InventoryItem> items = inventoryItemService.findByPriceRange(minPrice, maxPrice);
        List<InventoryItemResponseDTO> responseDTOs = inventoryItemMapper.toResponseDTOList(items);

        log.debug("Found {} inventory items in price range: {} - {}", items.size(), minPrice, maxPrice);
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Get inventory items near threshold (low stock).
     *
     * @param threshold the threshold quantity
     * @return ResponseEntity with low stock inventory items
     */
    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock inventory items", description = "Finds inventory items at or below threshold quantity")
    @ApiResponse(responseCode = "200", description = "Low stock inventory items found")
    public ResponseEntity<List<InventoryItemResponseDTO>> getLowStockInventoryItems(
            @Parameter(description = "Threshold quantity", required = false)
            @RequestParam(required = false, defaultValue = "10") Integer threshold) {

        log.debug("Searching for inventory items near threshold: {}", threshold);

        List<InventoryItem> items = inventoryItemService.findItemsNearThreshold(threshold);
        List<InventoryItemResponseDTO> responseDTOs = inventoryItemMapper.toResponseDTOList(items);

        log.debug("Found {} inventory items near threshold: {}", items.size(), threshold);
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Helper method to set entity relationships from IDs.
     *
     * @param item the inventory item entity
     * @param categoryId the category ID
     * @param unitId the unit ID
     * @param brandId the brand ID (optional)
     */
    private void setEntityRelationships(InventoryItem item, String categoryId, String unitId, String brandId) {
        // Set category
        Optional<InventoryItemCategory> categoryOptional = categoryService.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Category not found with ID: " + categoryId);
        }
        item.setCategory(categoryOptional.get());

        // Set unit
        Optional<Unit> unitOptional = unitService.findById(unitId);
        if (unitOptional.isEmpty()) {
            throw new IllegalArgumentException("Unit not found with ID: " + unitId);
        }
        item.setUnit(unitOptional.get());

        // Set brand (optional)
        if (brandId != null && !brandId.trim().isEmpty()) {
            Optional<Brand> brandOptional = brandService.findById(brandId);
            if (brandOptional.isEmpty()) {
                throw new IllegalArgumentException("Brand not found with ID: " + brandId);
            }
            item.setBrand(brandOptional.get());
        } else {
            item.setBrand(null);
        }
    }
}
