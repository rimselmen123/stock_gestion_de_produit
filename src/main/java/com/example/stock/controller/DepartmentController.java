package com.example.stock.controller;

import com.example.stock.dto.common.ApiResponse;
import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.department.DepartmentCreateDTO;
import com.example.stock.dto.department.DepartmentResponseDTO;
import com.example.stock.dto.department.DepartmentSummaryDTO;
import com.example.stock.dto.department.DepartmentUpdateDTO;
import com.example.stock.service.DepartmentService;
import com.example.stock.service.DepartmentService.DepartmentFilterDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for Department management operations.
 * Provides CRUD endpoints for department entities with proper validation and error handling.
 * 
 * @author Generated
 * @since 1.0
 */
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * Get all departments with advanced filtering and pagination.
     * 
     * @param search Search term for name/description field
     * @param name Filter by name (contains)
     * @param branchId Filter by branch ID
     * @param createdFrom Filter created_at from (ISO-8601)
     * @param createdTo Filter created_at to (ISO-8601)
     * @param page Page number (1-based)
     * @param perPage Items per page
     * @param sortField Field to sort by
     * @param sortDirection Sort direction (asc/desc)
     * @return Paginated response with departments
     */
    @GetMapping
    public ResponseEntity<PaginatedResponse<DepartmentResponseDTO>> getAllDepartments(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String name,
            @RequestParam(name = "branch_id", required = false) String branchId,
            @RequestParam(name = "created_from", required = false) String createdFrom,
            @RequestParam(name = "created_to", required = false) String createdTo,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "5") int perPage,
            @RequestParam(name = "sort_field", defaultValue = "createdAt") String sortField,
            @RequestParam(name = "sort_direction", defaultValue = "desc") String sortDirection) {
        
        log.debug("Getting departments with filters - search: {}, name: {}, branchId: {}, createdFrom: {}, createdTo: {}, page: {}, perPage: {}, sortField: {}, sortDirection: {}", 
            search, name, branchId, createdFrom, createdTo, page, perPage, sortField, sortDirection);
        
        // Build filter DTO
        DepartmentFilterDTO filter = DepartmentFilterDTO.builder()
            .search(search)
            .name(name)
            .branchId(branchId)
            .createdFrom(createdFrom != null ? LocalDateTime.parse(createdFrom) : null)
            .createdTo(createdTo != null ? LocalDateTime.parse(createdTo) : null)
            .page(page - 1) // Convert to 0-based
            .size(perPage)
            .sortField(sortField)
            .sortDirection(sortDirection.toUpperCase())
            .build();
        
        Page<DepartmentResponseDTO> departmentPage = departmentService.search(filter);
        
        // Create pagination info
        com.example.stock.dto.common.PaginationInfo paginationInfo = 
            new com.example.stock.dto.common.PaginationInfo(
                departmentPage.isEmpty() ? 0 : departmentPage.getNumber() + 1, // current_page (0 if no data, 1-based otherwise)
                departmentPage.getSize(), // per_page
                departmentPage.getTotalElements(), // total
                departmentPage.getTotalPages() // last_page
            );
        
        PaginatedResponse<DepartmentResponseDTO> response = PaginatedResponse.<DepartmentResponseDTO>builder()
            .data(departmentPage.getContent())
            .pagination(paginationInfo)
            .message("Departments retrieved successfully")
            .success(true)
            .build();
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific department by ID.
     * 
     * @param id Department ID
     * @return Department data or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getDepartmentById(@PathVariable String id) {
        
        log.debug("Getting department with ID: {}", id);
        
        DepartmentResponseDTO department = departmentService.findById(id);
        ApiResponse<DepartmentResponseDTO> response = ApiResponse.success(
            department, "Department retrieved successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new department.
     * 
     * @param createDTO Department creation data
     * @return Created department data
     */
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> createDepartment(
            @Valid @RequestBody DepartmentCreateDTO createDTO) {
        
        log.info("Creating new department with name: {}", createDTO.getName());
        
        DepartmentResponseDTO createdDepartment = departmentService.create(createDTO);
        ApiResponse<DepartmentResponseDTO> response = ApiResponse.success(
            createdDepartment, "Department created successfully");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing department.
     * 
     * @param id Department ID to update
     * @param updateDTO Department update data
     * @return Updated department data
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> updateDepartment(
            @PathVariable String id,
            @Valid @RequestBody DepartmentUpdateDTO updateDTO) {
        
        log.info("Updating department with ID: {}", id);
        
        DepartmentResponseDTO updatedDepartment = departmentService.update(id, updateDTO);
        ApiResponse<DepartmentResponseDTO> response = ApiResponse.success(
            updatedDepartment, "Department updated successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a department by ID.
     * 
     * @param id Department ID to delete
     * @return Success message or error if department is in use
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable String id) {
        
        log.info("Deleting department with ID: {}", id);
        
        departmentService.delete(id);
        ApiResponse<Void> response = ApiResponse.success("Department deleted successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get departments by branch ID (for dropdowns).
     * 
     * @param branchId Branch ID
     * @return List of department summaries
     */
    @GetMapping("/by-branch/{branchId}")
    public ResponseEntity<ApiResponse<List<DepartmentSummaryDTO>>> getDepartmentsByBranch(
            @PathVariable String branchId) {
        
        log.debug("Getting departments for branch: {}", branchId);
        
        List<DepartmentSummaryDTO> departments = departmentService.listByBranch(branchId);
        ApiResponse<List<DepartmentSummaryDTO>> response = ApiResponse.success(
            departments, "Departments for branch retrieved successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Check if a department can be deleted.
     * 
     * @param id Department ID
     * @return Boolean indicating if deletion is possible
     */
    @GetMapping("/{id}/can-delete")
    public ResponseEntity<ApiResponse<Boolean>> canDeleteDepartment(@PathVariable String id) {
        
        log.debug("Checking if department {} can be deleted", id);
        
        boolean canDelete = departmentService.canDelete(id);
        ApiResponse<Boolean> response = ApiResponse.success(
            canDelete, "Delete check completed");
        
        return ResponseEntity.ok(response);
    }
}
