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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    
    private final UnitRepository unitRepository;
    
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
            case "created_at" -> "createdAt";
            case "updated_at" -> "updatedAt";
            default -> sortField;
        };
    }
}