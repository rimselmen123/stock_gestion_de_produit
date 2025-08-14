package com.example.stock.controller;

import com.example.stock.dto.common.ApiResponse;
import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.unit.UnitCreateDTO;
import com.example.stock.dto.unit.UnitResponseDTO;
import com.example.stock.dto.unit.UnitUpdateDTO;
import com.example.stock.service.UnitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Unit management operations.
 * Provides CRUD endpoints for unit entities with proper validation and error
 * handling.
 * 
 * @author Generated
 * @since 1.0
 */
@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class UnitController {

    private final UnitService unitService;

    /**
     * Get all units with filtering and pagination.
     * 
     * @param search        Search term for name and symbol fields
     * @param name          Filter by name (contains)
     * @param symbol        Filter by symbol (contains)
     * @param createdFrom   Filter created_at from (ISO-8601)
     * @param createdTo     Filter created_at to (ISO-8601)
     * @param updatedFrom   Filter updated_at from (ISO-8601)
     * @param updatedTo     Filter updated_at to (ISO-8601)
     * @param page          Page number (1-based)
     * @param perPage       Items per page
     * @param sortField     Field to sort by
     * @param sortDirection Sort direction (asc/desc)
     * @return Paginated response with units
     */
    @GetMapping
    public ResponseEntity<PaginatedResponse<UnitResponseDTO>> getAllUnits(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String symbol,
            @RequestParam(name = "created_from", required = false) String createdFrom,
            @RequestParam(name = "created_to", required = false) String createdTo,
            @RequestParam(name = "updated_from", required = false) String updatedFrom,
            @RequestParam(name = "updated_to", required = false) String updatedTo,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "5") int perPage,
            @RequestParam(name = "sort_field", defaultValue = "created_at") String sortField,
            @RequestParam(name = "sort_direction", defaultValue = "desc") String sortDirection) {

        log.debug("Getting units with filters - search: {}, name: {}, symbol: {}, createdFrom: {}, createdTo: {}, updatedFrom: {}, updatedTo: {}, page: {}, perPage: {}, sortField: {}, sortDirection: {}",
                search, name, symbol, createdFrom, createdTo, updatedFrom, updatedTo, page, perPage, sortField, sortDirection);

        // Always use the advanced filters method for backward compatibility too.
        PaginatedResponse<UnitResponseDTO> response = unitService.findAllWithFilters(
                search, name, symbol, createdFrom, createdTo, updatedFrom, updatedTo, page, perPage, sortField, sortDirection);

        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific unit by ID.
     * 
     * @param id Unit ID
     * @return Unit data or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UnitResponseDTO>> getUnitById(@PathVariable String id) {

        log.debug("Getting unit with ID: {}", id);

        UnitResponseDTO unit = unitService.findByIdOrThrow(id);
        ApiResponse<UnitResponseDTO> response = ApiResponse.success(unit);

        return ResponseEntity.ok(response);
    }

    /**
     * Create a new unit.
     * 
     * @param createDTO Unit creation data
     * @return Created unit data
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UnitResponseDTO>> createUnit(
            @Valid @RequestBody UnitCreateDTO createDTO) {

        log.info("Creating new unit with name: {}", createDTO.getName());

        UnitResponseDTO createdUnit = unitService.create(createDTO);
        ApiResponse<UnitResponseDTO> response = ApiResponse.success(
                createdUnit, "Unit created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing unit.
     * 
     * @param id        Unit ID to update
     * @param updateDTO Unit update data
     * @return Updated unit data
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UnitResponseDTO>> updateUnit(
            @PathVariable String id,
            @Valid @RequestBody UnitUpdateDTO updateDTO) {

        log.info("Updating unit with ID: {}", id);

        UnitResponseDTO updatedUnit = unitService.update(id, updateDTO);
        ApiResponse<UnitResponseDTO> response = ApiResponse.success(
                updatedUnit, "Unit updated successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Delete a unit by ID.
     * 
     * @param id Unit ID to delete
     * @return Success message or error if unit is in use
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUnit(@PathVariable String id) {

        log.info("Deleting unit with ID: {}", id);

        unitService.delete(id);
        ApiResponse<Void> response = ApiResponse.success("Unit deleted successfully");

        return ResponseEntity.ok(response);
    }
}