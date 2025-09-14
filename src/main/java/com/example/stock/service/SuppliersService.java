package com.example.stock.service;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.suppliers.SupplierCreateDTO;
import com.example.stock.dto.suppliers.SupplierResponseDTO;
import com.example.stock.dto.suppliers.SupplierUpdateDTO;

/**
 * Service interface for Suppliers entity operations.
 * Defines business logic operations for Suppliers management.
 * 
 * @author Generated
 * @since 1.0
 */
public interface SuppliersService {
    
    /**
     * Find all suppliers with pagination, search, and sorting.
     * 
     * @param search Search term for name, email, and phone fields
     * @param page Page number (1-based)
     * @param perPage Items per page
     * @param sortField Field to sort by
     * @param sortDirection Sort direction (asc/desc)
     * @return Paginated response with suppliers
     */
    PaginatedResponse<SupplierResponseDTO> findAllWithPagination(
        String search, int page, int perPage, String sortField, String sortDirection);

    /**
     * Advanced filtering for suppliers with optional individual filters and search.
     * All parameters are optional except pagination/sorting.
     * Date-time parameters should be ISO-8601 strings (e.g. 2025-08-14T00:00:00).
     *
     * @param branchId     Filter by branch ID
     * @param search       Search term applied to name, email, or phone (contains)
     * @param name         Filter name (contains)
     * @param email        Filter email (contains)
     * @param phone        Filter phone (contains)
     * @param address      Filter address (contains)
     * @param description  Filter description (contains)
     * @param createdFrom  Filter createdAt >= this timestamp (ISO-8601)
     * @param createdTo    Filter createdAt <= this timestamp (ISO-8601)
     * @param updatedFrom  Filter updatedAt >= this timestamp (ISO-8601)
     * @param updatedTo    Filter updatedAt <= this timestamp (ISO-8601)
     * @param page         Page number (1-based)
     * @param perPage      Items per page
     * @param sortField    Field to sort by (API field names)
     * @param sortDirection Sort direction (asc/desc)
     * @return Paginated response with suppliers
     */
    PaginatedResponse<SupplierResponseDTO> findAllWithFilters(
        String branchId,
        String search,
        String name,
        String email,
        String phone,
        String address,
        String description,
        String createdFrom,
        String createdTo,
        String updatedFrom,
        String updatedTo,
        int page,
        int perPage,
        String sortField,
        String sortDirection);

    /**
     * Find all suppliers by branch ID.
     * 
     * @param branchId Branch ID
     * @param page Page number (1-based)
     * @param perPage Items per page
     * @return Paginated response with suppliers in the branch
     */
    PaginatedResponse<SupplierResponseDTO> findByBranchId(
            String branchId, int page, int perPage);
    
    /**
     * Find supplier by ID or throw exception if not found.
     * 
     * @param id Supplier ID
     * @return Supplier response DTO
     * @throws ResourceNotFoundException if supplier not found
     */
    SupplierResponseDTO findByIdOrThrow(String id);
    
    /**
     * Create a new supplier.
     * 
     * @param createDTO Supplier creation data
     * @return Created supplier response DTO
     * @throws IllegalArgumentException if supplier data is invalid
     * @throws ForeignKeyConstraintException if referenced branch doesn't exist
     * @throws DuplicateResourceException if email already exists
     */
    SupplierResponseDTO create(SupplierCreateDTO createDTO);
    
    /**
     * Update an existing supplier.
     * 
     * @param id Supplier ID
     * @param updateDTO Supplier update data
     * @return Updated supplier response DTO
     * @throws ResourceNotFoundException if supplier not found
     * @throws ForeignKeyConstraintException if referenced branch doesn't exist
     * @throws DuplicateResourceException if email already exists for another supplier
     */
    SupplierResponseDTO update(String id, SupplierUpdateDTO updateDTO);
    
    /**
     * Delete supplier by ID.
     * 
     * @param id Supplier ID to delete
     * @throws ResourceNotFoundException if supplier not found
     * @throws DeleteConstraintException if supplier is referenced by inventory movements
     */
    void delete(String id);

    /**
     * Check if supplier exists by ID.
     * 
     * @param id Supplier ID
     * @return true if supplier exists, false otherwise
     */
    boolean existsById(String id);

    /**
     * Check if email is already taken by another supplier.
     * 
     * @param email Email to check
     * @param excludeId Supplier ID to exclude from check (for updates)
     * @return true if email is taken, false otherwise
     */
    boolean isEmailTaken(String email, String excludeId);
}
