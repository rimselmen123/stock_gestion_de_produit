package com.example.stock.mapper;

import com.example.stock.dto.sellableitem.SellableItemResponseDTO;
import com.example.stock.entity.SellableItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SellableItemMapper {

    /**
     * Convert SellableItem entity to SellableItemResponseDTO.
     * Maps all fields directly as they have the same names.
     * Note: Relations JPA (menuItemSnapshot, variation) are ignored.
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "menuItemSnapshotId", target = "menuItemSnapshotId")
    @Mapping(source = "menuItemVariationSnapshotId", target = "menuItemVariationSnapshotId")
    @Mapping(source = "name", target = "name")
    SellableItemResponseDTO toResponseDTO(SellableItem entity);
}