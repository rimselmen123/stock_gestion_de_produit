package com.example.stock.mapper;

import com.example.stock.dto.inventorystock.*;
import com.example.stock.entity.InventoryStock;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper interface for InventoryStock entity and DTO conversions.
 * Handles mapping between InventoryStock entity and various DTOs with proper null handling.
 * 
 * @author Generated
 * @since 1.0
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface InventoryStockMapper {

    /**
     * Maps InventoryStockCreateDTO to InventoryStock entity.
     * Used when creating a new inventory stock from client request.
     * Note: InventoryItem relationship needs to be set separately in service layer.
     *
     * @param createDTO the inventory stock creation data
     * @return InventoryStock entity ready for persistence
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "inventoryItem", ignore = true)
    @Mapping(target = "movements", ignore = true)
    InventoryStock toEntity(InventoryStockCreateDTO createDTO);

    /**
     * Maps InventoryStock entity to InventoryStockResponseDTO.
     * Used when returning inventory stock data to client with embedded inventory item.
     * 
     * @param inventoryStock the inventory stock entity
     * @return InventoryStockResponseDTO with complete inventory stock information
     */
    // Previously included stock status in response DTO as per backend-only logic:
    // @Mapping(target = "stockStatus", expression = "java(calculateStockStatus(inventoryStock))")
    @Mapping(target = "inventoryItem.id", source = "inventoryItem.id")
    @Mapping(target = "inventoryItem.name", source = "inventoryItem.name")
    @Mapping(target = "inventoryItem.thresholdQuantity", source = "inventoryItem.thresholdQuantity")
    InventoryStockResponseDTO toResponseDTO(InventoryStock inventoryStock);

    /**
     * Maps InventoryStock entity to InventoryStockSummaryDTO.
     * Used for lightweight inventory stock listings.
     * 
     * @param inventoryStock the inventory stock entity
     * @return InventoryStockSummaryDTO with essential inventory stock information
     */
    // Previously included stock status in summary DTO:
    // @Mapping(target = "stockStatus", expression = "java(calculateStockStatus(inventoryStock))")
    @Mapping(target = "inventoryItem.id", source = "inventoryItem.id")
    @Mapping(target = "inventoryItem.name", source = "inventoryItem.name")
    @Mapping(target = "inventoryItem.thresholdQuantity", source = "inventoryItem.thresholdQuantity")
    InventoryStockSummaryDTO toSummaryDTO(InventoryStock inventoryStock);

    /**
     * Maps InventoryStockUpdateDTO to existing InventoryStock entity.
     * Updates only the provided fields, ignoring null values.
     * Note: InventoryItem relationship needs to be handled separately if changed.
     *
     * @param updateDTO the inventory stock update data
     * @param inventoryStock the existing inventory stock entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "inventoryItemId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "inventoryItem", ignore = true)
    @Mapping(target = "movements", ignore = true)
    void updateEntityFromDTO(InventoryStockUpdateDTO updateDTO, @MappingTarget InventoryStock inventoryStock);

    /**
     * Maps list of InventoryStock entities to list of InventoryStockResponseDTOs.
     * 
     * @param inventoryStocks list of inventory stock entities
     * @return list of InventoryStockResponseDTOs
     */
    List<InventoryStockResponseDTO> toResponseDTOList(List<InventoryStock> inventoryStocks);

    /**
     * Maps list of InventoryStock entities to list of InventoryStockSummaryDTOs.
     * 
     * @param inventoryStocks list of inventory stock entities
     * @return list of InventoryStockSummaryDTOs
     */
    List<InventoryStockSummaryDTO> toSummaryDTOList(List<InventoryStock> inventoryStocks);

    
    // ---------------------------- hethom lesmthodes mtaa lcalcule li chyarej3o mel mapper le service layer 
    // Stock status helpers (commented out to match frontend contract, kept for future use)
    // ----------------------------
    // /**
    //  * Calcule le statut du stock basé sur la quantité et le seuil
    //  */
    // default String calculateStockStatus(InventoryStock stock) {
    //     if (stock == null || stock.getInventoryItem() == null || stock.getInventoryItem().getThresholdQuantity() == null) {
    //         return com.example.stock.util.StockStatus.NORMAL.name();
    //     }
    //     Integer threshold = stock.getInventoryItem().getThresholdQuantity();
    //     int quantity = (stock.getQuantity() != null) ? stock.getQuantity().intValue() : 0;
    //     if (quantity <= 0) {
    //         return com.example.stock.util.StockStatus.RUPTURE.name();
    //     } else if (quantity <= threshold) {
    //         return com.example.stock.util.StockStatus.ALERTE_BASSE.name();
    //     }
    //     return com.example.stock.util.StockStatus.NORMAL.name();
    // }
    //
    // /**
    //  * Convertit une liste de stocks en rapport de statut de stock
    //  */
    // default com.example.stock.dto.inventorystock.StockStatusReportDTO toStockStatusReport(java.util.List<InventoryStock> stocks) {
    //     if (stocks == null) {
    //         return com.example.stock.dto.inventorystock.StockStatusReportDTO.builder()
    //                 .items(java.util.List.of())
    //                 .totalItems(0)
    //                 .lowStockCount(0)
    //                 .outOfStockCount(0)
    //                 .build();
    //     }
    //     java.util.List<com.example.stock.dto.inventorystock.StockStatusItemDTO> items = stocks.stream()
    //             .map(this::toStockStatusItem)
    //             .collect(java.util.stream.Collectors.toList());
    //     long lowStockCount = items.stream().filter(item -> "ALERTE_BASSE".equals(item.getStatus())).count();
    //     long outOfStockCount = items.stream().filter(item -> "RUPTURE".equals(item.getStatus())).count();
    //     return com.example.stock.dto.inventorystock.StockStatusReportDTO.builder()
    //             .items(items)
    //             .totalItems(items.size())
    //             .lowStockCount((int) lowStockCount)
    //             .outOfStockCount((int) outOfStockCount)
    //             .build();
    // }
    //
    // /**
    //  * Convertit un stock en élément de rapport de statut
    //  */
    // @Mapping(target = "itemId", source = "inventoryItem.id")
    // @Mapping(target = "itemName", source = "inventoryItem.name")
    // @Mapping(target = "currentQuantity", source = "quantity")
    // @Mapping(target = "thresholdQuantity", source = "inventoryItem.thresholdQuantity")
    // @Mapping(target = "status", expression = "java(calculateStockStatus(stock))")
    // com.example.stock.dto.inventorystock.StockStatusItemDTO toStockStatusItem(InventoryStock stock);
}
