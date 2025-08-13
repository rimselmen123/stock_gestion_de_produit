package com.example.stock.service;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.category.CategoryCreateDTO;
import com.example.stock.dto.category.CategoryResponseDTO;
import com.example.stock.dto.category.CategoryUpdateDTO;

/**
 * Service interface for InventoryItemCategory entity operations.
 * Defines business logic operations for Category management.
 * 
 * @author Generated
 * @since 1.0
 */
public interface InventoryItemCategoryService {
    
    /**
     * Find all categories with pagination, search, and sorting.
     * 
     * @param search Search term for name field
     * @param page Page number (1-based)
     * @param perPage Items per page
     * @param sortField Field to sort by
     * @param sortDirection Sort direction (asc/desc)
     * @return Paginated response with categories
     */
    PaginatedResponse<CategoryResponseDTO> findAllWithPagination(
        String search, int page, int perPage, String sortField, String sortDirection);
    
    /**
     * Find category by ID or throw exception if not found.
     * 
     * @param id Category ID
     * @return Category response DTO
     * @throws ResourceNotFoundException if category not found
     */
    CategoryResponseDTO findByIdOrThrow(String id);
    
    /**
     * Create a new category.
     * 
     * @param createDTO Category creation data
     * @return Created category response DTO
     * @throws IllegalArgumentException if category data is invalid
     */
    CategoryResponseDTO create(CategoryCreateDTO createDTO);
    
    /**
     * Update an existing category.
     * 
     * @param id Category ID
     * @param updateDTO Category update data
     * @return Updated category response DTO
     * @throws ResourceNotFoundException if category not found
     */
    CategoryResponseDTO update(String id, CategoryUpdateDTO updateDTO);
    
    /**
     * Delete category by ID.
     * 
     * @param id Category ID to delete
     * @throws ResourceNotFoundException if category not found
     * @throws DeleteConstraintException if category is referenced by inventory items
     */
    void delete(String id);
}