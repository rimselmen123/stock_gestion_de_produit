package com.example.stock.service.impl;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.common.PaginationInfo;
import com.example.stock.dto.category.CategoryCreateDTO;
import com.example.stock.dto.category.CategoryResponseDTO;
import com.example.stock.dto.category.CategoryUpdateDTO;
import com.example.stock.entity.InventoryItemCategory;
import com.example.stock.exception.DeleteConstraintException;
import com.example.stock.exception.ResourceNotFoundException;
import com.example.stock.repository.InventoryItemCategoryRepository;
import com.example.stock.service.InventoryItemCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of InventoryItemCategoryService interface.
 * Handles all business logic for Category entity operations.
 * 
 * @author Generated
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryItemCategoryServiceImpl implements InventoryItemCategoryService {

    private final InventoryItemCategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<CategoryResponseDTO> findAllWithFilters(
            String search, String name, String branchId,
            String createdFrom, String createdTo,
            String updatedFrom, String updatedTo,
            int page, int perPage, String sortField, String sortDirection) {

        log.debug("Finding categories with filters - search: {}, name: {}, branchId: {}, createdFrom: {}, createdTo: {}, updatedFrom: {}, updatedTo: {}, page: {}, perPage: {}",
                search, name, branchId, createdFrom, createdTo, updatedFrom, updatedTo, page, perPage);

        // Validate and adjust pagination parameters
        page = Math.max(1, page);
        perPage = Math.min(Math.max(1, perPage), 100);

        // Create sort object
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, mapSortField(sortField));

        Pageable pageable = PageRequest.of(page - 1, perPage, sort);

        // Build specification based on filters
        Specification<InventoryItemCategory> spec = (root, query, criteriaBuilder) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            // Search term (name or description)
            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern)
                ));
            }

            // Name filter
            if (name != null && !name.trim().isEmpty()) {
                String namePattern = "%" + name.toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), namePattern));
            }

            // Branch ID filter
            if (branchId != null && !branchId.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("branchId"), branchId));
            }

            // Created date range
            if (createdFrom != null) {
                try {
                    LocalDateTime from = LocalDateTime.parse(createdFrom);
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), from));
                } catch (Exception e) {
                    log.warn("Invalid createdFrom date format: {}", createdFrom);
                }
            }
            if (createdTo != null) {
                try {
                    LocalDateTime to = LocalDateTime.parse(createdTo);
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), to));
                } catch (Exception e) {
                    log.warn("Invalid createdTo date format: {}", createdTo);
                }
            }

            // Updated date range
            if (updatedFrom != null) {
                try {
                    LocalDateTime from = LocalDateTime.parse(updatedFrom);
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), from));
                } catch (Exception e) {
                    log.warn("Invalid updatedFrom date format: {}", updatedFrom);
                }
            }
            if (updatedTo != null) {
                try {
                    LocalDateTime to = LocalDateTime.parse(updatedTo);
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), to));
                } catch (Exception e) {
                    log.warn("Invalid updatedTo date format: {}", updatedTo);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        Page<InventoryItemCategory> categoryPage = categoryRepository.findAll(spec, pageable);

        List<CategoryResponseDTO> categoryDTOs = categoryPage.getContent().stream()
                .map(this::convertToResponseDTO)
                .toList();

        PaginationInfo paginationInfo = PaginationInfo.of(
                page, perPage, categoryPage.getTotalElements());

        return PaginatedResponse.of(categoryDTOs, paginationInfo);
    }



    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<CategoryResponseDTO> findAllWithPagination(
            String search, int page, int perPage, String sortField, String sortDirection) {

        log.debug("Finding categories with pagination - search: {}, page: {}, perPage: {}", search, page, perPage);

        // Validate and adjust pagination parameters
        page = Math.max(1, page);
        perPage = Math.min(Math.max(1, perPage), 100);

        // Create sort object
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, mapSortField(sortField));

        // Create pageable (Spring uses 0-based indexing)
        Pageable pageable = PageRequest.of(page - 1, perPage, sort);

        // Execute query
        Page<InventoryItemCategory> categoryPage;
        if (search != null && !search.trim().isEmpty()) {
            categoryPage = categoryRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
        } else {
            categoryPage = categoryRepository.findAll(pageable);
        }

        // Convert to DTOs
        List<CategoryResponseDTO> categoryDTOs = categoryPage.getContent().stream()
                .map(this::convertToResponseDTO)
                .toList();

        // Create pagination info
        PaginationInfo paginationInfo = PaginationInfo.of(page, perPage, categoryPage.getTotalElements());

        return PaginatedResponse.of(categoryDTOs, paginationInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO findByIdOrThrow(String id) {
        log.debug("Finding category by ID: {}", id);

        InventoryItemCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));

        return convertToResponseDTO(category);
    }

    @Override
    public CategoryResponseDTO create(CategoryCreateDTO createDTO) {
        log.info("Creating new category with name: {}", createDTO.getName());

        // Create entity
        InventoryItemCategory category = InventoryItemCategory.builder()
                .id(UUID.randomUUID().toString())
                .name(createDTO.getName())
                .branchId("default-branch") // Set default branch for now
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        InventoryItemCategory savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with ID: {}", savedCategory.getId());

        return convertToResponseDTO(savedCategory);
    }

    @Override
    public CategoryResponseDTO update(String id, CategoryUpdateDTO updateDTO) {
        log.info("Updating category with ID: {}", id);

        InventoryItemCategory existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));

        // Update fields
        existingCategory.setName(updateDTO.getName());
        existingCategory.setUpdatedAt(LocalDateTime.now());

        InventoryItemCategory updatedCategory = categoryRepository.save(existingCategory);
        log.info("Category updated successfully with ID: {}", updatedCategory.getId());

        return convertToResponseDTO(updatedCategory);
    }

    @Override
    public void delete(String id) {
        log.info("Deleting category with ID: {}", id);
        
        // Ensure category exists
        categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", id));
        
        // Check if category has associated inventory items using count query
        long inventoryItemCount = categoryRepository.countInventoryItemsByCategoryId(id);
        if (inventoryItemCount > 0) {
            throw new DeleteConstraintException("Category", id, "Category is referenced by inventory items");
        }
        
        categoryRepository.deleteById(id);
        log.info("Category deleted successfully with ID: {}", id);
    }
    
    /**
     * Convert InventoryItemCategory entity to CategoryResponseDTO.
     */
    private CategoryResponseDTO convertToResponseDTO(InventoryItemCategory category) {
        return new CategoryResponseDTO(
            category.getId(),
            category.getName(),
            category.getBranchId(),
            category.getCreatedAt(),
            category.getUpdatedAt()
        );
    }
    
    /**
     * Map API sort field names to entity field names.
     */
    private String mapSortField(String sortField) {
        return switch (sortField) {
            case "created_at" -> "createdAt";
            case "updated_at" -> "updatedAt";
            default -> sortField;
        };
    }
}