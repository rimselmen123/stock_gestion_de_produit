package com.example.stock.mapper;

import com.example.stock.dto.inventorystock.InventoryStockResponseDTO;
import com.example.stock.dto.inventorystock.InventoryStockSummaryDTO;
import com.example.stock.entity.InventoryStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper for InventoryStock -> DTOs (response + summary).
 * InventoryItem embedded info is not available from InventoryStock (no relation), left null.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InventoryStockMapper {

	@Mapping(target = "inventoryItem", ignore = true)
	InventoryStockResponseDTO toResponseDTO(InventoryStock stock);

	@Mapping(target = "inventoryItem", ignore = true)
	InventoryStockSummaryDTO toSummaryDTO(InventoryStock stock);

	List<InventoryStockResponseDTO> toResponseDTOList(List<InventoryStock> stocks);

	List<InventoryStockSummaryDTO> toSummaryDTOList(List<InventoryStock> stocks);
}
// end
