package com.example.stock.service;

import com.example.stock.dto.tax.TaxFilterDTO;
import com.example.stock.dto.tax.TaxRequestDTO;
import com.example.stock.dto.tax.TaxResponseDTO;
import com.example.stock.dto.common.PaginatedResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Tax entity operations.
 * Defines business logic operations for Tax management.
 * 
 * @author Generated
 * @since 1.0
 */
public interface TaxService {

    /**
     * Create a new tax.
     * 
     * @param createDTO Tax creation data
     * @return Created tax response DTO
     * @throws IllegalArgumentException if tax data is invalid
     * @throws ResourceNotFoundException if branch not found
     */
    TaxResponseDTO create(TaxRequestDTO createDTO);

    /**
     * Update an existing tax.
     * 
     * @param id Tax ID
     * @param updateDTO Tax update data
     * @return Updated tax response DTO
     * @throws ResourceNotFoundException if tax or branch not found
     */
    TaxResponseDTO update(Long id, TaxRequestDTO updateDTO);

    /**
     * Delete tax by ID.
     * 
     * @param id Tax ID to delete
     * @throws ResourceNotFoundException if tax not found
     * @throws DeleteConstraintException if tax is referenced by inventory items
     */
    void delete(Long id);

    /**
     * Find tax by ID.
     * 
     * @param id Tax ID
     * @return Tax response DTO
     * @throws ResourceNotFoundException if tax not found
     */
    TaxResponseDTO findById(Long id);

    /**
     * Find all taxes with pagination and filtering.
     * 
     * @param filterDTO Tax filter criteria
     * @return Paginated tax response
     */
    PaginatedResponse<TaxResponseDTO> findAll(TaxFilterDTO filterDTO);

    /**
     * Find taxes by branch ID.
     * 
     * @param branchId Branch ID
     * @return List of taxes in the branch
     */
    List<TaxResponseDTO> findByBranchId(String branchId);

    /**
     * Calculate tax amount for a base amount using tax ID.
     * 
     * @param taxId Tax ID
     * @param baseAmount Base amount
     * @return Calculated tax amount
     */
    BigDecimal calculateTaxAmount(Long taxId, BigDecimal baseAmount);

    /**
     * Calculate tax amount for a base amount using tax rate.
     * 
     * @param taxRate Tax rate percentage
     * @param baseAmount Base amount
     * @return Calculated tax amount
     */
    BigDecimal calculateTaxAmount(BigDecimal taxRate, BigDecimal baseAmount);

    /**
     * Calculate total price including tax using tax ID.
     * 
     * @param taxId Tax ID
     * @param baseAmount Base amount
     * @return Total price with tax
     */
    BigDecimal calculatePriceWithTax(Long taxId, BigDecimal baseAmount);

    /**
     * Calculate total price including tax using tax rate.
     * 
     * @param taxRate Tax rate percentage
     * @param baseAmount Base amount
     * @return Total price with tax
     */
    BigDecimal calculatePriceWithTax(BigDecimal taxRate, BigDecimal baseAmount);

    /**
     * Get display name for tax (name with rate).
     * 
     * @param taxId Tax ID
     * @return Display name with rate
     */
    String getDisplayName(Long taxId);

    /**
     * Validate if tax rate is within acceptable range.
     * 
     * @param rate Tax rate to validate
     * @return True if rate is valid
     */
    boolean isValidTaxRate(BigDecimal rate);

    /**
     * Check if tax can be safely deleted.
     * 
     * @param id Tax ID
     * @return True if tax can be deleted
     */
    boolean canDelete(Long id);

    /**
     * Count taxes by branch ID.
     * 
     * @param branchId Branch ID
     * @return Number of taxes in branch
     */
    long countByBranchId(String branchId);
}