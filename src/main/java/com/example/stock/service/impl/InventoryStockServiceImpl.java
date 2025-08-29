package com.example.stock.service.impl;

// import com.example.stock.dto.common.PaginatedResponse;
// import com.example.stock.dto.common.PaginationInfo;
// import com.example.stock.dto.inventorystock.InventoryStockCreateDTO;
// import com.example.stock.dto.inventorystock.InventoryStockResponseDTO;
// import com.example.stock.dto.inventorystock.InventoryStockUpdateDTO;
// import com.example.stock.entity.InventoryStock;
// import com.example.stock.exception.ResourceNotFoundException;
// import com.example.stock.mapper.InventoryStockMapper;
// import com.example.stock.repository.InventoryStockRepository;
// import com.example.stock.service.InventoryStockService;
// import jakarta.persistence.criteria.Predicate;

// import java.math.BigDecimal;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;
// import java.util.stream.Collectors;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.data.jpa.domain.Specification;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.util.StringUtils;

// /**
//  * Implementation of InventoryStockService interface.
//  * Handles all business logic for Inventory Stock management.
//  */
// @Service
// @RequiredArgsConstructor
// @Slf4j
// @Transactional
// public class InventoryStockServiceImpl implements InventoryStockService {

//     private final InventoryStockRepository inventoryStockRepository;
//     private final InventoryStockMapper inventoryStockMapper;

//     @Override
//     @Transactional(readOnly = true)
//     public PaginatedResponse<InventoryStockResponseDTO> findAllWithFilters(
//             String search,
//             String branchId,
//             String inventoryItemId,
//             Double minQuantity,
//             Double maxQuantity,
//             Double minUnitPrice,
//             Double maxUnitPrice,
//             Boolean expiredOnly,
//             Boolean expiringSoon,
//             LocalDate createdFrom,
//             LocalDate createdTo,
//             LocalDate updatedFrom,
//             LocalDate updatedTo,
//             int page,
//             int size,
//             String sortField,
//             String sortDirection) {
        
//         log.debug("Finding inventory stocks with filters - search: {}, branchId: {}, itemId: {}", 
//                 search, branchId, inventoryItemId);
        
//         // Validate and adjust pagination parameters
//         page = Math.max(1, page);
//         size = Math.min(Math.max(1, size), 100);
        
//         // Create sort object
//         Sort.Direction direction = Sort.Direction.fromString(sortDirection);
//         Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, sortField));
        
//         // Build specification
//         Specification<InventoryStock> spec = (root, query, criteriaBuilder) -> {
//             List<Predicate> predicates = new ArrayList<>();
            
//             // Add search criteria
//             if (StringUtils.hasText(search)) {
//                 String searchPattern = "%" + search.toLowerCase() + "%";
//                 predicates.add(criteriaBuilder.or(
//                     criteriaBuilder.like(criteriaBuilder.lower(root.get("inventoryItem").get("name")), searchPattern),
//                     criteriaBuilder.like(criteriaBuilder.lower(root.get("inventoryItemId")), searchPattern),
//                     criteriaBuilder.like(criteriaBuilder.lower(root.get("branchId")), searchPattern)
//                 ));
//             }
            
//             // Add filters
//             if (StringUtils.hasText(branchId)) {
//                 predicates.add(criteriaBuilder.equal(root.get("branchId"), branchId));
//             }
            
//             if (StringUtils.hasText(inventoryItemId)) {
//                 predicates.add(criteriaBuilder.equal(root.get("inventoryItem").get("id"), inventoryItemId));
//             }
            
//             if (minQuantity != null) {
//                 predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), minQuantity));
//             }
            
//             if (maxQuantity != null) {
//                 predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("quantity"), maxQuantity));
//             }
            
//             if (expiredOnly != null && expiredOnly) {
//                 predicates.add(criteriaBuilder.lessThan(root.get("expirationDate"), LocalDate.now()));
//             }
            
//             if (expiringSoon != null && expiringSoon) {
//                 LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
//                 predicates.add(criteriaBuilder.between(
//                     root.get("expirationDate"), 
//                     LocalDate.now(), 
//                     thirtyDaysFromNow
//                 ));
//             }
            
//             if (createdFrom != null) {
//                 predicates.add(criteriaBuilder.greaterThanOrEqualTo(
//                     root.get("createdAt"), 
//                     createdFrom.atStartOfDay()
//                 ));
//             }
            
//             if (createdTo != null) {
//                 predicates.add(criteriaBuilder.lessThanOrEqualTo(
//                     root.get("createdAt"), 
//                     createdTo.plusDays(1).atStartOfDay()
//                 ));
//             }
            
//             if (updatedFrom != null) {
//                 predicates.add(criteriaBuilder.greaterThanOrEqualTo(
//                     root.get("updatedAt"), 
//                     updatedFrom.atStartOfDay()
//                 ));
//             }
            
//             if (updatedTo != null) {
//                 predicates.add(criteriaBuilder.lessThanOrEqualTo(
//                     root.get("updatedAt"), 
//                     updatedTo.plusDays(1).atStartOfDay()
//                 ));
//             }
            
//             return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//         };
        
//         // Execute query
//         Page<InventoryStock> pageResult = inventoryStockRepository.findAll(spec, pageable);
        
//         // Convert to DTOs
//         List<InventoryStockResponseDTO> content = pageResult.getContent().stream()
//                 .map(inventoryStockMapper::toResponseDTO)
//                 .toList();
        
//         // Build pagination info
//         PaginationInfo paginationInfo = new PaginationInfo(
//                 page,
//                 size,
//                 (int) pageResult.getTotalElements(),
//                 pageResult.getTotalPages()
//         );
        
//         return new PaginatedResponse<>(content, paginationInfo);
//     }
    
//     @Override
//     @Transactional(readOnly = true)
//     public PaginatedResponse<InventoryStockResponseDTO> findLowStockItems(String branchId, int page, int size) {
//         log.debug("Finding low stock items for branch: {}", branchId);
        
//         // Validate and adjust pagination parameters
//         page = Math.max(1, page);
//         size = Math.min(Math.max(1, size), 100);
        
//         // Create pageable with default sort
//         Pageable pageable = PageRequest.of(page - 1, size, Sort.by("quantity").ascending());
        
//         // Find items below threshold
//         Page<InventoryStock> pageResult = inventoryStockRepository.findByBranchIdAndQuantityLessThanEqual(
//                 branchId, 
//                 10.0, // Threshold value, adjust as needed
//                 pageable
//         );
        
//         // Convert to DTOs
//         List<InventoryStockResponseDTO> content = pageResult.getContent().stream()
//                 .map(this::convertToResponseDTO)
//                 .collect(Collectors.toList());
        
//         // Build pagination info
//         PaginationInfo paginationInfo = new PaginationInfo(
//                 pageResult.getNumber() + 1,
//                 pageResult.getSize(),
//                 (int) pageResult.getTotalElements(),
//                 pageResult.getTotalPages()
//         );
        
//         return new PaginatedResponse<>(content, paginationInfo);
//     }

//     @Override
//     @Transactional(readOnly = true)
//     public InventoryStockResponseDTO findById(String id) {
//         log.debug("Finding inventory stock by id: {}", id);
//         InventoryStock stock = inventoryStockRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Inventory stock", id));
//         return inventoryStockMapper.toResponseDTO(stock);
//     }
    
//     @Override
//     @Transactional(readOnly = true)
//     public InventoryStockResponseDTO findByIdOrThrow(String id) {
//         return findById(id);
//     }

//     @Override
//     @Transactional(readOnly = true)
//     public boolean isBelowThreshold(String itemId, String branchId) {
//         log.debug("Checking if inventory item {} is below threshold in branch {}", itemId, branchId);
        
//         return inventoryStockRepository
//                 .findByInventoryItemIdAndBranchId(itemId, branchId)
//                 .map(stock -> {
//                     Integer threshold = stock.getInventoryItem() != null ? 
//                             stock.getInventoryItem().getThresholdQuantity() : 0;
//                     return stock.getQuantity().compareTo(new BigDecimal(threshold != null ? threshold : 0)) < 0;
//                 })
//                 .orElse(true); // Si le stock n'existe pas, on considère qu'il est en dessous du seuil
//     }

//     @Override
//     @Transactional(readOnly = true)
//     public double getCurrentStockLevel(String itemId, String branchId) {
//         log.debug("Getting current stock level for item {} in branch {}", itemId, branchId);
        
//         Optional<InventoryStock> stock = inventoryStockRepository.findByInventoryItemIdAndBranchId(itemId, branchId);
//         return stock.map(s -> s.getQuantity().doubleValue()).orElse(0.0);
//     }

//     @Override
//     @Transactional
//     public InventoryStockResponseDTO create(InventoryStockCreateDTO createDTO) {
//         log.info("Creating new inventory stock for item: {}", createDTO.getInventoryItemId());

//         // Validate input
//         if (createDTO.getQuantity() != null && createDTO.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
//             throw new IllegalArgumentException("Quantity cannot be negative");
//         }
//         if (createDTO.getUnitPurchasePrice() != null && createDTO.getUnitPurchasePrice().compareTo(BigDecimal.ZERO) < 0) {
//             throw new IllegalArgumentException("Unit purchase price cannot be negative");
//         }

//         // Create entity
//         InventoryStock stock = InventoryStock.builder()
//                 .id(UUID.randomUUID().toString())
//                 .inventoryItemId(createDTO.getInventoryItemId())
//                 .branchId(createDTO.getBranchId())
//                 .quantity(createDTO.getQuantity())
//                 .unitPurchasePrice(createDTO.getUnitPurchasePrice())
//                 .expirationDate(createDTO.getExpirationDate())
//                 .createdAt(LocalDateTime.now())
//                 .updatedAt(LocalDateTime.now())
//                 .build();

//         InventoryStock savedStock = inventoryStockRepository.save(stock);
//         log.info("Inventory stock created successfully with ID: {}", savedStock.getId());

//         return convertToResponseDTO(savedStock);
//     }

//     @Override
//     @Transactional
//     public InventoryStockResponseDTO update(String id, InventoryStockUpdateDTO updateDTO) {
//         log.info("Updating inventory stock with ID: {}", id);

//         // Find existing stock
//         InventoryStock existingStock = inventoryStockRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Inventory stock", id));

//         // Update fields if provided in DTO
//         if (updateDTO.getQuantity() != null) {
//             if (updateDTO.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
//                 throw new IllegalArgumentException("Quantity cannot be negative");
//             }
//             existingStock.setQuantity(updateDTO.getQuantity());
//         }

//         if (updateDTO.getUnitPurchasePrice() != null) {
//             if (updateDTO.getUnitPurchasePrice().compareTo(BigDecimal.ZERO) < 0) {
//                 throw new IllegalArgumentException("Unit purchase price cannot be negative");
//             }
//             existingStock.setUnitPurchasePrice(updateDTO.getUnitPurchasePrice());
//         }

//         existingStock.setExpirationDate(updateDTO.getExpirationDate());
//         existingStock.setUpdatedAt(LocalDateTime.now());

//         InventoryStock updatedStock = inventoryStockRepository.save(existingStock);
//         log.info("Inventory stock updated successfully with ID: {}", updatedStock.getId());

//         return convertToResponseDTO(updatedStock);
//     }

//     @Override
//     @Transactional
//     public void delete(String id) {
//         log.info("Deleting inventory stock with ID: {}", id);

//         // Check if stock exists
//         InventoryStock stock = inventoryStockRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Inventory stock", id));

//         // Delete the stock
//         inventoryStockRepository.delete(stock);
//     }

//     private InventoryStockResponseDTO convertToResponseDTO(InventoryStock stock) {
//         // Calculate average purchase price for this inventory item
//         BigDecimal averagePrice = inventoryStockRepository.findAverageUnitPriceByInventoryItemId(stock.getInventoryItemId());

//         InventoryStockResponseDTO.InventoryItemEmbeddedDTO embeddedItem = null;
//         if (stock.getInventoryItem() != null) {
//             embeddedItem = InventoryStockResponseDTO.InventoryItemEmbeddedDTO.builder()
//                     .id(stock.getInventoryItem().getId())
//                     .name(stock.getInventoryItem().getName())
//                     .thresholdQuantity(stock.getInventoryItem().getThresholdQuantity())
//                     .build();
//         }

//         return InventoryStockResponseDTO.builder()
//                 .id(stock.getId())
//                 .inventoryItemId(stock.getInventoryItemId())
//                 .branchId(stock.getBranchId())
//                 .quantity(stock.getQuantity())
//                 .moy(averagePrice != null ? averagePrice : BigDecimal.ZERO)
//                 .expirationDate(stock.getExpirationDate())
//                 .createdAt(stock.getCreatedAt())
//                 .updatedAt(stock.getUpdatedAt())
//                 .inventoryItem(embeddedItem)
//                 .build();}}