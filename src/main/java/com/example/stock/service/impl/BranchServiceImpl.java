package com.example.stock.service.impl;

import com.example.stock.dto.branch.BranchCreateDTO;
import com.example.stock.dto.branch.BranchFilterDTO;
import com.example.stock.dto.branch.BranchResponseDTO;
import com.example.stock.dto.branch.BranchSummaryDTO;
import com.example.stock.dto.branch.BranchUpdateDTO;
import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.entity.Branch;
import com.example.stock.exception.ResourceNotFoundException;
import com.example.stock.exception.DuplicateResourceException;
import com.example.stock.exception.ResourceConflictException;
import com.example.stock.mapper.BranchMapper;
import com.example.stock.repository.BranchRepository;
import com.example.stock.service.BranchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Professional implementation of BranchService.
 * Handles all business logic for Branch management.
 * 
 * @author Generated
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BranchServiceImpl implements BranchService {
    
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    
    // =============================================
    // CRUD OPERATIONS
    // =============================================
    
    @Override
    @Transactional
    public BranchResponseDTO create(BranchCreateDTO createDTO) {
        log.info("Creating new branch with name: {}", createDTO.getName());
        
        // Validate uniqueness
        validateNameUniqueness(createDTO.getName(), null);
        if (StringUtils.hasText(createDTO.getCode())) {
            validateCodeUniqueness(createDTO.getCode(), null);
        }
        
        // Map to entity
        Branch branch = branchMapper.toEntity(createDTO);
        // Normalize and generate ID
        if (StringUtils.hasText(branch.getCode())) {
            branch.setCode(branch.getCode().trim().toUpperCase());
        }
        if (!StringUtils.hasText(branch.getId())) {
            branch.setId(UUID.randomUUID().toString());
        }
        
        // Save
        Branch savedBranch = branchRepository.save(branch);
        log.info("Branch created successfully with ID: {}", savedBranch.getId());
        
        return branchMapper.toResponseDTO(savedBranch);
    }
    
    @Override
    @Transactional
    public BranchResponseDTO update(String id, BranchUpdateDTO updateDTO) {
        log.info("Updating branch with ID: {}", id);
        
        // Find existing
        Branch existingBranch = findBranchEntityById(id);
        
        // Validate uniqueness if values changed
        if (StringUtils.hasText(updateDTO.getName()) && 
            !updateDTO.getName().equals(existingBranch.getName())) {
            validateNameUniqueness(updateDTO.getName(), id);
        }
        
        if (StringUtils.hasText(updateDTO.getCode()) && 
            !updateDTO.getCode().equals(existingBranch.getCode())) {
            validateCodeUniqueness(updateDTO.getCode(), id);
        }
        
        // Update entity
        branchMapper.updateEntityFromDTO(updateDTO, existingBranch);
        if (StringUtils.hasText(existingBranch.getCode())) {
            existingBranch.setCode(existingBranch.getCode().trim().toUpperCase());
        }
        
        // Save
        Branch updatedBranch = branchRepository.save(existingBranch);
        log.info("Branch updated successfully with ID: {}", updatedBranch.getId());
        
        return branchMapper.toResponseDTO(updatedBranch);
    }
    
    @Override
    public Optional<BranchResponseDTO> findById(String id) {
        log.debug("Finding branch by ID: {}", id);
        
        return branchRepository.findById(id)
                .map(branchMapper::toResponseDTO);
    }
    
    @Override
    @Transactional
    public void deleteById(String id) {
        log.info("Attempting to delete branch with ID: {}", id);
        
        Branch branch = findBranchEntityById(id);
        
        // Check if branch can be deleted
        if (branchRepository.isUsedInMovements(id)) {
            throw new ResourceConflictException(
                "Cannot delete branch: it has associated inventory movements"
            );
        }
        
        if (branchRepository.hasDepartments(id)) {
            throw new ResourceConflictException(
                "Cannot delete branch: it has associated departments"
            );
        }
        
        branchRepository.delete(branch);
        log.info("Branch deleted successfully with ID: {}", id);
    }

    // =============================================
    // SEARCH AND FILTERING
    // =============================================
    
    @Override
    public Optional<BranchResponseDTO> findByName(String name) {
        log.debug("Finding branch by name: {}", name);
        
        return branchRepository.findByNameIgnoreCase(name)
                .map(branchMapper::toResponseDTO);
    }
    
    @Override
    public Optional<BranchResponseDTO> findByCode(String code) {
        log.debug("Finding branch by code: {}", code);
        
        return branchRepository.findByCodeIgnoreCase(code)
                .map(branchMapper::toResponseDTO);
    }
    
    @Override
    public PaginatedResponse<BranchResponseDTO> findAllWithFilters(BranchFilterDTO filterDTO) {
        log.debug("Finding branches with filters - page: {}, perPage: {}", 
                 filterDTO.getPage(), filterDTO.getPerPage());
        
        // Create pageable
        Sort sort = createSort(filterDTO.getSortField(), filterDTO.getSortDirection());
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getPerPage(), sort);
        
        // Find with filters
        Page<Branch> branchPage = branchRepository.findAllWithFilters(
            filterDTO.getSearch(), 
            filterDTO.getName(), 
            filterDTO.getLocation(), 
            filterDTO.getCode(), 
            filterDTO.getIsActive(), 
            filterDTO.getCreatedAfter(), 
            filterDTO.getCreatedBefore(), 
            pageable
        );
        
        // Map to DTOs
        List<BranchResponseDTO> branches = branchPage.getContent()
                .stream()
                .map(branchMapper::toResponseDTO)
                .toList();
        
        return PaginatedResponse.<BranchResponseDTO>builder()
                .data(branches)
                .totalElements(branchPage.getTotalElements())
                .totalPages(branchPage.getTotalPages())
                .currentPage(filterDTO.getPage())
                .pageSize(filterDTO.getPerPage())
                .hasNext(branchPage.hasNext())
                .hasPrevious(branchPage.hasPrevious())
                .build();
    }

    // =============================================
    // UTILITY METHODS
    // =============================================
    
    @Override
    public List<BranchSummaryDTO> findAllActiveBranches() {
        log.debug("Finding all active branches");
        
        return branchRepository.findByIsActiveTrueOrderByNameAsc()
                .stream()
                .map(branchMapper::toSummaryDTO)
                .toList();
    }
    
    @Override
    public List<BranchSummaryDTO> findAllBranches() {
        log.debug("Finding all branches");
        
        return branchRepository.findAllByOrderByNameAsc()
                .stream()
                .map(branchMapper::toSummaryDTO)
                .toList();
    }
    
    @Override
    public List<BranchSummaryDTO> searchByName(String name, boolean activeOnly) {
        log.debug("Searching branches by name: {}, activeOnly: {}", name, activeOnly);
        
        List<Branch> branches;
        if (activeOnly) {
            branches = branchRepository.findActiveByNameContainingIgnoreCase(name);
        } else {
            branches = branchRepository.findByNameContainingIgnoreCase(name);
        }
        
        return branches.stream()
                .map(branchMapper::toSummaryDTO)
                .toList();
    }

    // =============================================
    // VALIDATION METHODS
    // =============================================
    
    @Override
    public boolean existsByName(String name) {
        return branchRepository.existsByNameIgnoreCase(name);
    }
    
    @Override
    public boolean existsByCode(String code) {
        return branchRepository.existsByCode(code);
    }
    
    @Override
    public boolean canDelete(String id) {
        return !branchRepository.isUsedInMovements(id) && 
               !branchRepository.hasDepartments(id);
    }
    
    @Override
    @Transactional
    public void softDelete(String id) {
        log.info("Soft deleting branch with ID: {}", id);
        
        Branch branch = findBranchEntityById(id);
        branch.setIsActive(false);
        branchRepository.save(branch);
        
        log.info("Branch soft deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional
    public void reactivate(String id) {
        log.info("Reactivating branch with ID: {}", id);
        
        Branch branch = findBranchEntityById(id);
        branch.setIsActive(true);
        branchRepository.save(branch);
        
        log.info("Branch reactivated successfully with ID: {}", id);
    }

    // =============================================
    // STATISTICS
    // =============================================
    
    @Override
    public long getTotalCount() {
        return branchRepository.count();
    }
    
    @Override
    public long getActiveCount() {
        return branchRepository.countActiveBranches();
    }

    // =============================================
    // PRIVATE HELPER METHODS
    // =============================================
    
    /**
     * Find branch entity by ID or throw exception.
     */
    private Branch findBranchEntityById(String id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Branch not found with ID: " + id
                ));
    }
    
    /**
     * Validate name uniqueness.
     */
    private void validateNameUniqueness(String name, String excludeId) {
        Optional<Branch> existing = branchRepository.findByNameIgnoreCase(name);
        if (existing.isPresent() && !existing.get().getId().equals(excludeId)) {
            throw new DuplicateResourceException(
                "Branch already exists with name: " + name
            );
        }
    }
    
    /**
     * Validate code uniqueness.
     */
    private void validateCodeUniqueness(String code, String excludeId) {
        Optional<Branch> existing = branchRepository.findByCodeIgnoreCase(code);
        if (existing.isPresent() && !existing.get().getId().equals(excludeId)) {
            throw new DuplicateResourceException(
                "Branch already exists with code: " + code
            );
        }
    }
    
    /**
     * Create Sort object from field and direction.
     */
    private Sort createSort(String sortField, String sortDirection) {
        if (!StringUtils.hasText(sortField)) {
            return Sort.by(Sort.Direction.ASC, "name");
        }
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
            
        return Sort.by(direction, sortField);
    }
}
