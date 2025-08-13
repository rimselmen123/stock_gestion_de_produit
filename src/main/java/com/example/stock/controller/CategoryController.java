package com.example.stock.controller;

import com.example.stock.dto.common.ApiResponse;
import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.category.CategoryCreateDTO;
import com.example.stock.dto.category.CategoryResponseDTO;
import com.example.stock.dto.category.CategoryUpdateDTO;
import com.example.stock.service.InventoryItemCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Category management operations.
 * Provides CRUD endpoints for category entities with proper validation and error handling.
 * 
 * @author Generated
 * @since 1.0
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class CategoryController {

    private final InventoryItemCategoryService categoryService;

    /**
     * Get all categories with filtering and pagination.
     * 
     * @param search Search term for name field
     * @param page Page number (1-based)
     * @param perPage Items per page
     * @param sortField Field to sort by
     * @param sortDirection Sort direction (asc/desc)
     * @return Paginated response with categories
     */
    @GetMapping
    public ResponseEntity<PaginatedResponse<CategoryResponseDTO>> getAllCategories(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "5") int perPage,
            @RequestParam(name = "sort_field", defaultValue = "created_at") String sortField,
            @RequestParam(name = "sort_direction", defaultValue = "desc") String sortDirection) {
        
        log.debug("Getting categories with search: {}, page: {}, perPage: {}, sortField: {}, sortDirection: {}", 
                  search, page, perPage, sortField, sortDirection);
        
        PaginatedResponse<CategoryResponseDTO> response = categoryService.findAllWithPagination(
            search, page, perPage, sortField, sortDirection);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific category by ID.
     * 
     * @param id Category ID
     * @return Category data or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> getCategoryById(@PathVariable String id) {
        
        log.debug("Getting category with ID: {}", id);
        
        CategoryResponseDTO category = categoryService.findByIdOrThrow(id);
        ApiResponse<CategoryResponseDTO> response = ApiResponse.success(category);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new category.
     * 
     * @param createDTO Category creation data
     * @return Created category data
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> createCategory(
            @Valid @RequestBody CategoryCreateDTO createDTO) {
        
        log.info("Creating new category with name: {}", createDTO.getName());
        
        CategoryResponseDTO createdCategory = categoryService.create(createDTO);
        ApiResponse<CategoryResponseDTO> response = ApiResponse.success(
            createdCategory, "Category created successfully");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing category.
     * 
     * @param id Category ID to update
     * @param updateDTO Category update data
     * @return Updated category data
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody CategoryUpdateDTO updateDTO) {
        
        log.info("Updating category with ID: {}", id);
        
        CategoryResponseDTO updatedCategory = categoryService.update(id, updateDTO);
        ApiResponse<CategoryResponseDTO> response = ApiResponse.success(
            updatedCategory, "Category updated successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a category by ID.
     * 
     * @param id Category ID to delete
     * @return Success message or error if category is in use
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String id) {
        
        log.info("Deleting category with ID: {}", id);
        
        categoryService.delete(id);
        ApiResponse<Void> response = ApiResponse.success("Category deleted successfully");
        
        return ResponseEntity.ok(response);
    }
}