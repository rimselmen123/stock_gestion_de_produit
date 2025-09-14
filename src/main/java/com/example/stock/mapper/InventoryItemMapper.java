package com.example.stock.mapper;

import com.example.stock.dto.inventoryitem.InventoryItemCreateDTO;
import com.example.stock.dto.inventoryitem.InventoryItemResponseDTO;
import com.example.stock.dto.inventoryitem.InventoryItemSummaryDTO;
import com.example.stock.dto.inventoryitem.InventoryItemUpdateDTO;
import com.example.stock.entity.InventoryItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper interface for InventoryItem entity and DTO conversions.
 * Handles mapping between InventoryItem entity and various DTOs with proper null handling.
 * 
 * @author Generated
 * @since 1.0
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {UnitMapper.class, CategoryMapper.class}
)
public interface InventoryItemMapper {

    /**
     * Maps InventoryItemCreateDTO to InventoryItem entity.
     * Used when creating a new inventory item from client request.
     * Note: Relationships (category, unit) need to be set separately.
     *
     * @param createDTO the inventory item creation data
     * @return InventoryItem entity ready for persistence
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "movements", ignore = true)
    InventoryItem toEntity(InventoryItemCreateDTO createDTO);

    /**
     * Maps InventoryItem entity to InventoryItemResponseDTO.
     * Used when returning inventory item data to client.
     * 
     * @param inventoryItem the inventory item entity
     * @return InventoryItemResponseDTO with complete inventory item information
     */
    InventoryItemResponseDTO toResponseDTO(InventoryItem inventoryItem);

    /**
     * Maps InventoryItem entity to InventoryItemSummaryDTO.
     * Used for lightweight inventory item listings.
     * 
     * @param inventoryItem the inventory item entity
     * @return InventoryItemSummaryDTO with essential inventory item information
     */
    InventoryItemSummaryDTO toSummaryDTO(InventoryItem inventoryItem);

    /**
     * Maps InventoryItemUpdateDTO to existing InventoryItem entity.
     * Updates only the provided fields, ignoring null values.
     * Note: Relationships need to be set separately in service layer.
     * 
     * @param updateDTO the inventory item update data
     * @param inventoryItem the existing inventory item entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "movements", ignore = true)
    void updateEntityFromDTO(InventoryItemUpdateDTO updateDTO, @MappingTarget InventoryItem inventoryItem);

    /**
     * Maps list of InventoryItem entities to list of InventoryItemResponseDTOs.
     * 
     * @param inventoryItems list of inventory item entities
     * @return list of InventoryItemResponseDTOs
     */
    List<InventoryItemResponseDTO> toResponseDTOList(List<InventoryItem> inventoryItems);

    /**
     * Maps list of InventoryItem entities to list of InventoryItemSummaryDTOs.
     * 
     * @param inventoryItems list of inventory item entities
     * @return list of InventoryItemSummaryDTOs
     */
    List<InventoryItemSummaryDTO> toSummaryDTOList(List<InventoryItem> inventoryItems);
}
