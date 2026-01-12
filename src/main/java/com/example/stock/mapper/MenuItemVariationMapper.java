package com.example.stock.mapper;

import com.example.stock.dto.variation.MenuItemVariationResponseDTO;
import com.example.stock.dto.variation.VariationDTO;
import com.example.stock.entity.MenuItemVariationSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MenuItemVariationMapper {

    /**
     * Convert VariationDTO (from POS) to MenuItemVariationSnapshot entity.
     * Maps:
     * - id → id (POS variation ID)
     * - variation → name
     * - menu_item_id → menuItemSnapshotId (will be set manually in service)
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "variation", target = "name")
    @Mapping(source = "menu_item_id", target = "menuItemSnapshotId")
    @Mapping(target = "menuItemSnapshot", ignore = true) // Relation JPA, ignore
    MenuItemVariationSnapshot toEntity(VariationDTO dto);

    /**
     * Convert MenuItemVariationSnapshot entity to MenuItemVariationResponseDTO.
     * Maps all fields directly as they have the same names.
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "menuItemSnapshotId", target = "menuItemSnapshotId")
    @Mapping(source = "name", target = "name")
    MenuItemVariationResponseDTO toResponseDTO(MenuItemVariationSnapshot entity);
}