package com.example.stock.service;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.unit.UnitCreateDTO;
import com.example.stock.dto.unit.UnitResponseDTO;
import com.example.stock.dto.unit.UnitUpdateDTO;

/**
 * Service interface for Unit entity operations.
 * Defines business logic operations for Unit management.
 * 
 * @author Generated
 * @since 1.0
 */
public interface UnitService {
    
    /**
     * Find all units with pagination, search, and sorting.
     * 
     * @param search Search term for name and symbol fields
     * @param page Page number (1-based)
     * @param perPage Items per page
     * @param sortField Field to sort by
     * @param sortDirection Sort direction (asc/desc)
     * @return Paginated response with units
     */
    PaginatedResponse<UnitResponseDTO> findAllWithPagination(
        String search, int page, int perPage, String sortField, String sortDirection);
    
    /**
     * Find unit by ID or throw exception if not found.
     * 
     * @param id Unit ID
     * @return Unit response DTO
     * @throws ResourceNotFoundException if unit not found
     */
    UnitResponseDTO findByIdOrThrow(String id);
    
    /**
     * Create a new unit.
     * 
     * @param createDTO Unit creation data
     * @return Created unit response DTO
     * @throws IllegalArgumentException if unit data is invalid
     */
    UnitResponseDTO create(UnitCreateDTO createDTO);
    
    /**
     * Update an existing unit.
     * 
     * @param id Unit ID
     * @param updateDTO Unit update data
     * @return Updated unit response DTO
     * @throws ResourceNotFoundException if unit not found
     */
    UnitResponseDTO update(String id, UnitUpdateDTO updateDTO);
    
    /**
     * Delete unit by ID.
     * 
     * @param id Unit ID to delete
     * @throws ResourceNotFoundException if unit not found
     * @throws DeleteConstraintException if unit is referenced by inventory items
     */
    void delete(String id);
}