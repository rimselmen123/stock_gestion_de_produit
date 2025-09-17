package com.example.stock.controller;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.tax.TaxRequestDTO;
import com.example.stock.dto.tax.TaxFilterDTO;
import com.example.stock.dto.tax.TaxResponseDTO;
import com.example.stock.service.TaxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for Tax management operations.
 */
@RestController
@RequestMapping("/api/taxes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tax Management", description = "Operations related to tax management")
public class TaxController {

    private final TaxService taxService;

    @Operation(summary = "Create a new tax", description = "Create a new tax rate for a branch")
    @PostMapping
    public ResponseEntity<TaxResponseDTO> createTax(
            @Valid @RequestBody TaxRequestDTO createDTO) {
        log.info("Creating new tax: {}", createDTO.getName());
        TaxResponseDTO response = taxService.create(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing tax", description = "Update tax information by ID")
    @PutMapping("/{id}")
    public ResponseEntity<TaxResponseDTO> updateTax(
            @Parameter(description = "Tax ID") @PathVariable Long id,
            @Valid @RequestBody TaxRequestDTO updateDTO) {
        log.info("Updating tax with ID: {}", id);
        TaxResponseDTO response = taxService.update(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get tax by ID", description = "Retrieve tax information by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TaxResponseDTO> getTaxById(
            @Parameter(description = "Tax ID") @PathVariable Long id) {
        log.debug("Getting tax by ID: {}", id);
        TaxResponseDTO response = taxService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all taxes with pagination and filtering", description = "Retrieve paginated list of taxes with optional filters")
    @GetMapping
    public ResponseEntity<PaginatedResponse<TaxResponseDTO>> getAllTaxes(
            @Parameter(description = "Branch ID filter") @RequestParam(required = false) String branchId,
            @Parameter(description = "Tax name filter") @RequestParam(required = false) String name,
            @Parameter(description = "Minimum tax rate filter") @RequestParam(required = false) BigDecimal minRate,
            @Parameter(description = "Maximum tax rate filter") @RequestParam(required = false) BigDecimal maxRate,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("Getting all taxes with filters - branchId: {}, name: {}, page: {}, size: {}", 
                branchId, name, page, size);
        
        TaxFilterDTO filterDTO = TaxFilterDTO.builder()
                .branchId(branchId)
                .name(name)
                .minRate(minRate)
                .maxRate(maxRate)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .build();
        
        PaginatedResponse<TaxResponseDTO> response = taxService.findAll(filterDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get taxes by branch", description = "Retrieve all taxes for a specific branch")
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<TaxResponseDTO>> getTaxesByBranch(
            @Parameter(description = "Branch ID") @PathVariable String branchId) {
        log.debug("Getting taxes for branch: {}", branchId);
        List<TaxResponseDTO> response = taxService.findByBranchId(branchId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete tax", description = "Delete tax by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTax(
            @Parameter(description = "Tax ID") @PathVariable Long id) {
        log.info("Deleting tax with ID: {}", id);
        taxService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Calculate tax amount", description = "Calculate tax amount for a base amount using tax ID")
    @PostMapping("/{id}/calculate")
    public ResponseEntity<BigDecimal> calculateTaxAmount(
            @Parameter(description = "Tax ID") @PathVariable Long id,
            @Parameter(description = "Base amount") @RequestParam BigDecimal baseAmount) {
        log.debug("Calculating tax amount for tax ID: {} with base amount: {}", id, baseAmount);
        BigDecimal taxAmount = taxService.calculateTaxAmount(id, baseAmount);
        return ResponseEntity.ok(taxAmount);
    }

    @Operation(summary = "Calculate price with tax", description = "Calculate total price including tax using tax ID")
    @PostMapping("/{id}/calculate-total")
    public ResponseEntity<BigDecimal> calculatePriceWithTax(
            @Parameter(description = "Tax ID") @PathVariable Long id,
            @Parameter(description = "Base amount") @RequestParam BigDecimal baseAmount) {
        log.debug("Calculating price with tax for tax ID: {} with base amount: {}", id, baseAmount);
        BigDecimal totalPrice = taxService.calculatePriceWithTax(id, baseAmount);
        return ResponseEntity.ok(totalPrice);
    }

    @Operation(summary = "Calculate tax amount by rate", description = "Calculate tax amount using custom tax rate")
    @PostMapping("/calculate-by-rate")
    public ResponseEntity<BigDecimal> calculateTaxAmountByRate(
            @Parameter(description = "Tax rate percentage") @RequestParam BigDecimal taxRate,
            @Parameter(description = "Base amount") @RequestParam BigDecimal baseAmount) {
        log.debug("Calculating tax amount with rate: {} for base amount: {}", taxRate, baseAmount);
        BigDecimal taxAmount = taxService.calculateTaxAmount(taxRate, baseAmount);
        return ResponseEntity.ok(taxAmount);
    }

    @Operation(summary = "Calculate total price by rate", description = "Calculate total price including tax using custom tax rate")
    @PostMapping("/calculate-total-by-rate")
    public ResponseEntity<BigDecimal> calculatePriceWithTaxByRate(
            @Parameter(description = "Tax rate percentage") @RequestParam BigDecimal taxRate,
            @Parameter(description = "Base amount") @RequestParam BigDecimal baseAmount) {
        log.debug("Calculating total price with rate: {} for base amount: {}", taxRate, baseAmount);
        BigDecimal totalPrice = taxService.calculatePriceWithTax(taxRate, baseAmount);
        return ResponseEntity.ok(totalPrice);
    }

    @Operation(summary = "Get tax display name", description = "Get formatted display name for tax")
    @GetMapping("/{id}/display-name")
    public ResponseEntity<String> getTaxDisplayName(
            @Parameter(description = "Tax ID") @PathVariable Long id) {
        log.debug("Getting display name for tax ID: {}", id);
        String displayName = taxService.getDisplayName(id);
        return ResponseEntity.ok(displayName);
    }

    @Operation(summary = "Validate tax rate", description = "Check if tax rate is valid")
    @GetMapping("/validate-rate")
    public ResponseEntity<Boolean> validateTaxRate(
            @Parameter(description = "Tax rate to validate") @RequestParam BigDecimal rate) {
        log.debug("Validating tax rate: {}", rate);
        boolean isValid = taxService.isValidTaxRate(rate);
        return ResponseEntity.ok(isValid);
    }

    @Operation(summary = "Check if tax can be deleted", description = "Check if tax can be safely deleted")
    @GetMapping("/{id}/can-delete")
    public ResponseEntity<Boolean> canDeleteTax(
            @Parameter(description = "Tax ID") @PathVariable Long id) {
        log.debug("Checking if tax ID: {} can be deleted", id);
        boolean canDelete = taxService.canDelete(id);
        return ResponseEntity.ok(canDelete);
    }

    @Operation(summary = "Count taxes by branch", description = "Get number of taxes in a branch")
    @GetMapping("/branch/{branchId}/count")
    public ResponseEntity<Long> countTaxesByBranch(
            @Parameter(description = "Branch ID") @PathVariable String branchId) {
        log.debug("Counting taxes for branch: {}", branchId);
        long count = taxService.countByBranchId(branchId);
        return ResponseEntity.ok(count);
    }
}