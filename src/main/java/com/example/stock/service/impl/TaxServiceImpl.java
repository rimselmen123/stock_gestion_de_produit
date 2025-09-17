package com.example.stock.service.impl;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.common.PaginationInfo;
import com.example.stock.dto.tax.TaxRequestDTO;
import com.example.stock.dto.tax.TaxFilterDTO;
import com.example.stock.dto.tax.TaxResponseDTO;
import com.example.stock.entity.Tax;
import com.example.stock.exception.DeleteConstraintException;
import com.example.stock.exception.ResourceNotFoundException;
import com.example.stock.exception.ValidationException;
import com.example.stock.mapper.TaxMapper;
import com.example.stock.repository.BranchRepository;
import com.example.stock.repository.TaxRepository;
import com.example.stock.service.TaxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of TaxService interface.
 * Handles all business logic for Tax entity operations.
 * 
 * @author Generated
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaxServiceImpl implements TaxService {

    private final TaxRepository taxRepository;
    private final BranchRepository branchRepository;
    private final TaxMapper taxMapper;

    // Constants
    private static final String TAX_NOT_FOUND_MSG = "Tax not found with ID: ";
    private static final String BRANCH_NOT_FOUND_MSG = "Branch not found with ID: ";
    private static final String CREATED_AT_FIELD = "createdAt";
    private static final String UPDATED_AT_FIELD = "updatedAt";

    @Override
    public TaxResponseDTO create(TaxRequestDTO createDTO) {
        log.info("Creating new tax: {}", createDTO.getName());
        
        // Validate branch exists
        validateBranchExists(createDTO.getBranchId());
        
        // Validate unique name per branch
        validateUniqueTaxName(createDTO.getName(), createDTO.getBranchId(), null);
        
        // Create and save tax
        Tax tax = taxMapper.toEntity(createDTO);
        Tax savedTax = taxRepository.save(tax);
        
        log.info("Tax created successfully with ID: {}", savedTax.getId());
        return convertToResponseDTO(savedTax);
    }

    @Override
    public TaxResponseDTO update(Long id, TaxRequestDTO updateDTO) {
        log.info("Updating tax with ID: {}", id);
        
        Tax existingTax = findTaxByIdOrThrow(id);
        
        // Validate branch exists
        validateBranchExists(updateDTO.getBranchId());
        
        // Validate unique name per branch (excluding current tax)
        validateUniqueTaxName(updateDTO.getName(), updateDTO.getBranchId(), id);
        
        // Update using mapper
        taxMapper.updateEntityFromRequest(updateDTO, existingTax);
        
        Tax updatedTax = taxRepository.save(existingTax);
        
        log.info("Tax updated successfully: {}", updatedTax.getId());
        return convertToResponseDTO(updatedTax);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting tax with ID: {}", id);
        
        Tax tax = findTaxByIdOrThrow(id);
        
        // Check if tax can be deleted - inline check instead of calling transactional method
        if (tax.getInventoryItems() != null && !tax.getInventoryItems().isEmpty()) {
            throw new DeleteConstraintException("Tax", id.toString(), "Tax is referenced by inventory items");
        }
        
        taxRepository.delete(tax);
        log.info("Tax deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public TaxResponseDTO findById(Long id) {
        log.debug("Finding tax by ID: {}", id);
        Tax tax = findTaxByIdOrThrow(id);
        return convertToResponseDTO(tax);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<TaxResponseDTO> findAll(TaxFilterDTO filterDTO) {
        log.debug("Finding all taxes with filters: {}", filterDTO);
        
        // Create Specification for dynamic filtering
        Specification<Tax> spec = createSpecification(filterDTO);
        
        // Create Pageable object
        Pageable pageable = createPageable(filterDTO);
        
        // Execute query
        Page<Tax> taxPage = taxRepository.findAll(spec, pageable);
        
        // Convert to DTOs
        List<TaxResponseDTO> taxDTOs = taxPage.getContent().stream()
                .map(this::convertToResponseDTO)
                .toList();
        
        // Create pagination info
        PaginationInfo paginationInfo = PaginationInfo.of(
                taxPage.getNumber() + 1, // Convert to 1-based
                taxPage.getSize(), 
                taxPage.getTotalElements()
        );
        
        return PaginatedResponse.<TaxResponseDTO>builder()
                .data(taxDTOs)
                .pagination(paginationInfo)
                .success(true)
                .message("Taxes retrieved successfully")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaxResponseDTO> findByBranchId(String branchId) {
        log.debug("Finding taxes by branch ID: {}", branchId);
        
        validateBranchExists(branchId);
        
        List<Tax> taxes = taxRepository.findByBranchIdOrderByRate(branchId);
        return taxes.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTaxAmount(Long taxId, BigDecimal baseAmount) {
        if (baseAmount == null || baseAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        Tax tax = findTaxByIdOrThrow(taxId);
        return calculateTaxAmount(tax.getRate(), baseAmount);
    }

    @Override
    public BigDecimal calculateTaxAmount(BigDecimal taxRate, BigDecimal baseAmount) {
        if (baseAmount == null || baseAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        if (taxRate == null || taxRate.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        return baseAmount.multiply(taxRate)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculatePriceWithTax(Long taxId, BigDecimal baseAmount) {
        if (baseAmount == null || baseAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        // Inline tax calculation to avoid transactional call
        Tax tax = findTaxByIdOrThrow(taxId);
        BigDecimal taxAmount = calculateTaxAmount(tax.getRate(), baseAmount);
        return baseAmount.add(taxAmount);
    }

    @Override
    public BigDecimal calculatePriceWithTax(BigDecimal taxRate, BigDecimal baseAmount) {
        if (baseAmount == null || baseAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal taxAmount = calculateTaxAmount(taxRate, baseAmount);
        return baseAmount.add(taxAmount);
    }

    @Override
    @Transactional(readOnly = true)
    public String getDisplayName(Long taxId) {
        Tax tax = findTaxByIdOrThrow(taxId);
        return String.format("%s (%.2f%%)", tax.getName(), tax.getRate());
    }

    @Override
    public boolean isValidTaxRate(BigDecimal rate) {
        return rate != null && 
               rate.compareTo(BigDecimal.ZERO) >= 0 && 
               rate.compareTo(BigDecimal.valueOf(100)) <= 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDelete(Long id) {
        Tax tax = findTaxByIdOrThrow(id);
        return tax.getInventoryItems() == null || tax.getInventoryItems().isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByBranchId(String branchId) {
        validateBranchExists(branchId);
        return taxRepository.countByBranchId(branchId);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private Tax findTaxByIdOrThrow(Long id) {
        return taxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TAX_NOT_FOUND_MSG + id));
    }

    private void validateBranchExists(String branchId) {
        if (!branchRepository.existsById(branchId)) {
            log.error("Branch not found with ID: {}", branchId);
            throw new ResourceNotFoundException(BRANCH_NOT_FOUND_MSG + branchId);
        }
    }

    private void validateUniqueTaxName(String name, String branchId, Long excludeId) {
        boolean exists;
        if (excludeId != null) {
            exists = taxRepository.existsByNameAndBranchIdAndIdNot(name, branchId, excludeId);
        } else {
            exists = taxRepository.existsByNameAndBranchId(name, branchId);
        }
        
        if (exists) {
            throw new ValidationException("Tax name '" + name + "' already exists for this branch");
        }
    }

    private TaxResponseDTO convertToResponseDTO(Tax tax) {
        TaxResponseDTO dto = taxMapper.toResponseDTO(tax);
        
        // Add branch name if available
        if (tax.getBranch() != null) {
            dto.setBranchName(tax.getBranch().getName());
        }
        
        // Add display name
        dto.setDisplayName(String.format("%s (%.2f%%)", tax.getName(), tax.getRate()));
        
        return dto;
    }

    private Specification<Tax> createSpecification(TaxFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Filter by branch ID
            if (StringUtils.hasText(filterDTO.getBranchId())) {
                predicates.add(criteriaBuilder.equal(root.get("branchId"), filterDTO.getBranchId()));
            }
            
            // Filter by name (case-insensitive partial match)
            if (StringUtils.hasText(filterDTO.getName())) {
                String namePattern = "%" + filterDTO.getName().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), namePattern));
            }
            
            // Filter by minimum rate
            if (filterDTO.getMinRate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rate"), filterDTO.getMinRate()));
            }
            
            // Filter by maximum rate
            if (filterDTO.getMaxRate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("rate"), filterDTO.getMaxRate()));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable createPageable(TaxFilterDTO filterDTO) {
        // Map and validate sort field
        String sortField = mapSortField(filterDTO.getSortBy());
        
        // Create sort direction
        Sort.Direction direction = "desc".equalsIgnoreCase(filterDTO.getSortDir()) 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        // Create sort object
        Sort sort = Sort.by(direction, sortField);
        
        // Create pageable
        return PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), sort);
    }

    private String mapSortField(String sortField) {
        if (sortField == null || sortField.trim().isEmpty()) {
            return "name";
        }
        
        return switch (sortField.toLowerCase()) {
            case "created", "created_at" -> CREATED_AT_FIELD;
            case "updated", "updated_at" -> UPDATED_AT_FIELD;
            case "name", "rate", "description" -> sortField.toLowerCase();
            default -> "name";
        };
    }
}