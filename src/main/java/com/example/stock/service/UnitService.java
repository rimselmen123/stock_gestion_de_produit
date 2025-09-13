package com.example.stock.service;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.unit.UnitCreateDTO;
import com.example.stock.dto.unit.UnitResponseDTO;
import com.example.stock.dto.unit.UnitUpdateDTO;

import java.util.List;

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
     * Advanced filtering for units with optional individual filters and search.
     * All parameters are optional except pagination/sorting.
     * Date-time parameters should be ISO-8601 strings (e.g. 2025-08-14T00:00:00).
     *
     * @param search       Search term applied to name or symbol (contains)
     * @param name         Filter name (contains)
     * @param symbol       Filter symbol (contains)
     * @param branchId     Filter by branch ID
     * @param departmentId Filter by department ID
     * @param createdFrom  Filter createdAt >= this timestamp (ISO-8601)
     * @param createdTo    Filter createdAt <= this timestamp (ISO-8601)
     * @param updatedFrom  Filter updatedAt >= this timestamp (ISO-8601)
     * @param updatedTo    Filter updatedAt <= this timestamp (ISO-8601)
     * @param page         Page number (1-based)
     * @param perPage      Items per page
     * @param sortField    Field to sort by (API field names)
     * @param sortDirection Sort direction (asc/desc)
     * @return Paginated response with units
     */
    PaginatedResponse<UnitResponseDTO> findAllWithFilters(
        String search,
        String name,
        String symbol,
        String branchId,
        String departmentId,
        String createdFrom,
        String createdTo,
        String updatedFrom,
        String updatedTo,
        int page,
        int perPage,
        String sortField,
        String sortDirection);
    
    /**
     * Find unit by ID or throw exception if not found.
     * 
     * @param id Unit ID
     * @return Unit response DTO
     * @throws ResourceNotFoundException if unit not found
     */
    UnitResponseDTO findByIdOrThrow(String id);

    /**
     * Find units by branch ID.
     * 
     * @param branchId Branch ID
     * @return List of units in the branch
     */
    List<UnitResponseDTO> findByBranchId(String branchId);

    /**
     * Find units by department ID.
     * 
     * @param departmentId Department ID
     * @return List of units in the department
     */
    List<UnitResponseDTO> findByDepartmentId(String departmentId);

    /**
     * Find units by branch ID and department ID.
     * 
     * @param branchId Branch ID
     * @param departmentId Department ID
     * @return List of units in the branch and department
     */
    List<UnitResponseDTO> findByBranchIdAndDepartmentId(String branchId, String departmentId);
    
    /**
     * Create a new unit.
     * 
     * @param createDTO Unit creation data
     * @return Created unit response DTO
     * @throws IllegalArgumentException if unit data is invalid
     * @throws ResourceNotFoundException if branch or department not found
     */
    UnitResponseDTO create(UnitCreateDTO createDTO);
    
    /**
     * Update an existing unit.
     * 
     * @param id Unit ID
     * @param updateDTO Unit update data
     * @return Updated unit response DTO
     * @throws ResourceNotFoundException if unit, branch, or department not found
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