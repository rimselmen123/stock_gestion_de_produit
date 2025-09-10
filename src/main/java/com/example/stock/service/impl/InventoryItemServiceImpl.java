package com.example.stock.service.impl;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.common.PaginationInfo;
import com.example.stock.dto.category.CategoryResponseDTO;
import com.example.stock.dto.inventoryitem.InventoryItemCreateDTO;
import com.example.stock.dto.inventoryitem.InventoryItemResponseDTO;
import com.example.stock.dto.inventoryitem.InventoryItemUpdateDTO;
import com.example.stock.dto.unit.UnitResponseDTO;
import com.example.stock.entity.InventoryItem;
import com.example.stock.entity.InventoryItemCategory;
import com.example.stock.entity.Unit;
import com.example.stock.exception.ForeignKeyConstraintException;
import com.example.stock.exception.ResourceNotFoundException;
import com.example.stock.repository.InventoryItemCategoryRepository;
import com.example.stock.repository.InventoryItemRepository;
import com.example.stock.repository.UnitRepository;
import com.example.stock.service.InventoryItemService;
import com.example.stock.specification.InventoryItemSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of InventoryItemService interface.
 * Handles all business logic for Inventory Item entity operations.
 * 
 * @author Generated
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryItemServiceImpl implements InventoryItemService {
    
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryItemCategoryRepository categoryRepository;
    private final UnitRepository unitRepository;
    
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<InventoryItemResponseDTO> findAllWithFilters(
            String search, String name, String categoryId, String unitId,
            Integer minThreshold, Integer maxThreshold, Integer minReorder, Integer maxReorder,
            String createdFrom, String createdTo, String updatedFrom, String updatedTo,
            int page, int perPage, String sortField, String sortDirection) {
        
        log.debug("Finding inventory items with filters - search: {}, name: {}, categoryId: {}, unitId: {}, " +
                "minThreshold: {}, maxThreshold: {}, minReorder: {}, maxReorder: {}",
                search, name, categoryId, unitId, minThreshold, maxThreshold, minReorder, maxReorder);
        
        // Validate and adjust pagination parameters
        page = Math.max(1, page);
        perPage = Math.min(Math.max(1, perPage), 100);
        
        // Map sort field from DTO to entity field names
        String mappedSortField = mapSortField(sortField);
        
        // Create sort object
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Sort sort = Sort.by(direction, mappedSortField);
        
        // Create pageable (Spring uses 0-based indexing)
        Pageable pageable = PageRequest.of(page - 1, perPage, sort);
        
        // Create specification for filtering
        Specification<InventoryItem> spec = (root, query, criteriaBuilder) -> {
            var predicates = new java.util.ArrayList<Predicate>();
            
            // Apply search term (if provided) - search in name
            if (search != null && !search.trim().isEmpty()) {
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), 
                        "%" + search.trim().toLowerCase() + "%")
                ));
            }
            
            // Apply name filter
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.trim().toLowerCase() + "%"
                ));
            }
            
            // Apply category filter
            if (categoryId != null && !categoryId.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId.trim()));
            }
            
            // Apply unit filter
            if (unitId != null && !unitId.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("unit").get("id"), unitId.trim()));
            }
            
            // Apply threshold range filters
            if (minThreshold != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("thresholdQuantity"), minThreshold));
            }
            if (maxThreshold != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("thresholdQuantity"), maxThreshold));
            }
            
            // Apply reorder quantity range filters
            if (minReorder != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("reorderQuantity"), minReorder));
            }
            if (maxReorder != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("reorderQuantity"), maxReorder));
            }
            
            // Apply date range filters
            try {
                if (createdFrom != null && !createdFrom.trim().isEmpty()) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        LocalDateTime.parse(createdFrom.trim())
                    ));
                }
                if (createdTo != null && !createdTo.trim().isEmpty()) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("createdAt"),
                        LocalDateTime.parse(createdTo.trim())
                    ));
                }
                if (updatedFrom != null && !updatedFrom.trim().isEmpty()) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("updatedAt"),
                        LocalDateTime.parse(updatedFrom.trim())
                    ));
                }
                if (updatedTo != null && !updatedTo.trim().isEmpty()) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("updatedAt"),
                        LocalDateTime.parse(updatedTo.trim())
                    ));
                }
            } catch (Exception e) {
                log.warn("Invalid date format in filter parameters: {}", e.getMessage());
                // Continue without date filtering if date parsing fails
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        // Execute query with specifications and pagination
        Page<InventoryItem> itemPage = inventoryItemRepository.findAll(spec, pageable);
        
        // Convert to DTOs
        List<InventoryItemResponseDTO> itemDTOs = itemPage.getContent().stream()
                .map(this::convertToResponseDTO)
                .toList();
        
        // Create pagination info
        PaginationInfo paginationInfo = new PaginationInfo(
                itemPage.getNumber() + 1,
                itemPage.getSize(),
                (int) itemPage.getTotalElements(),
                itemPage.getTotalPages()
        );
        
        return PaginatedResponse.of(itemDTOs, paginationInfo);
    }
    
    /**
     * Searches for inventory items based on various filter criteria with pagination and sorting.
     * 
     * This method implements advanced filtering using JPA Specifications to build dynamic queries.
     * All parameters are optional - if a parameter is null or empty, it won't be used for filtering.
     * 
     * @param name Filter by item name (partial match, case-insensitive)
     * @param unit Filter by unit ID (exact match)
     * @param unitName Filter by unit name (partial match, case-insensitive)
     * @param category Filter by category ID (exact match)
     * @param categoryName Filter by category name (partial match, case-insensitive)
     * @param page Page number (1-based)
     * @param size Number of items per page (max 100)
     * @param sortBy Field to sort by (will be mapped to entity field names)
     * @param sortDirection Sort direction ('asc' or 'desc')
     * @return Paginated response containing filtered items and pagination metadata
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<InventoryItemResponseDTO> searchItems(
            String name, String unit, String unitName, String category, String categoryName,
            int page, int size, String sortBy, String sortDirection) {
        
        log.debug("Searching inventory items with filters - name: {}, unit: {}, unitName: {}, category: {}, categoryName: {}",
                name, unit, unitName, category, categoryName);
        
        // Ensure page number is at least 1
        page = Math.max(1, page);
        // Ensure page size is between 1 and 100
        size = Math.min(Math.max(1, size), 100);
        
        // Map API field names to entity field names for sorting
        String mappedSortField = mapSortField(sortBy);
        
        // Create sort direction (default to ascending if invalid)
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(sortDirection);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid sort direction: {}. Defaulting to 'asc'.", sortDirection);
            direction = Sort.Direction.ASC;
        }
        
        // Create sort object with the specified field and direction
        Sort sort = Sort.by(direction, mappedSortField);
        
        // Create pageable with 0-based page index (Spring Data JPA convention)
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        
        // Build the specification by combining all provided filters
        // Each filter is optional - if the parameter is null/empty, it won't affect the results
        Specification<InventoryItem> spec = (root, query, criteriaBuilder) -> {
            // Create a list to hold all predicates
            List<Predicate> predicates = new ArrayList<>();
            
            // Add each filter condition if the parameter is provided
            if (StringUtils.hasText(name)) {
                predicates.add(InventoryItemSpecifications.withName(name).toPredicate(root, query, criteriaBuilder));
            }
            if (StringUtils.hasText(unit)) {
                predicates.add(InventoryItemSpecifications.withUnitId(unit).toPredicate(root, query, criteriaBuilder));
            }
            if (StringUtils.hasText(unitName)) {
                predicates.add(InventoryItemSpecifications.withUnitName(unitName).toPredicate(root, query, criteriaBuilder));
            }
            if (StringUtils.hasText(category)) {
                predicates.add(InventoryItemSpecifications.withCategoryId(category).toPredicate(root, query, criteriaBuilder));
            }
            if (StringUtils.hasText(categoryName)) {
                predicates.add(InventoryItemSpecifications.withCategoryName(categoryName).toPredicate(root, query, criteriaBuilder));
            }
            
            // Combine all predicates with AND operator
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        // Execute the query with the built specification and pagination
        Page<InventoryItem> itemPage = inventoryItemRepository.findAll(spec, pageable);
        
        // Convert the page of entities to a list of DTOs
        List<InventoryItemResponseDTO> itemDTOs = itemPage.getContent().stream()
                .map(this::convertToResponseDTO)
                .toList();
        
        // Create pagination metadata for the response
        PaginationInfo paginationInfo = new PaginationInfo(
                itemPage.getNumber() + 1,  // Convert back to 1-based page number
                itemPage.getSize(),        // Page size (items per page)
                (int) itemPage.getTotalElements(),  // Total number of items
                itemPage.getTotalPages()   // Total number of pages
        );
        
        // Return the paginated response with data and metadata
        return PaginatedResponse.of(itemDTOs, paginationInfo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<InventoryItemResponseDTO> findAllWithPagination(
            String search, int page, int perPage, String sortField, String sortDirection) {
        
        log.debug("Finding inventory items with pagination - search: {}, page: {}, perPage: {}", search, page, perPage);
        
        // Delegate to the new method with all filter parameters set to null
        return findAllWithFilters(
            search, // search
            null,   // name
            null,   // categoryId
            null,   // unitId
            null,   // minThreshold
            null,   // maxThreshold
            null,   // minReorder
            null,   // maxReorder
            null,   // createdFrom
            null,   // createdTo
            null,   // updatedFrom
            null,   // updatedTo
            page,   // page
            perPage, // perPage
            sortField, // sortField
            sortDirection // sortDirection
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public InventoryItemResponseDTO findByIdOrThrow(String id) {
        log.debug("Finding inventory item by ID: {}", id);
        
        InventoryItem inventoryItem = inventoryItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory item", id));
        
        return convertToResponseDTO(inventoryItem);
    }
    
    @Override
    public InventoryItemResponseDTO create(InventoryItemCreateDTO createDTO) {
        log.info("Creating new inventory item with name: {}", createDTO.getName());
        
        // Validate foreign keys
        InventoryItemCategory category = categoryRepository.findById(createDTO.getInventoryItemCategoryId())
            .orElseThrow(() -> new ForeignKeyConstraintException("inventory_item_category_id", createDTO.getInventoryItemCategoryId()));
        
        Unit unit = unitRepository.findById(createDTO.getUnitId())
            .orElseThrow(() -> new ForeignKeyConstraintException("unit_id", createDTO.getUnitId()));
        
        // Create entity
        InventoryItem inventoryItem = InventoryItem.builder()
            .id(UUID.randomUUID().toString())
            .name(createDTO.getName())
            .thresholdQuantity(createDTO.getThresholdQuantity())
            .reorderQuantity(createDTO.getReorderQuantity())
            .category(category)
            .unit(unit)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem);
        log.info("Inventory item created successfully with ID: {}", savedInventoryItem.getId());
        
        return convertToResponseDTO(savedInventoryItem);
    }
    
    @Override
    public InventoryItemResponseDTO update(String id, InventoryItemUpdateDTO updateDTO) {
        log.info("Updating inventory item with ID: {}", id);
        
        InventoryItem existingInventoryItem = inventoryItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory item", id));
        
        // Validate foreign keys
        InventoryItemCategory category = categoryRepository.findById(updateDTO.getInventoryItemCategoryId())
            .orElseThrow(() -> new ForeignKeyConstraintException("inventory_item_category_id", updateDTO.getInventoryItemCategoryId()));
        
        Unit unit = unitRepository.findById(updateDTO.getUnitId())
            .orElseThrow(() -> new ForeignKeyConstraintException("unit_id", updateDTO.getUnitId()));
        
        // Update fields
        existingInventoryItem.setName(updateDTO.getName());
        existingInventoryItem.setThresholdQuantity(updateDTO.getThresholdQuantity());
        existingInventoryItem.setReorderQuantity(updateDTO.getReorderQuantity());
        existingInventoryItem.setCategory(category);
        existingInventoryItem.setUnit(unit);
        existingInventoryItem.setUpdatedAt(LocalDateTime.now());
        
        InventoryItem updatedInventoryItem = inventoryItemRepository.save(existingInventoryItem);
        log.info("Inventory item updated successfully with ID: {}", updatedInventoryItem.getId());
        
        return convertToResponseDTO(updatedInventoryItem);
    }
    
    @Override
    public void delete(String id) {
        log.info("Deleting inventory item with ID: {}", id);
        
        // Ensure inventory item exists
        inventoryItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory item", id));
        
        // Check if inventory item has stock records or movement history
        // For now, we'll just delete it - in a real system you'd check for references
        
        inventoryItemRepository.deleteById(id);
        log.info("Inventory item deleted successfully with ID: {}", id);
    }
    
    /**
     * Convert InventoryItem entity to InventoryItemResponseDTO.
     */
    private InventoryItemResponseDTO convertToResponseDTO(InventoryItem inventoryItem) {
        // Convert category
        CategoryResponseDTO categoryDTO = new CategoryResponseDTO(
            inventoryItem.getCategory().getId(),
            inventoryItem.getCategory().getName(),
            inventoryItem.getCategory().getDepartmentId(),
            inventoryItem.getCategory().getCreatedAt(),
            inventoryItem.getCategory().getUpdatedAt()
        );
        
        // Convert unit
        UnitResponseDTO unitDTO = new UnitResponseDTO(
            inventoryItem.getUnit().getId(),
            inventoryItem.getUnit().getName(),
            inventoryItem.getUnit().getSymbol(),
            inventoryItem.getUnit().getCreatedAt(),
            inventoryItem.getUnit().getUpdatedAt()
        );
        
        return new InventoryItemResponseDTO(
            inventoryItem.getId(),                    // id
            inventoryItem.getName(),                  // name
            inventoryItem.getCategory().getId(),      // inventoryItemCategoryId
            inventoryItem.getUnit().getId(),          // unitId
            inventoryItem.getThresholdQuantity(),     // thresholdQuantity
            inventoryItem.getReorderQuantity(),       // reorderQuantity
            inventoryItem.getCreatedAt(),             // createdAt
            inventoryItem.getUpdatedAt(),             // updatedAt
            categoryDTO,                                   // category
            unitDTO                                        // unit
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