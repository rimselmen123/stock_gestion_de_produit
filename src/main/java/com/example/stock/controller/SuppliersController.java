package com.example.stock.controller;

import com.example.stock.dto.common.ApiResponse;
import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.suppliers.SupplierCreateDTO;
import com.example.stock.dto.suppliers.SupplierResponseDTO;
import com.example.stock.dto.suppliers.SupplierUpdateDTO;
import com.example.stock.service.SuppliersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Suppliers management operations.
 * Provides CRUD endpoints for supplier entities with proper validation and error handling.
 * 
 * @author Generated
 * @since 1.0
 */
@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Suppliers", description = "APIs for managing suppliers")
public class SuppliersController {

    private final SuppliersService suppliersService;

    /**
     * Get all suppliers with advanced filtering and pagination.
     */
    @GetMapping
    @Operation(summary = "Get all suppliers with advanced filtering and pagination")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved suppliers")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid parameters provided")
    public ResponseEntity<PaginatedResponse<SupplierResponseDTO>> getAllSuppliers(
            @Parameter(description = "Search term for name, email, and phone fields")
            @RequestParam(required = false) String search,
            
            @Parameter(description = "Filter by name (contains)")
            @RequestParam(required = false) String name,
            
            @Parameter(description = "Filter by email (contains)")
            @RequestParam(required = false) String email,
            
            @Parameter(description = "Filter by phone (contains)")
            @RequestParam(required = false) String phone,
            
            @Parameter(description = "Filter by address (contains)")
            @RequestParam(required = false) String address,
            
            @Parameter(description = "Filter by description (contains)")
            @RequestParam(required = false) String description,
            
            @Parameter(description = "Filter created_at from (ISO-8601)")
            @RequestParam(name = "created_from", required = false) String createdFrom,
            
            @Parameter(description = "Filter created_at to (ISO-8601)")
            @RequestParam(name = "created_to", required = false) String createdTo,
            
            @Parameter(description = "Filter updated_at from (ISO-8601)")
            @RequestParam(name = "updated_from", required = false) String updatedFrom,
            
            @Parameter(description = "Filter updated_at to (ISO-8601)")
            @RequestParam(name = "updated_to", required = false) String updatedTo,
            
            @Parameter(description = "Page number (1-based)", example = "1")
            @RequestParam(defaultValue = "1") int page,
            
            @Parameter(description = "Items per page", example = "5")
            @RequestParam(name = "per_page", defaultValue = "5") int perPage,
            
            @Parameter(description = "Field to sort by", example = "created_at")
            @RequestParam(name = "sort_field", defaultValue = "created_at") String sortField,
            
            @Parameter(description = "Sort direction (asc/desc)", example = "desc")
            @RequestParam(name = "sort_direction", defaultValue = "desc") String sortDirection) {
        
        log.debug("""
                Getting suppliers with filters - 
                search: {}, name: {}, email: {}, phone: {}, address: {}, description: {},
                createdFrom: {}, createdTo: {}, updatedFrom: {}, updatedTo: {},
                page: {}, perPage: {}, sortField: {}, sortDirection: {}""",
                search, name, email, phone, address, description,
                createdFrom, createdTo, updatedFrom, updatedTo,
                page, perPage, sortField, sortDirection);
        
        PaginatedResponse<SupplierResponseDTO> response = suppliersService.findAllWithFilters(
                search, name, email, phone, address, description,
                createdFrom, createdTo, updatedFrom, updatedTo,
                page, perPage, sortField, sortDirection);
        
        response.setMessage("Suppliers retrieved successfully");
        response.setSuccess(true);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific supplier by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a specific supplier by ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved supplier")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Supplier not found")
    public ResponseEntity<ApiResponse<SupplierResponseDTO>> getSupplierById(
            @Parameter(description = "Supplier ID") @PathVariable String id) {
        
        log.debug("Getting supplier with ID: {}", id);
        
        SupplierResponseDTO supplier = suppliersService.findByIdOrThrow(id);
        ApiResponse<SupplierResponseDTO> response = ApiResponse.success(supplier);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new supplier.
     */
    @PostMapping
    @Operation(summary = "Create a new supplier")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Supplier created successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Supplier with same name or email already exists")
    public ResponseEntity<ApiResponse<SupplierResponseDTO>> createSupplier(
            @Valid @RequestBody SupplierCreateDTO createDTO) {
        
        log.info("Creating new supplier with name: {}", createDTO.getName());
        
        SupplierResponseDTO createdSupplier = suppliersService.create(createDTO);
        ApiResponse<SupplierResponseDTO> response = ApiResponse.success(
            createdSupplier, "Supplier created successfully");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing supplier.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing supplier")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Supplier updated successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Supplier not found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Supplier with same name or email already exists")
    public ResponseEntity<ApiResponse<SupplierResponseDTO>> updateSupplier(
            @Parameter(description = "Supplier ID to update") @PathVariable String id,
            @Valid @RequestBody SupplierUpdateDTO updateDTO) {
        
        log.info("Updating supplier with ID: {}", id);
        
        SupplierResponseDTO updatedSupplier = suppliersService.update(id, updateDTO);
        ApiResponse<SupplierResponseDTO> response = ApiResponse.success(
            updatedSupplier, "Supplier updated successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a supplier by ID.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a supplier by ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Supplier deleted successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Supplier not found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Cannot delete supplier with associated inventory movements")
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(
            @Parameter(description = "Supplier ID to delete") @PathVariable String id) {
        
        log.info("Deleting supplier with ID: {}", id);
        
        suppliersService.delete(id);
        ApiResponse<Void> response = ApiResponse.success("Supplier deleted successfully");
        
        return ResponseEntity.ok(response);
    }
}
