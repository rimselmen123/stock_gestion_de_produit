package com.example.stock.controller;

import com.example.stock.dto.common.ApiResponse;
import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.common.PaginationInfo;
import com.example.stock.dto.inventorymouvement.InventoryMovementCreateDTO;
import com.example.stock.dto.inventorymouvement.InventoryMovementResponseDTO;
import com.example.stock.dto.inventorymouvement.InventoryMovementUpdateDTO;
import com.example.stock.service.InventoryMovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory-movements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class InventoryMovementController {

	private final InventoryMovementService inventoryMovementService;

	@PostMapping
	public ResponseEntity<ApiResponse<InventoryMovementResponseDTO>> create(
			@Valid @RequestBody InventoryMovementCreateDTO dto) {
		log.info("API create inventory movement called");
		InventoryMovementResponseDTO created = inventoryMovementService.createMovement(dto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success(created, "Inventory movement created successfully"));
	}

    @GetMapping
    public ResponseEntity<PaginatedResponse<InventoryMovementResponseDTO>> list(
	    @RequestParam(defaultValue = "1") int page,
	    @RequestParam(name = "per_page", defaultValue = "10") int perPage,
	    @RequestParam(name = "sort_field", defaultValue = "createdAt") String sortField,
	    @RequestParam(name = "sort_direction", defaultValue = "desc") String sortDirection,
	    // Filters
	    @RequestParam(name = "branch_id", required = false) String branchId,
	    @RequestParam(name = "supplier_id", required = false) String supplierId,
	    @RequestParam(name = "transaction_type", required = false) String transactionType,
	    @RequestParam(name = "item_name", required = false) String itemName,
	    @RequestParam(name = "search", required = false) String globalSearch,
	    @RequestParam(name = "qty_min", required = false) java.math.BigDecimal qtyMin,
	    @RequestParam(name = "qty_max", required = false) java.math.BigDecimal qtyMax,
	    @RequestParam(name = "price_min", required = false) java.math.BigDecimal priceMin,
	    @RequestParam(name = "price_max", required = false) java.math.BigDecimal priceMax,
	    @RequestParam(name = "exp_after", required = false) java.time.LocalDate expAfter,
	    @RequestParam(name = "exp_before", required = false) java.time.LocalDate expBefore,
	    @RequestParam(name = "created_after", required = false) java.time.LocalDateTime createdAfter,
	    @RequestParam(name = "created_before", required = false) java.time.LocalDateTime createdBefore,
	    @RequestParam(name = "updated_after", required = false) java.time.LocalDateTime updatedAfter,
	    @RequestParam(name = "updated_before", required = false) java.time.LocalDateTime updatedBefore
    ) {

	if (page < 1) page = 1;
	if (perPage < 1) perPage = 10;

	Sort sort = Sort.by(sortField);
	sort = "asc".equalsIgnoreCase(sortDirection) ? sort.ascending() : sort.descending();
	Pageable pageable = PageRequest.of(page - 1, perPage, sort);

	var filter = new InventoryMovementService.MovementSearchFilter(
		branchId, supplierId, transactionType, itemName, globalSearch,
		qtyMin, qtyMax, priceMin, priceMax, expAfter, expBefore,
		createdAfter, createdBefore, updatedAfter, updatedBefore
	);

	Page<InventoryMovementResponseDTO> result = inventoryMovementService.searchMovements(filter, pageable);
	PaginationInfo paginationInfo = PaginationInfo.of(page, perPage, result.getTotalElements());
	PaginatedResponse<InventoryMovementResponseDTO> response = PaginatedResponse.of(result.getContent(), paginationInfo);
	return ResponseEntity.ok(response);
    }

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<InventoryMovementResponseDTO>> getOne(@PathVariable String id) {
		InventoryMovementResponseDTO movement = inventoryMovementService.getMovementById(id);
		return ResponseEntity.ok(ApiResponse.success(movement));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<InventoryMovementResponseDTO>> update(
			@PathVariable String id,
			@Valid @RequestBody InventoryMovementUpdateDTO dto) {
		InventoryMovementResponseDTO updated = inventoryMovementService.updateMovement(id, dto);
		return ResponseEntity.ok(ApiResponse.success(updated, "Inventory movement updated successfully"));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
		inventoryMovementService.delete(id);
		return ResponseEntity.ok(ApiResponse.success(null, "Inventory movement deleted successfully"));
	}
}
