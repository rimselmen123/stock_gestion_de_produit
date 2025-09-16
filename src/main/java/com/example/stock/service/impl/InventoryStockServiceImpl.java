package com.example.stock.service.impl;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.common.PaginationInfo;
import com.example.stock.dto.inventorystock.InventoryStockResponseDTO;
import com.example.stock.dto.inventorystock.InventoryStockSummaryDTO;
import com.example.stock.entity.InventoryStock;
import com.example.stock.exception.ResourceNotFoundException;
import com.example.stock.mapper.InventoryStockMapper;
import com.example.stock.repository.InventoryStockRepository;
import com.example.stock.service.InventoryStockService;
import com.example.stock.specification.InventoryStockSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InventoryStockServiceImpl implements InventoryStockService {

    private final InventoryStockRepository inventoryStockRepository;
    private final InventoryStockMapper inventoryStockMapper;

    @Override
    public PaginatedResponse<InventoryStockResponseDTO> findAllWithFilters(
            Filters filters,
            int page,
            int size,
            String sortField,
            String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        page = Math.max(page, 1);
        size = Math.min(Math.max(size, 1), 100);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, mapSortField(sortField)));
        Specification<InventoryStock> spec = InventoryStockSpecifications.build(
                filters.branchId(),
                filters.departmentId(),
                filters.inventoryItemId(),
                filters.search(),
                filters.qtyMin(),
                filters.qtyMax());
        Page<InventoryStock> result = inventoryStockRepository.findAll(spec, pageable);
        List<InventoryStockResponseDTO> content = result.getContent().stream()
                .map(inventoryStockMapper::toResponseDTO)
                .toList();
        
        // Create pagination info with proper current_page calculation
        int currentPage = content.isEmpty() ? 0 : page;
        int totalPages = result.getTotalPages();
        if (totalPages == 0) totalPages = 1;
        
        PaginationInfo info = new PaginationInfo();
        info.setCurrentPage(currentPage);
        info.setPerPage(size);
        info.setTotal((int) result.getTotalElements());
        info.setLastPage(totalPages);
        
        return PaginatedResponse.of(content, info);
    }

    @Override
    public PaginatedResponse<InventoryStockSummaryDTO> findAllSummariesWithFilters(
            Filters filters,
            int page,
            int size,
            String sortField,
            String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        page = Math.max(page, 1);
        size = Math.min(Math.max(size, 1), 100);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, mapSortField(sortField)));
        Specification<InventoryStock> spec = InventoryStockSpecifications.build(
                filters.branchId(),
                filters.departmentId(),
                filters.inventoryItemId(),
                filters.search(),
                filters.qtyMin(),
                filters.qtyMax());
        Page<InventoryStock> result = inventoryStockRepository.findAll(spec, pageable);
        List<InventoryStockSummaryDTO> content = result.getContent().stream()
                .map(inventoryStockMapper::toSummaryDTO)
                .toList();
        
        // Create pagination info with proper current_page calculation
        int currentPage = content.isEmpty() ? 0 : page;
        int totalPages = result.getTotalPages();
        if (totalPages == 0) totalPages = 1;
        
        PaginationInfo info = new PaginationInfo();
        info.setCurrentPage(currentPage);
        info.setPerPage(size);
        info.setTotal((int) result.getTotalElements());
        info.setLastPage(totalPages);
        
        return PaginatedResponse.of(content, info);
    }

    @Override
    public InventoryStockResponseDTO findByIdOrThrow(String id) {
        InventoryStock stock = inventoryStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryStock", id));
        return inventoryStockMapper.toResponseDTO(stock);
    }

    @Override
    @Transactional
    public void delete(String id) {
        InventoryStock stock = inventoryStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryStock", id));
        inventoryStockRepository.delete(stock);
    }

    /**
     * Maps frontend sort field names to entity field names.
     * Handles the mismatch between frontend snake_case conventions and backend camelCase.
     * 
     * @param sortField the sort field from frontend API request
     * @return the corresponding entity field name
     */
    private String mapSortField(String sortField) {
        return switch (sortField) {
            case "created" -> "createdAt";
            case "updated" -> "updatedAt";
            case "current_quantity" -> "currentQuantity";
            case "average_unit_cost" -> "averageUnitCost";
            case "total_value" -> "totalValue";
            case "last_movement_date" -> "lastMovementDate";
            case "inventory_item_id" -> "inventoryItemId";
            case "branch_id" -> "branchId";
            case "department_id" -> "departmentId";
            default -> sortField; // Return as-is if no mapping needed
        };
    }
}