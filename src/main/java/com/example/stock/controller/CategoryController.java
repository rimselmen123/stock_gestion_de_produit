package com.example.stock.controller;

import com.example.stock.dto.category.CategoryCreateDTO;
import com.example.stock.dto.category.CategoryResponseDTO;
import com.example.stock.dto.category.CategorySummaryDTO;
import com.example.stock.dto.category.CategoryUpdateDTO;
import com.example.stock.entity.InventoryItemCategory;
import com.example.stock.mapper.CategoryMapper;
import com.example.stock.service.InventoryItemCategoryService;
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

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for InventoryItemCategory management operations.
 * Provides CRUD endpoints for category entities with proper validation and error handling.
 * 
 * @author Generated
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Category Management", description = "APIs for managing inventory item categories")
public class CategoryController {

    private final InventoryItemCategoryService categoryService;
    private final CategoryMapper categoryMapper;

    /**
     * Create a new category.
     * 
     * @param createDTO the category creation data
     * @return ResponseEntity with created category data
     */
    @PostMapping
    @Operation(summary = "Create a new category", description = "Creates a new inventory item category with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Category with same name already exists in branch")
    })
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryCreateDTO createDTO) {
        
        log.info("Creating new category with name: {} for branch: {}", 
                createDTO.getName(), createDTO.getBranchId());
        
        InventoryItemCategory categoryEntity = categoryMapper.toEntity(createDTO);
        InventoryItemCategory createdCategory = categoryService.createCategory(categoryEntity);
        CategoryResponseDTO responseDTO = categoryMapper.toResponseDTO(createdCategory);
        
        log.info("Category created successfully with ID: {}", createdCategory.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Get all categories.
     * 
     * @return ResponseEntity with list of all categories
     */
    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieves a list of all inventory item categories")
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        
        log.debug("Retrieving all categories");
        
        List<InventoryItemCategory> categories = categoryService.findAll();
        List<CategoryResponseDTO> responseDTOs = categoryMapper.toResponseDTOList(categories);
        
        log.debug("Retrieved {} categories", categories.size());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Get categories by branch ID.
     *
     * @param branchId the branch ID
     * @return ResponseEntity with list of categories for the branch
     */
    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get categories by branch", description = "Retrieves all categories for a specific branch")
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesByBranch(
            @Parameter(description = "Branch ID", required = true)
            @PathVariable String branchId) {

        log.debug("Retrieving categories for branch: {}", branchId);

        List<InventoryItemCategory> categories = categoryService.findByBranchId(branchId);
        List<CategoryResponseDTO> responseDTOs = categoryMapper.toResponseDTOList(categories);

        log.debug("Retrieved {} categories for branch: {}", categories.size(), branchId);
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Get categories summary for listings.
     *
     * @return ResponseEntity with list of category summaries
     */
    @GetMapping("/summary")
    @Operation(summary = "Get categories summary", description = "Retrieves a lightweight list of categories for dropdowns and listings")
    @ApiResponse(responseCode = "200", description = "Category summaries retrieved successfully")
    public ResponseEntity<List<CategorySummaryDTO>> getCategoriesSummary() {

        log.debug("Retrieving categories summary");

        List<InventoryItemCategory> categories = categoryService.findAll();
        List<CategorySummaryDTO> summaryDTOs = categoryMapper.toSummaryDTOList(categories);

        log.debug("Retrieved {} category summaries", categories.size());
        return ResponseEntity.ok(summaryDTOs);
    }

    /**
     * Get category by ID.
     *
     * @param id the category ID
     * @return ResponseEntity with category data or 404 if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieves a specific category by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @Parameter(description = "Category ID", required = true)
            @PathVariable String id) {

        log.debug("Retrieving category with ID: {}", id);

        Optional<InventoryItemCategory> categoryOptional = categoryService.findById(id);

        if (categoryOptional.isPresent()) {
            CategoryResponseDTO responseDTO = categoryMapper.toResponseDTO(categoryOptional.get());
            log.debug("Category found with ID: {}", id);
            return ResponseEntity.ok(responseDTO);
        } else {
            log.warn("Category not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update an existing category.
     *
     * @param id the category ID to update
     * @param updateDTO the category update data
     * @return ResponseEntity with updated category data
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Updates an existing category with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "409", description = "Category with same name already exists in branch")
    })
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @Parameter(description = "Category ID", required = true)
            @PathVariable String id,
            @Valid @RequestBody CategoryUpdateDTO updateDTO) {

        log.info("Updating category with ID: {}", id);

        // Check if category exists
        Optional<InventoryItemCategory> existingCategoryOptional = categoryService.findById(id);
        if (existingCategoryOptional.isEmpty()) {
            log.warn("Category not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        InventoryItemCategory categoryToUpdate = categoryMapper.toEntity(new CategoryCreateDTO(
            updateDTO.getName(),
            updateDTO.getDescription(),
            existingCategoryOptional.get().getBranchId() // Keep original branchId
        ));

        InventoryItemCategory updatedCategory = categoryService.updateCategory(id, categoryToUpdate);
        CategoryResponseDTO responseDTO = categoryMapper.toResponseDTO(updatedCategory);

        log.info("Category updated successfully with ID: {}", id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Delete a category by ID.
     *
     * @param id the category ID to delete
     * @return ResponseEntity with no content or error status
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Deletes a category by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete category with associated inventory items")
    })
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Category ID", required = true)
            @PathVariable String id) {

        log.info("Deleting category with ID: {}", id);

        // Check if category exists
        Optional<InventoryItemCategory> categoryOptional = categoryService.findById(id);
        if (categoryOptional.isEmpty()) {
            log.warn("Category not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        try {
            categoryService.deleteById(id);
            log.info("Category deleted successfully with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting category with ID: {}, Error: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Search categories by name and branch.
     *
     * @param name the category name to search for
     * @param branchId the branch ID
     * @return ResponseEntity with matching categories
     */
    @GetMapping("/search")
    @Operation(summary = "Search categories by name", description = "Finds categories by partial name match within a specific branch")
    @ApiResponse(responseCode = "200", description = "Categories found")
    public ResponseEntity<List<CategoryResponseDTO>> searchCategoriesByName(
            @Parameter(description = "Category name to search for", required = true)
            @RequestParam String name,
            @Parameter(description = "Branch ID", required = true)
            @RequestParam String branchId) {

        log.debug("Searching for categories with name: {} in branch: {}", name, branchId);

        List<InventoryItemCategory> categories = categoryService.searchByNameAndBranchId(name, branchId);
        List<CategoryResponseDTO> responseDTOs = categoryMapper.toResponseDTOList(categories);

        log.debug("Found {} categories matching name: {} in branch: {}", categories.size(), name, branchId);
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Get category by name and branch.
     *
     * @param name the category name
     * @param branchId the branch ID
     * @return ResponseEntity with category data or 404 if not found
     */
    @GetMapping("/find")
    @Operation(summary = "Find category by exact name", description = "Finds a category by its exact name within a specific branch")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category found"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CategoryResponseDTO> findCategoryByNameAndBranch(
            @Parameter(description = "Category name", required = true)
            @RequestParam String name,
            @Parameter(description = "Branch ID", required = true)
            @RequestParam String branchId) {

        log.debug("Finding category with name: {} in branch: {}", name, branchId);

        Optional<InventoryItemCategory> categoryOptional = categoryService.findByNameAndBranchId(name, branchId);

        if (categoryOptional.isPresent()) {
            CategoryResponseDTO responseDTO = categoryMapper.toResponseDTO(categoryOptional.get());
            log.debug("Category found with name: {} in branch: {}", name, branchId);
            return ResponseEntity.ok(responseDTO);
        } else {
            log.warn("Category not found with name: {} in branch: {}", name, branchId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get category count by branch.
     *
     * @param branchId the branch ID
     * @return ResponseEntity with category count
     */
    @GetMapping("/count/branch/{branchId}")
    @Operation(summary = "Get category count by branch", description = "Returns the number of categories in a specific branch")
    @ApiResponse(responseCode = "200", description = "Category count retrieved successfully")
    public ResponseEntity<Long> getCategoryCountByBranch(
            @Parameter(description = "Branch ID", required = true)
            @PathVariable String branchId) {

        log.debug("Counting categories for branch: {}", branchId);

        long count = categoryService.countByBranchId(branchId);

        log.debug("Found {} categories in branch: {}", count, branchId);
        return ResponseEntity.ok(count);
    }
}
