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

    private static final String CREATED_AT_FIELD = "createdAt";
    private static final String UPDATED_AT_FIELD = "updatedAt";
    private static final String CATEGORY_ENTITY = "Category";

    private final InventoryItemCategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<CategoryResponseDTO> findAllWithFilters(
            String search, String name, String branchId, String departmentId,
            String createdFrom, String createdTo,
            String updatedFrom, String updatedTo,
            int page, int perPage, String sortField, String sortDirection) {

        log.debug("Finding categories with filters - search: {}, name: {}, branchId: {}, departmentId: {}, createdFrom: {}, createdTo: {}, updatedFrom: {}, updatedTo: {}, page: {}, perPage: {}",
            search, name, branchId, departmentId, createdFrom, createdTo, updatedFrom, updatedTo, page, perPage);

        Pageable pageable = createPageable(page, perPage, sortField, sortDirection);
        Specification<InventoryItemCategory> spec = buildFilterSpecification(search, name, branchId, departmentId, createdFrom, createdTo, updatedFrom, updatedTo);
        
        Page<InventoryItemCategory> categoryPage = categoryRepository.findAll(spec, pageable);
        List<CategoryResponseDTO> categoryDTOs = categoryPage.getContent().stream()
                .map(this::convertToResponseDTO)
                .toList();

        // Create pagination info with current_page = 0 if no data
        PaginationInfo paginationInfo = new PaginationInfo(
            categoryPage.isEmpty() ? 0 : categoryPage.getNumber() + 1, // current_page (0 if no data, 1-based otherwise)
            categoryPage.getSize(), // per_page
            categoryPage.getTotalElements(), // total
            categoryPage.getTotalPages() // last_page
        );
        
        return PaginatedResponse.<CategoryResponseDTO>builder()
                .data(categoryDTOs)
                .pagination(paginationInfo)
                .build();
    }

    private Pageable createPageable(int page, int perPage, String sortField, String sortDirection) {
        page = Math.max(1, page);
        perPage = Math.min(Math.max(1, perPage), 100);
        
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, mapSortField(sortField));
        
        return PageRequest.of(page - 1, perPage, sort);
    }

    private Specification<InventoryItemCategory> buildFilterSpecification(
            String search, String name, String branchId, String departmentId,
            String createdFrom, String createdTo,
            String updatedFrom, String updatedTo) {
        
        return (root, query, criteriaBuilder) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            addSearchFilter(predicates, root, criteriaBuilder, search);
            addNameFilter(predicates, root, criteriaBuilder, name);
            addBranchFilter(predicates, root, criteriaBuilder, branchId);
            addDepartmentFilter(predicates, root, criteriaBuilder, departmentId);
            addDateRangeFilters(predicates, root, criteriaBuilder, createdFrom, createdTo, updatedFrom, updatedTo);

            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    private void addSearchFilter(java.util.List<jakarta.persistence.criteria.Predicate> predicates, 
                                jakarta.persistence.criteria.Root<InventoryItemCategory> root, 
                                jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder, 
                                String search) {
        if (search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            predicates.add(criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern)
            ));
        }
    }

    private void addNameFilter(java.util.List<jakarta.persistence.criteria.Predicate> predicates, 
                              jakarta.persistence.criteria.Root<InventoryItemCategory> root, 
                              jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder, 
                              String name) {
        if (name != null && !name.trim().isEmpty()) {
            String namePattern = "%" + name.toLowerCase() + "%";
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")), namePattern));
        }
    }

    private void addBranchFilter(java.util.List<jakarta.persistence.criteria.Predicate> predicates, 
                                jakarta.persistence.criteria.Root<InventoryItemCategory> root, 
                                jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder, 
                                String branchId) {
        if (branchId != null && !branchId.trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("branchId"), branchId));
        }
    }

    private void addDepartmentFilter(java.util.List<jakarta.persistence.criteria.Predicate> predicates, 
                                    jakarta.persistence.criteria.Root<InventoryItemCategory> root, 
                                    jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder, 
                                    String departmentId) {
        if (departmentId != null && !departmentId.trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("departmentId"), departmentId));
        }
    }

    private void addDateRangeFilters(java.util.List<jakarta.persistence.criteria.Predicate> predicates, 
                                    jakarta.persistence.criteria.Root<InventoryItemCategory> root, 
                                    jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
                                    String createdFrom, String createdTo, 
                                    String updatedFrom, String updatedTo) {
        addDateFilter(predicates, root, criteriaBuilder, createdFrom, CREATED_AT_FIELD, true);
        addDateFilter(predicates, root, criteriaBuilder, createdTo, CREATED_AT_FIELD, false);
        addDateFilter(predicates, root, criteriaBuilder, updatedFrom, UPDATED_AT_FIELD, true);
        addDateFilter(predicates, root, criteriaBuilder, updatedTo, UPDATED_AT_FIELD, false);
    }

    private void addDateFilter(java.util.List<jakarta.persistence.criteria.Predicate> predicates, 
                              jakarta.persistence.criteria.Root<InventoryItemCategory> root, 
                              jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
                              String dateString, String fieldName, boolean isFromDate) {
        if (dateString != null) {
            try {
                LocalDateTime date = LocalDateTime.parse(dateString);
                if (isFromDate) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), date));
                } else {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), date));
                }
            } catch (Exception e) {
                log.warn("Invalid {} date format: {}", isFromDate ? "from" : "to", dateString);
            }
        }
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

        // Create pagination info with current_page = 0 if no data
        PaginationInfo paginationInfo = new PaginationInfo(
            categoryPage.isEmpty() ? 0 : categoryPage.getNumber() + 1, // current_page (0 if no data, 1-based otherwise)
            categoryPage.getSize(), // per_page
            categoryPage.getTotalElements(), // total
            categoryPage.getTotalPages() // last_page
        );

        return PaginatedResponse.<CategoryResponseDTO>builder()
                .data(categoryDTOs)
                .pagination(paginationInfo)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO findByIdOrThrow(String id) {
        log.debug("Finding category by ID: {}", id);

        InventoryItemCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_ENTITY, id));

        return convertToResponseDTO(category);
    }

    @Override
    public CategoryResponseDTO create(CategoryCreateDTO createDTO) {
        log.info("Creating new category with name: {}", createDTO.getName());

        // Create entity
        InventoryItemCategory category = InventoryItemCategory.builder()
            .id(UUID.randomUUID().toString())
            .name(createDTO.getName())
            .branchId(createDTO.getBranchId())
            .departmentId(createDTO.getDepartmentId())
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
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_ENTITY, id));

        // Update fields
        existingCategory.setName(updateDTO.getName());
        existingCategory.setBranchId(updateDTO.getBranchId());
        existingCategory.setDepartmentId(updateDTO.getDepartmentId());
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
            .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_ENTITY, id));
        
        // Check if category has associated inventory items using count query
        long inventoryItemCount = categoryRepository.countInventoryItemsByCategoryId(id);
        if (inventoryItemCount > 0) {
            throw new DeleteConstraintException(CATEGORY_ENTITY, id, "Category is referenced by inventory items");
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
            category.getDepartmentId(),
            category.getCreatedAt(),
            category.getUpdatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findByBranchId(String branchId) {
        log.debug("Finding categories by branch ID: {}", branchId);
        List<InventoryItemCategory> categories = categoryRepository.findByBranchId(branchId);
        return categories.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findByDepartmentId(String departmentId) {
        log.debug("Finding categories by department ID: {}", departmentId);
        List<InventoryItemCategory> categories = categoryRepository.findByDepartmentId(departmentId);
        return categories.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findByBranchIdAndDepartmentId(String branchId, String departmentId) {
        log.debug("Finding categories by branch ID: {} and department ID: {}", branchId, departmentId);
        List<InventoryItemCategory> categories = categoryRepository.findByBranchIdAndDepartmentId(branchId, departmentId);
        return categories.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }
    
    /**
     * Map API sort field names to entity field names.
     */
    private String mapSortField(String sortField) {
        return switch (sortField) {
            case "created_at" -> CREATED_AT_FIELD;
            case "updated_at" -> UPDATED_AT_FIELD;
            default -> sortField;
        };
    }
}