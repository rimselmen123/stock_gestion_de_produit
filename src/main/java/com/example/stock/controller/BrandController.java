package com.example.stock.controller;

import com.example.stock.dto.brand.BrandCreateDTO;
import com.example.stock.dto.brand.BrandResponseDTO;
import com.example.stock.dto.brand.BrandSummaryDTO;
import com.example.stock.dto.brand.BrandUpdateDTO;
import com.example.stock.entity.Brand;
import com.example.stock.mapper.BrandMapper;
import com.example.stock.service.BrandService;
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
 * REST Controller for Brand management operations.
 * Provides CRUD endpoints for brand entities with proper validation and error handling.
 * 
 * @author Generated
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Brand Management", description = "APIs for managing brand information")
public class BrandController {

    private final BrandService brandService;
    private final BrandMapper brandMapper;

    /**
     * Create a new brand.
     * 
     * @param createDTO the brand creation data
     * @return ResponseEntity with created brand data
     */
    @PostMapping
    @Operation(summary = "Create a new brand", description = "Creates a new brand with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Brand created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Brand with same name already exists")
    })
    public ResponseEntity<BrandResponseDTO> createBrand(
            @Valid @RequestBody BrandCreateDTO createDTO) {
        
        log.info("Creating new brand with name: {}", createDTO.getName());
        
        Brand brandEntity = brandMapper.toEntity(createDTO);
        Brand createdBrand = brandService.createBrand(brandEntity);
        BrandResponseDTO responseDTO = brandMapper.toResponseDTO(createdBrand);
        
        log.info("Brand created successfully with ID: {}", createdBrand.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Get all brands.
     * 
     * @return ResponseEntity with list of all brands
     */
    @GetMapping
    @Operation(summary = "Get all brands", description = "Retrieves a list of all brands")
    @ApiResponse(responseCode = "200", description = "Brands retrieved successfully")
    public ResponseEntity<List<BrandResponseDTO>> getAllBrands() {
        
        log.debug("Retrieving all brands");
        
        List<Brand> brands = brandService.findAll();
        List<BrandResponseDTO> responseDTOs = brandMapper.toResponseDTOList(brands);
        
        log.debug("Retrieved {} brands", brands.size());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Get brands summary for listings.
     * 
     * @return ResponseEntity with list of brand summaries
     */
    @GetMapping("/summary")
    @Operation(summary = "Get brands summary", description = "Retrieves a lightweight list of brands for dropdowns and listings")
    @ApiResponse(responseCode = "200", description = "Brand summaries retrieved successfully")
    public ResponseEntity<List<BrandSummaryDTO>> getBrandsSummary() {
        
        log.debug("Retrieving brands summary");
        
        List<Brand> brands = brandService.findAll();
        List<BrandSummaryDTO> summaryDTOs = brandMapper.toSummaryDTOList(brands);
        
        log.debug("Retrieved {} brand summaries", brands.size());
        return ResponseEntity.ok(summaryDTOs);
    }

    /**
     * Get brand by ID.
     *
     * @param id the brand ID
     * @return ResponseEntity with brand data or 404 if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get brand by ID", description = "Retrieves a specific brand by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Brand found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<BrandResponseDTO> getBrandById(
            @Parameter(description = "Brand ID", required = true)
            @PathVariable String id) {

        log.debug("Retrieving brand with ID: {}", id);

        Optional<Brand> brandOptional = brandService.findById(id);

        if (brandOptional.isPresent()) {
            BrandResponseDTO responseDTO = brandMapper.toResponseDTO(brandOptional.get());
            log.debug("Brand found with ID: {}", id);
            return ResponseEntity.ok(responseDTO);
        } else {
            log.warn("Brand not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update an existing brand.
     *
     * @param id the brand ID to update
     * @param updateDTO the brand update data
     * @return ResponseEntity with updated brand data
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update brand", description = "Updates an existing brand with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Brand updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Brand not found"),
        @ApiResponse(responseCode = "409", description = "Brand with same name already exists")
    })
    public ResponseEntity<BrandResponseDTO> updateBrand(
            @Parameter(description = "Brand ID", required = true)
            @PathVariable String id,
            @Valid @RequestBody BrandUpdateDTO updateDTO) {

        log.info("Updating brand with ID: {}", id);

        // Check if brand exists
        Optional<Brand> existingBrandOptional = brandService.findById(id);
        if (existingBrandOptional.isEmpty()) {
            log.warn("Brand not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        Brand brandToUpdate = brandMapper.toEntity(new BrandCreateDTO(
            updateDTO.getName(),
            updateDTO.getDescription(),
            updateDTO.getImageUrl()
        ));

        Brand updatedBrand = brandService.updateBrand(id, brandToUpdate);
        BrandResponseDTO responseDTO = brandMapper.toResponseDTO(updatedBrand);

        log.info("Brand updated successfully with ID: {}", id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Delete a brand by ID.
     *
     * @param id the brand ID to delete
     * @return ResponseEntity with no content or error status
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete brand", description = "Deletes a brand by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Brand deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Brand not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete brand with associated inventory items")
    })
    public ResponseEntity<Void> deleteBrand(
            @Parameter(description = "Brand ID", required = true)
            @PathVariable String id) {

        log.info("Deleting brand with ID: {}", id);

        // Check if brand exists
        Optional<Brand> brandOptional = brandService.findById(id);
        if (brandOptional.isEmpty()) {
            log.warn("Brand not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        try {
            brandService.deleteById(id);
            log.info("Brand deleted successfully with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting brand with ID: {}, Error: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Search brands by name.
     *
     * @param name the brand name to search for
     * @return ResponseEntity with brand data or 404 if not found
     */
    @GetMapping("/search")
    @Operation(summary = "Search brand by name", description = "Finds a brand by its exact name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Brand found"),
        @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<BrandResponseDTO> searchBrandByName(
            @Parameter(description = "Brand name to search for", required = true)
            @RequestParam String name) {

        log.debug("Searching for brand with name: {}", name);

        Brand brand = brandService.findByName(name);

        if (brand != null) {
            BrandResponseDTO responseDTO = brandMapper.toResponseDTO(brand);
            log.debug("Brand found with name: {}", name);
            return ResponseEntity.ok(responseDTO);
        } else {
            log.warn("Brand not found with name: {}", name);
            return ResponseEntity.notFound().build();
        }
    }
}
