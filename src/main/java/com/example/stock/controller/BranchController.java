package com.example.stock.controller;

import com.example.stock.dto.branch.BranchCreateDTO;
import com.example.stock.dto.branch.BranchFilterDTO;
import com.example.stock.dto.branch.BranchResponseDTO;
import com.example.stock.dto.branch.BranchSummaryDTO;
import com.example.stock.dto.branch.BranchUpdateDTO;
import com.example.stock.dto.common.ApiResponse;
import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for Branch management.
 * Provides comprehensive CRUD operations and advanced filtering.
 * 
 * @author Generated
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
@Tag(name = "Branch Management", description = "APIs for managing branches")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BranchController {

        private final BranchService branchService;

        // =============================================
        // CONSTANTS
        // =============================================

        private static final String BRANCH_RETRIEVED_SUCCESSFULLY = "Branch retrieved successfully";
        private static final String BRANCH_CREATED_SUCCESSFULLY = "Branch created successfully";
        private static final String BRANCH_UPDATED_SUCCESSFULLY = "Branch updated successfully";
        private static final String BRANCH_DELETED_SUCCESSFULLY = "Branch deleted successfully";

        // =============================================
        // CRUD OPERATIONS
        // =============================================

        @PostMapping
        @Operation(summary = "Create a new branch", description = "Creates a new branch with the provided details")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Branch created successfully"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Branch already exists")
        })
        public ResponseEntity<ApiResponse<BranchResponseDTO>> createBranch(
                        @Valid @RequestBody BranchCreateDTO createDTO) {

                log.info("Creating new branch: {}", createDTO.getName());

                BranchResponseDTO createdBranch = branchService.create(createDTO);

                ApiResponse<BranchResponseDTO> response = ApiResponse.<BranchResponseDTO>builder()
                                .success(true)
                                .message(BRANCH_CREATED_SUCCESSFULLY)
                                .data(createdBranch)
                                .build();

                log.info("Branch created successfully with ID: {}", createdBranch.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get branch by ID", description = "Retrieves a branch by its unique identifier")
        public ResponseEntity<ApiResponse<BranchResponseDTO>> getBranchById(
                        @Parameter(description = "Branch unique identifier") @PathVariable String id) {

                log.debug("Fetching branch with ID: {}", id);

                return branchService.findById(id)
                                .map(branch -> {
                                        ApiResponse<BranchResponseDTO> response = ApiResponse
                                                        .<BranchResponseDTO>builder()
                                                        .success(true)
                                                        .message(BRANCH_RETRIEVED_SUCCESSFULLY)
                                                        .data(branch)
                                                        .build();
                                        return ResponseEntity.ok(response);
                                })
                                .orElse(ResponseEntity.notFound().build());
        }

        @PutMapping("/{id}")
        @Operation(summary = "Update branch", description = "Updates an existing branch with the provided details")
        public ResponseEntity<ApiResponse<BranchResponseDTO>> updateBranch(
                        @Parameter(description = "Branch unique identifier") @PathVariable String id,
                        @Valid @RequestBody BranchUpdateDTO updateDTO) {

                log.info("Updating branch with ID: {}", id);

                BranchResponseDTO updatedBranch = branchService.update(id, updateDTO);

                ApiResponse<BranchResponseDTO> response = ApiResponse.<BranchResponseDTO>builder()
                                .success(true)
                                .message(BRANCH_UPDATED_SUCCESSFULLY)
                                .data(updatedBranch)
                                .build();

                log.info("Branch updated successfully with ID: {}", updatedBranch.getId());
                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete branch", description = "Permanently deletes a branch (only if no dependencies)")
        public ResponseEntity<ApiResponse<Void>> deleteBranch(
                        @Parameter(description = "Branch unique identifier") @PathVariable String id) {

                log.info("Deleting branch with ID: {}", id);

                branchService.deleteById(id);

                ApiResponse<Void> response = ApiResponse.<Void>builder()
                                .success(true)
                                .message(BRANCH_DELETED_SUCCESSFULLY)
                                .build();

                log.info("Branch deleted successfully with ID: {}", id);
                return ResponseEntity.ok(response);
        }

        // =============================================
        // SEARCH AND FILTERING
        // =============================================

        @GetMapping
        @Operation(summary = "Get all branches with filtering", description = "Retrieves branches with comprehensive filtering, pagination and sorting")
        public ResponseEntity<PaginatedResponse<BranchResponseDTO>> getAllBranches(
                        @Parameter(description = "Global search term") @RequestParam(required = false) String search,
                        @Parameter(description = "Filter by name") @RequestParam(required = false) String name,
                        @Parameter(description = "Filter by location") @RequestParam(required = false) String location,
                        @Parameter(description = "Filter by code") @RequestParam(required = false) String code,
                        @Parameter(description = "Filter by active status") @RequestParam(required = false) Boolean isActive,
                        @Parameter(description = "Filter by creation date (after)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
                        @Parameter(description = "Filter by creation date (before)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore,
                        @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
                        @Parameter(description = "Items per page") @RequestParam(name = "per_page", defaultValue = "5") int perPage,
                        @Parameter(description = "Sort field") @RequestParam(name = "sort_field", defaultValue = "createdAt") String sortField,
                        @Parameter(description = "Sort direction") @RequestParam(name = "sort_direction", defaultValue = "desc") String sortDirection) {

                log.debug("Fetching branches with filters - page: {}, perPage: {}", page, perPage);

                BranchFilterDTO filterDTO = BranchFilterDTO.builder()
                                .search(search)
                                .name(name)
                                .location(location)
                                .code(code)
                                .isActive(isActive)
                                .createdAfter(createdAfter)
                                .createdBefore(createdBefore)
                                .page(page - 1) // Convert to 0-based
                                .perPage(perPage)
                                .sortField(sortField)
                                .sortDirection(sortDirection)
                                .build();

                PaginatedResponse<BranchResponseDTO> branches = branchService.findAllWithFilters(filterDTO);
                
                branches.setMessage("Branches retrieved successfully");
                branches.setSuccess(true);

                return ResponseEntity.ok(branches);
        }

        @GetMapping("/search/name")
        @Operation(summary = "Search branches by name", description = "Search branches by name for autocomplete")
        public ResponseEntity<ApiResponse<List<BranchSummaryDTO>>> searchBranchesByName(
                        @Parameter(description = "Search term") @RequestParam String name,
                        @Parameter(description = "Include only active branches") @RequestParam(defaultValue = "true") boolean activeOnly) {

                log.debug("Searching branches by name: {}, activeOnly: {}", name, activeOnly);

                List<BranchSummaryDTO> branches = branchService.searchByName(name, activeOnly);

                ApiResponse<List<BranchSummaryDTO>> response = ApiResponse.<List<BranchSummaryDTO>>builder()
                                .success(true)
                                .message("Search completed successfully")
                                .data(branches)
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/by-name/{name}")
        @Operation(summary = "Get branch by name", description = "Retrieves a branch by its name (case-insensitive)")
        public ResponseEntity<ApiResponse<BranchResponseDTO>> getBranchByName(
                        @Parameter(description = "Branch name") @PathVariable String name) {

                log.debug("Fetching branch by name: {}", name);

                return branchService.findByName(name)
                                .map(branch -> {
                                        ApiResponse<BranchResponseDTO> response = ApiResponse
                                                        .<BranchResponseDTO>builder()
                                                        .success(true)
                                                        .message(BRANCH_RETRIEVED_SUCCESSFULLY)
                                                        .data(branch)
                                                        .build();
                                        return ResponseEntity.ok(response);
                                })
                                .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping("/by-code/{code}")
        @Operation(summary = "Get branch by code", description = "Retrieves a branch by its code (case-insensitive)")
        public ResponseEntity<ApiResponse<BranchResponseDTO>> getBranchByCode(
                        @Parameter(description = "Branch code") @PathVariable String code) {

                log.debug("Fetching branch by code: {}", code);

                return branchService.findByCode(code)
                                .map(branch -> {
                                        ApiResponse<BranchResponseDTO> response = ApiResponse
                                                        .<BranchResponseDTO>builder()
                                                        .success(true)
                                                        .message(BRANCH_RETRIEVED_SUCCESSFULLY)
                                                        .data(branch)
                                                        .build();
                                        return ResponseEntity.ok(response);
                                })
                                .orElse(ResponseEntity.notFound().build());
        }

        // =============================================
        // DROPDOWN AND UTILITY ENDPOINTS
        // =============================================

        @GetMapping("/dropdown/active")
        @Operation(summary = "Get active branches for dropdown", description = "Returns all active branches for dropdown menus")
        public ResponseEntity<ApiResponse<List<BranchSummaryDTO>>> getActiveBranchesDropdown() {

                log.debug("Fetching active branches for dropdown");

                List<BranchSummaryDTO> branches = branchService.findAllActiveBranches();

                ApiResponse<List<BranchSummaryDTO>> response = ApiResponse.<List<BranchSummaryDTO>>builder()
                                .success(true)
                                .message("Active branches retrieved successfully")
                                .data(branches)
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/dropdown/all")
        @Operation(summary = "Get all branches for dropdown", description = "Returns all branches (active and inactive) for admin dropdown menus")
        public ResponseEntity<ApiResponse<List<BranchSummaryDTO>>> getAllBranchesDropdown() {

                log.debug("Fetching all branches for dropdown");

                List<BranchSummaryDTO> branches = branchService.findAllBranches();

                ApiResponse<List<BranchSummaryDTO>> response = ApiResponse.<List<BranchSummaryDTO>>builder()
                                .success(true)
                                .message("All branches retrieved successfully")
                                .data(branches)
                                .build();

                return ResponseEntity.ok(response);
        }

        // =============================================
        // BUSINESS OPERATIONS
        // =============================================

        @PatchMapping("/{id}/soft-delete")
        @Operation(summary = "Soft delete branch", description = "Deactivates a branch (sets isActive = false)")
        public ResponseEntity<ApiResponse<Void>> softDeleteBranch(
                        @Parameter(description = "Branch unique identifier") @PathVariable String id) {

                log.info("Soft deleting branch with ID: {}", id);

                branchService.softDelete(id);

                ApiResponse<Void> response = ApiResponse.<Void>builder()
                                .success(true)
                                .message("Branch deactivated successfully")
                                .build();

                log.info("Branch soft deleted successfully with ID: {}", id);
                return ResponseEntity.ok(response);
        }

        @PatchMapping("/{id}/reactivate")
        @Operation(summary = "Reactivate branch", description = "Reactivates a branch (sets isActive = true)")
        public ResponseEntity<ApiResponse<Void>> reactivateBranch(
                        @Parameter(description = "Branch unique identifier") @PathVariable String id) {

                log.info("Reactivating branch with ID: {}", id);

                branchService.reactivate(id);

                ApiResponse<Void> response = ApiResponse.<Void>builder()
                                .success(true)
                                .message("Branch reactivated successfully")
                                .build();

                log.info("Branch reactivated successfully with ID: {}", id);
                return ResponseEntity.ok(response);
        }

        // =============================================
        // VALIDATION ENDPOINTS
        // =============================================

        @GetMapping("/exists/name/{name}")
        @Operation(summary = "Check if branch name exists", description = "Validates if a branch name is already taken")
        public ResponseEntity<ApiResponse<Boolean>> checkNameExists(
                        @Parameter(description = "Branch name to check") @PathVariable String name) {

                log.debug("Checking if branch name exists: {}", name);

                boolean exists = branchService.existsByName(name);

                ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                                .success(true)
                                .message("Name availability checked")
                                .data(exists)
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/exists/code/{code}")
        @Operation(summary = "Check if branch code exists", description = "Validates if a branch code is already taken")
        public ResponseEntity<ApiResponse<Boolean>> checkCodeExists(
                        @Parameter(description = "Branch code to check") @PathVariable String code) {

                log.debug("Checking if branch code exists: {}", code);

                boolean exists = branchService.existsByCode(code);

                ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                                .success(true)
                                .message("Code availability checked")
                                .data(exists)
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}/can-delete")
        @Operation(summary = "Check if branch can be deleted", description = "Validates if a branch can be safely deleted")
        public ResponseEntity<ApiResponse<Boolean>> checkCanDelete(
                        @Parameter(description = "Branch unique identifier") @PathVariable String id) {

                log.debug("Checking if branch can be deleted: {}", id);

                boolean canDelete = branchService.canDelete(id);

                ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                                .success(true)
                                .message("Deletion possibility checked")
                                .data(canDelete)
                                .build();

                return ResponseEntity.ok(response);
        }

        // =============================================
        // STATISTICS ENDPOINTS
        // =============================================

        @GetMapping("/stats/count")
        @Operation(summary = "Get branch statistics", description = "Returns total and active branch counts")
        public ResponseEntity<ApiResponse<BranchStatsDTO>> getBranchStats() {

                log.debug("Fetching branch statistics");

                long totalCount = branchService.getTotalCount();
                long activeCount = branchService.getActiveCount();

                BranchStatsDTO stats = BranchStatsDTO.builder()
                                .totalBranches(totalCount)
                                .activeBranches(activeCount)
                                .inactiveBranches(totalCount - activeCount)
                                .build();

                ApiResponse<BranchStatsDTO> response = ApiResponse.<BranchStatsDTO>builder()
                                .success(true)
                                .message("Statistics retrieved successfully")
                                .data(stats)
                                .build();

                return ResponseEntity.ok(response);
        }

        // =============================================
        // NESTED DTO FOR STATISTICS
        // =============================================

        @lombok.Data
        @lombok.Builder
        @lombok.NoArgsConstructor
        @lombok.AllArgsConstructor
        public static class BranchStatsDTO {
                private long totalBranches;
                private long activeBranches;
                private long inactiveBranches;
        }
}
