package com.example.stock.controller;

import com.example.stock.dto.unit.UnitCreateDTO;
import com.example.stock.dto.unit.UnitResponseDTO;
import com.example.stock.dto.unit.UnitSummaryDTO;
import com.example.stock.dto.unit.UnitUpdateDTO;
import com.example.stock.entity.Unit;
import com.example.stock.mapper.UnitMapper;
import com.example.stock.service.UnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Unit management operations.
 * Provides CRUD endpoints for unit entities with proper validation and error handling.
 * 
 * @author Generated
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/units")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
@Tag(name = "Unit Management", description = "APIs for managing unit information")
public class UnitController {

    private final UnitService unitService;
    private final UnitMapper unitMapper;

    /**
     * Create a new unit.
     * 
     * @param createDTO the unit creation data
     * @return ResponseEntity with created unit data
     */
    @PostMapping
    @Operation(summary = "Create a new unit", description = "Creates a new unit with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Unit created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Unit with same symbol already exists")
    })
    public ResponseEntity<UnitResponseDTO> createUnit(
            @Valid @RequestBody UnitCreateDTO createDTO) {
        
        log.info("Creating new unit with symbol: {}", createDTO.getSymbol());
        
        Unit unitEntity = unitMapper.toEntity(createDTO);
        Unit createdUnit = unitService.createUnit(unitEntity);
        UnitResponseDTO responseDTO = unitMapper.toResponseDTO(createdUnit);
        
        log.info("Unit created successfully with ID: {}", createdUnit.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Get all units.
     * 
     * @return ResponseEntity with list of all units
     */
    @GetMapping
    @Operation(summary = "Get all units", description = "Retrieves a list of all units")
    @ApiResponse(responseCode = "200", description = "Units retrieved successfully")
    public ResponseEntity<List<UnitResponseDTO>> getAllUnits() {
        
        log.debug("Retrieving all units");
        
        List<Unit> units = unitService.findAllOrderByName();
        List<UnitResponseDTO> responseDTOs = unitMapper.toResponseDTOList(units);
        
        log.debug("Retrieved {} units", units.size());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Get units summary for listings.
     *
     * @return ResponseEntity with list of unit summaries
     */
    @GetMapping("/summary")
    @Operation(summary = "Get units summary", description = "Retrieves a lightweight list of units for dropdowns and listings")
    @ApiResponse(responseCode = "200", description = "Unit summaries retrieved successfully")
    public ResponseEntity<List<UnitSummaryDTO>> getUnitsSummary() {

        log.debug("Retrieving units summary");

        List<Unit> units = unitService.findAllOrderByName();
        List<UnitSummaryDTO> summaryDTOs = unitMapper.toSummaryDTOList(units);

        log.debug("Retrieved {} unit summaries", units.size());
        return ResponseEntity.ok(summaryDTOs);
    }

    /**
     * Get unit by ID.
     *
     * @param id the unit ID
     * @return ResponseEntity with unit data or 404 if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get unit by ID", description = "Retrieves a specific unit by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Unit found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Unit not found")
    })
    public ResponseEntity<UnitResponseDTO> getUnitById(
            @Parameter(description = "Unit ID", required = true)
            @PathVariable String id) {

        log.debug("Retrieving unit with ID: {}", id);

        Optional<Unit> unitOptional = unitService.findById(id);

        if (unitOptional.isPresent()) {
            UnitResponseDTO responseDTO = unitMapper.toResponseDTO(unitOptional.get());
            log.debug("Unit found with ID: {}", id);
            return ResponseEntity.ok(responseDTO);
        } else {
            log.warn("Unit not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update an existing unit.
     *
     * @param id the unit ID to update
     * @param updateDTO the unit update data
     * @return ResponseEntity with updated unit data
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update unit", description = "Updates an existing unit with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Unit updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Unit not found"),
        @ApiResponse(responseCode = "409", description = "Unit with same symbol already exists")
    })
    public ResponseEntity<UnitResponseDTO> updateUnit(
            @Parameter(description = "Unit ID", required = true)
            @PathVariable String id,
            @Valid @RequestBody UnitUpdateDTO updateDTO) {

        log.info("Updating unit with ID: {}", id);

        // Check if unit exists
        Optional<Unit> existingUnitOptional = unitService.findById(id);
        if (existingUnitOptional.isEmpty()) {
            log.warn("Unit not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        Unit unitToUpdate = unitMapper.toEntity(new UnitCreateDTO(
            updateDTO.getName(),
            updateDTO.getSymbol()
        ));

        Unit updatedUnit = unitService.updateUnit(id, unitToUpdate);
        UnitResponseDTO responseDTO = unitMapper.toResponseDTO(updatedUnit);

        log.info("Unit updated successfully with ID: {}", id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Delete a unit by ID.
     *
     * @param id the unit ID to delete
     * @return ResponseEntity with no content or error status
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete unit", description = "Deletes a unit by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Unit deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Unit not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete unit with associated inventory items")
    })
    public ResponseEntity<Void> deleteUnit(
            @Parameter(description = "Unit ID", required = true)
            @PathVariable String id) {

        log.info("Deleting unit with ID: {}", id);

        // Check if unit exists
        Optional<Unit> unitOptional = unitService.findById(id);
        if (unitOptional.isEmpty()) {
            log.warn("Unit not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        try {
            unitService.deleteById(id);
            log.info("Unit deleted successfully with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting unit with ID: {}, Error: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Search unit by symbol.
     *
     * @param symbol the unit symbol to search for
     * @return ResponseEntity with unit data or 404 if not found
     */
    @GetMapping("/search")
    @Operation(summary = "Search unit by symbol", description = "Finds a unit by its exact symbol")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Unit found"),
        @ApiResponse(responseCode = "404", description = "Unit not found")
    })
    public ResponseEntity<UnitResponseDTO> searchUnitBySymbol(
            @Parameter(description = "Unit symbol to search for", required = true)
            @RequestParam String symbol) {

        log.debug("Searching for unit with symbol: {}", symbol);

        Optional<Unit> unitOptional = unitService.findBySymbol(symbol);

        if (unitOptional.isPresent()) {
            UnitResponseDTO responseDTO = unitMapper.toResponseDTO(unitOptional.get());
            log.debug("Unit found with symbol: {}", symbol);
            return ResponseEntity.ok(responseDTO);
        } else {
            log.warn("Unit not found with symbol: {}", symbol);
            return ResponseEntity.notFound().build();
        }
    }
}
