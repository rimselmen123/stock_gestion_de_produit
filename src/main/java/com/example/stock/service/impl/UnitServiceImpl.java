package com.example.stock.service.impl;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.common.PaginationInfo;
import com.example.stock.dto.unit.UnitCreateDTO;
import com.example.stock.dto.unit.UnitResponseDTO;
import com.example.stock.dto.unit.UnitUpdateDTO;
import com.example.stock.entity.Unit;
import com.example.stock.exception.DeleteConstraintException;
import com.example.stock.exception.ResourceNotFoundException;
import com.example.stock.repository.UnitRepository;
import com.example.stock.service.UnitService;
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
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of UnitService interface.
 * Handles all business logic for Unit entity operations.
 * 
 * @author Generated
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UnitServiceImpl implements UnitService {
    
    private static final String CREATED_AT_FIELD = "createdAt";
    private static final String UPDATED_AT_FIELD = "updatedAt";
    
    private final UnitRepository unitRepository;
    
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<UnitResponseDTO> findAllWithFilters(
            String search,
            String name,
            String symbol,
            String createdFrom,
            String createdTo,
            String updatedFrom,
            String updatedTo,
            int page,
            int perPage,
            String sortField,
            String sortDirection) {

        log.debug("Finding units with filters - search: {}, name: {}, symbol: {}, createdFrom: {}, createdTo: {}, updatedFrom: {}, updatedTo: {}, page: {}, perPage: {}",
                search, name, symbol, createdFrom, createdTo, updatedFrom, updatedTo, page, perPage);

        // Validate and adjust pagination parameters
        page = Math.max(1, page);
        perPage = Math.min(Math.max(1, perPage), 100);

        // Create sort object
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, mapSortField(sortField));

        Pageable pageable = PageRequest.of(page - 1, perPage, sort);

        Specification<Unit> spec = buildSpecification(search, name, symbol, createdFrom, createdTo, updatedFrom, updatedTo);

        Page<Unit> unitPage = unitRepository.findAll(spec, pageable);

        List<UnitResponseDTO> unitDTOs = unitPage.getContent().stream()
            .map(this::convertToResponseDTO)
            .toList();

        // Create pagination info with proper current_page calculation
        int currentPage = unitDTOs.isEmpty() ? 0 : page;
        int totalPages = unitPage.getTotalPages();
        if (totalPages == 0) totalPages = 1;
        
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPage(currentPage);
        paginationInfo.setPerPage(perPage);
        paginationInfo.setTotal((int) unitPage.getTotalElements());
        paginationInfo.setLastPage(totalPages);
        
        return PaginatedResponse.of(unitDTOs, paginationInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<UnitResponseDTO> findAllWithPagination(
            String search, int page, int perPage, String sortField, String sortDirection) {
        
        log.debug("Finding units with pagination - search: {}, page: {}, perPage: {}", search, page, perPage);
        
        // Validate and adjust pagination parameters
        page = Math.max(1, page);
        perPage = Math.min(Math.max(1, perPage), 100);
        
        // Create sort object
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, mapSortField(sortField));
        
        // Create pageable (Spring uses 0-based indexing)
        Pageable pageable = PageRequest.of(page - 1, perPage, sort);
        
        // Execute query
        Page<Unit> unitPage;
        if (search != null && !search.trim().isEmpty()) {
            unitPage = unitRepository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(
                search.trim(), search.trim(), pageable);
        } else {
            unitPage = unitRepository.findAll(pageable);
        }
        
        // Convert to DTOs
        List<UnitResponseDTO> unitDTOs = unitPage.getContent().stream()
            .map(this::convertToResponseDTO)
            .toList();
        
        // Create pagination info
        PaginationInfo paginationInfo = PaginationInfo.of(page, perPage, unitPage.getTotalElements());
        
        return PaginatedResponse.of(unitDTOs, paginationInfo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UnitResponseDTO findByIdOrThrow(String id) {
        log.debug("Finding unit by ID: {}", id);
        
        Unit unit = unitRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Unit", id));
        
        return convertToResponseDTO(unit);
    }
    
    @Override
    public UnitResponseDTO create(UnitCreateDTO createDTO) {
        log.info("Creating new unit with name: {}", createDTO.getName());
        
        // Check if symbol already exists
        if (unitRepository.existsBySymbol(createDTO.getSymbol())) {
            throw new IllegalArgumentException("Unit with symbol '" + createDTO.getSymbol() + "' already exists");
        }
        
        // Create entity
        Unit unit = Unit.builder()
            .id(UUID.randomUUID().toString())
            .name(createDTO.getName())
            .symbol(createDTO.getSymbol())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        
        Unit savedUnit = unitRepository.save(unit);
        log.info("Unit created successfully with ID: {}", savedUnit.getId());
        
        return convertToResponseDTO(savedUnit);
    }
    
    @Override
    public UnitResponseDTO update(String id, UnitUpdateDTO updateDTO) {
        log.info("Updating unit with ID: {}", id);
        
        Unit existingUnit = unitRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Unit", id));
        
        // Check if symbol is being changed and if new symbol already exists
        if (!existingUnit.getSymbol().equals(updateDTO.getSymbol()) && 
            unitRepository.existsBySymbol(updateDTO.getSymbol())) {
            throw new IllegalArgumentException("Unit with symbol '" + updateDTO.getSymbol() + "' already exists");
        }
        
        // Update fields
        existingUnit.setName(updateDTO.getName());
        existingUnit.setSymbol(updateDTO.getSymbol());
        existingUnit.setUpdatedAt(LocalDateTime.now());
        
        Unit updatedUnit = unitRepository.save(existingUnit);
        log.info("Unit updated successfully with ID: {}", updatedUnit.getId());
        
        return convertToResponseDTO(updatedUnit);
    }
    
    @Override
    public void delete(String id) {
        log.info("Deleting unit with ID: {}", id);
        
        Unit unit = unitRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Unit", id));
        
        // Check if unit has associated inventory items
        if (unit.getInventoryItems() != null && !unit.getInventoryItems().isEmpty()) {
            throw new DeleteConstraintException("Unit", id, "Unit is referenced by inventory items");
        }
        
        unitRepository.deleteById(id);
        log.info("Unit deleted successfully with ID: {}", id);
    }
    
    /**
     * Convert Unit entity to UnitResponseDTO.
     */
    private UnitResponseDTO convertToResponseDTO(Unit unit) {
        return new UnitResponseDTO(
            unit.getId(),
            unit.getName(),
            unit.getSymbol(),
            unit.getCreatedAt(),
            unit.getUpdatedAt()
        );
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

    /**
     * Build dynamic specification from filter parameters.
     */
    private Specification<Unit> buildSpecification(
            String search,
            String name,
            String symbol,
            String createdFrom,
            String createdTo,
            String updatedFrom,
            String updatedTo) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            // search over name or symbol
            if (search != null && !search.trim().isEmpty()) {
                String like = "%" + search.trim().toLowerCase() + "%";
                var p = cb.or(
                        cb.like(cb.lower(root.get("name")), like),
                        cb.like(cb.lower(root.get("symbol")), like)
                );
                predicates = cb.and(predicates, p);
            }

            // name contains
            if (name != null && !name.trim().isEmpty()) {
                String like = "%" + name.trim().toLowerCase() + "%";
                predicates = cb.and(predicates, cb.like(cb.lower(root.get("name")), like));
            }

            // symbol contains
            if (symbol != null && !symbol.trim().isEmpty()) {
                String like = "%" + symbol.trim().toLowerCase() + "%";
                predicates = cb.and(predicates, cb.like(cb.lower(root.get("symbol")), like));
            }

            // created range
            if (createdFrom != null && !createdFrom.isBlank()) {
                LocalDateTime from = parseIsoDateTime(createdFrom, "createdFrom");
                predicates = cb.and(predicates, cb.greaterThanOrEqualTo(root.get(CREATED_AT_FIELD), from));
            }
            if (createdTo != null && !createdTo.isBlank()) {
                LocalDateTime to = parseIsoDateTime(createdTo, "createdTo");
                predicates = cb.and(predicates, cb.lessThanOrEqualTo(root.get(CREATED_AT_FIELD), to));
            }

            // updated range
            if (updatedFrom != null && !updatedFrom.isBlank()) {
                LocalDateTime from = parseIsoDateTime(updatedFrom, "updatedFrom");
                predicates = cb.and(predicates, cb.greaterThanOrEqualTo(root.get(UPDATED_AT_FIELD), from));
            }
            if (updatedTo != null && !updatedTo.isBlank()) {
                LocalDateTime to = parseIsoDateTime(updatedTo, "updatedTo");
                predicates = cb.and(predicates, cb.lessThanOrEqualTo(root.get(UPDATED_AT_FIELD), to));
            }

            return predicates;
        };
    }

    private LocalDateTime parseIsoDateTime(String value, String fieldName) {
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid ISO-8601 date-time for " + fieldName + ": " + value);
        }
    }
}