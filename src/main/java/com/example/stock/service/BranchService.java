package com.example.stock.service;

import com.example.stock.dto.branch.BranchCreateDTO;
import com.example.stock.dto.branch.BranchFilterDTO;
import com.example.stock.dto.branch.BranchResponseDTO;
import com.example.stock.dto.branch.BranchSummaryDTO;
import com.example.stock.dto.branch.BranchUpdateDTO;
import com.example.stock.dto.common.PaginatedResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Branch entity operations.
 * Defines business logic operations for Branch management.
 * 
 * @author Generated
 * @since 1.0
 */
public interface BranchService {
    
    // =============================================
    // CRUD OPERATIONS
    // =============================================
    
    /**
     * Create a new branch.
     * 
     * @param createDTO branch creation data
     * @return created branch details
     */
    BranchResponseDTO create(BranchCreateDTO createDTO);
    
    /**
     * Update an existing branch.
     * 
     * @param id branch ID
     * @param updateDTO branch update data
     * @return updated branch details
     */
    BranchResponseDTO update(String id, BranchUpdateDTO updateDTO);
    
    /**
     * Find branch by ID.
     * 
     * @param id branch ID
     * @return branch details if found
     */
    Optional<BranchResponseDTO> findById(String id);
    
    /**
     * Delete branch by ID (with validation).
     * 
     * @param id branch ID
     */
    void deleteById(String id);

    // =============================================
    // SEARCH AND FILTERING
    // =============================================
    
    /**
     * Find branch by name (case-insensitive).
     * 
     * @param name branch name
     * @return branch details if found
     */
    Optional<BranchResponseDTO> findByName(String name);
    
    /**
     * Find branch by code (case-insensitive).
     * 
     * @param code branch code
     * @return branch details if found
     */
    Optional<BranchResponseDTO> findByCode(String code);
    
    /**
     * Find all branches with comprehensive filtering.
     * 
     * @param filterDTO comprehensive filter parameters
     * @return paginated response with branches
     */
    PaginatedResponse<BranchResponseDTO> findAllWithFilters(BranchFilterDTO filterDTO);

    // =============================================
    // UTILITY METHODS
    // =============================================
    
    /**
     * Get all active branches for dropdowns.
     * 
     * @return list of active branches
     */
    List<BranchSummaryDTO> findAllActiveBranches();
    
    /**
     * Get all branches (active and inactive) for admin dropdowns.
     * 
     * @return list of all branches
     */
    List<BranchSummaryDTO> findAllBranches();
    
    /**
     * Search branches by name for autocomplete.
     * 
     * @param name search term
     * @param activeOnly include only active branches
     * @return list of matching branches
     */
    List<BranchSummaryDTO> searchByName(String name, boolean activeOnly);

    // =============================================
    // VALIDATION METHODS
    // =============================================
    
    /**
     * Check if branch exists by name.
     * 
     * @param name branch name
     * @return true if exists
     */
    boolean existsByName(String name);
    
    /**
     * Check if branch exists by code.
     * 
     * @param code branch code
     * @return true if exists
     */
    boolean existsByCode(String code);
    
    /**
     * Check if branch can be deleted (no dependencies).
     * 
     * @param id branch ID
     * @return true if can be deleted
     */
    boolean canDelete(String id);
    
    /**
     * Soft delete branch (set isActive = false).
     * 
     * @param id branch ID
     */
    void softDelete(String id);
    
    /**
     * Reactivate branch (set isActive = true).
     * 
     * @param id branch ID
     */
    void reactivate(String id);

    // =============================================
    // STATISTICS
    // =============================================
    
    /**
     * Get total count of branches.
     * 
     * @return total count
     */
    long getTotalCount();
    
    /**
     * Get count of active branches.
     * 
     * @return active count
     */
    long getActiveCount();
}
