package com.example.stock.mapper;

import com.example.stock.dto.inventorystock.InventoryStockCreateDTO;
import com.example.stock.dto.inventorystock.InventoryStockResponseDTO;
import com.example.stock.dto.inventorystock.InventoryStockSummaryDTO;
import com.example.stock.dto.inventorystock.InventoryStockUpdateDTO;
import com.example.stock.entity.InventoryStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

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
    @Mapping(target = "inventoryItemName", source = "inventoryItem.name")
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
}
