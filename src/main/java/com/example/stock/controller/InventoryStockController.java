package com.example.stock.controller;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.inventorystock.InventoryStockResponseDTO;
import com.example.stock.dto.inventorystock.InventoryStockSummaryDTO;
import com.example.stock.service.InventoryStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@Tag(name = "Inventory Stock", description = "Endpoints for current stock and stock summaries")
@CrossOrigin(origins = "*")
public class InventoryStockController {

    private final InventoryStockService inventoryStockService;

    @GetMapping("/currentstock")
    @Operation(summary = "List current stock", description = "Filterable, paginated list of current stock")
    public ResponseEntity<PaginatedResponse<InventoryStockResponseDTO>> getCurrentStock(
	    @Parameter(description = "Global search term") @RequestParam(required = false) String search,
	    @Parameter(description = "Branch ID") @RequestParam(name = "branch_id", required = false) String branchId,
	    @Parameter(description = "Department ID") @RequestParam(name = "department_id", required = false) String departmentId,
	    @Parameter(description = "Inventory Item ID") @RequestParam(name = "inventory_item_id", required = false) String inventoryItemId,
	    @Parameter(description = "Min quantity") @RequestParam(name = "qty_min", required = false) BigDecimal qtyMin,
	    @Parameter(description = "Max quantity") @RequestParam(name = "qty_max", required = false) BigDecimal qtyMax,
	    @Parameter(description = "Page (1-based)") @RequestParam(defaultValue = "1") int page,
	    @Parameter(description = "Items per page") @RequestParam(name = "per_page", defaultValue = "20") int perPage,
	    @Parameter(description = "Sort field") @RequestParam(name = "sort_field", defaultValue = "createdAt") String sortField,
	    @Parameter(description = "Sort direction") @RequestParam(name = "sort_direction", defaultValue = "desc") String sortDirection
    ) {
	var filters = new InventoryStockService.Filters(
		search,
		branchId,
		departmentId,
		inventoryItemId,
		qtyMin,
		qtyMax
	);

	PaginatedResponse<InventoryStockResponseDTO> response = inventoryStockService.findAllWithFilters(
		filters,
		page,
		perPage,
		sortField,
		sortDirection
	);

	response.setMessage("Current stock retrieved successfully");
	response.setSuccess(true);

	return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    @Operation(summary = "List stock summaries", description = "Filterable, paginated list of stock summaries")
    public ResponseEntity<PaginatedResponse<InventoryStockSummaryDTO>> getStockSummary(
	    @Parameter(description = "Global search term") @RequestParam(required = false) String search,
	    @Parameter(description = "Branch ID") @RequestParam(name = "branch_id", required = false) String branchId,
	    @Parameter(description = "Department ID") @RequestParam(name = "department_id", required = false) String departmentId,
	    @Parameter(description = "Inventory Item ID") @RequestParam(name = "inventory_item_id", required = false) String inventoryItemId,
	    @Parameter(description = "Min quantity") @RequestParam(name = "qty_min", required = false) BigDecimal qtyMin,
	    @Parameter(description = "Max quantity") @RequestParam(name = "qty_max", required = false) BigDecimal qtyMax,
	    @Parameter(description = "Page (1-based)") @RequestParam(defaultValue = "1") int page,
	    @Parameter(description = "Items per page") @RequestParam(name = "per_page", defaultValue = "20") int perPage,
	    @Parameter(description = "Sort field") @RequestParam(name = "sort_field", defaultValue = "createdAt") String sortField,
	    @Parameter(description = "Sort direction") @RequestParam(name = "sort_direction", defaultValue = "desc") String sortDirection
    ) {
	var filters = new InventoryStockService.Filters(
		search,
		branchId,
		departmentId,
		inventoryItemId,
		qtyMin,
		qtyMax
	);

	PaginatedResponse<InventoryStockSummaryDTO> response = inventoryStockService.findAllSummariesWithFilters(
		filters,
		page,
		perPage,
		sortField,
		sortDirection
	);

	response.setMessage("Stock summary retrieved successfully");
	response.setSuccess(true);

	return ResponseEntity.ok(response);
    }
}
