package com.example.stock.mapper;

import com.example.stock.dto.menuitemssnapshot.MenuItemResponseDTO;
import com.example.stock.dto.menuitemssnapshot.PosMenuItemDTO;
import com.example.stock.entity.MenuItemSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MenuItemMapper {

    @Mapping(source = "id", target = "posMenuItemId")
    @Mapping(source = "item_name", target = "name")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "branch_id", target = "branchId")
    @Mapping(target = "id", ignore = true) // Ignorer l'ID lors du mapping
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MenuItemSnapshot toEntity(PosMenuItemDTO dto);

    @Mapping(source = "posMenuItemId", target = "posMenuItemId")
    MenuItemResponseDTO toResponseDTO(MenuItemSnapshot entity);
}