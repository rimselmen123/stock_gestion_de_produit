package com.example.stock.service;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.inventorystock.InventoryStockResponseDTO;
import com.example.stock.dto.inventorystock.InventoryStockSummaryDTO;

import java.math.BigDecimal;

/**
 * Service interface for InventoryStock entity operations.
 */
public interface InventoryStockService {

    /** Lightweight filter holder to avoid long parameter lists. */
    record Filters(
	    String search,
	    String branchId,
	    String departmentId,
	    String inventoryItemId,
	    BigDecimal qtyMin,
	    BigDecimal qtyMax
    ) {}

	/**
	 * Find inventory stocks with filters and pagination (1-based page index).
	 */
    PaginatedResponse<InventoryStockResponseDTO> findAllWithFilters(
	    Filters filters,
	    int page,
	    int size,
	    String sortField,
	    String sortDirection
    );

	/**
	 * Find inventory stock summaries with filters and pagination (1-based page index).
	 */
    PaginatedResponse<InventoryStockSummaryDTO> findAllSummariesWithFilters(
	    Filters filters,
	    int page,
	    int size,
	    String sortField,
	    String sortDirection
    );

	/** Find by id or throw. */
	InventoryStockResponseDTO findByIdOrThrow(String id);

	/** Delete by id. */
	void delete(String id);
}