package com.example.stock.mapper;

import com.example.stock.dto.category.CategoryCreateDTO;
import com.example.stock.dto.category.CategoryResponseDTO;
import com.example.stock.dto.category.CategorySummaryDTO;
import com.example.stock.dto.category.CategoryUpdateDTO;
import com.example.stock.entity.InventoryItemCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper interface for InventoryItemCategory entity and DTO conversions.
 * Handles mapping between InventoryItemCategory entity and various DTOs with proper null handling.
 * 
 * @author Generated
 * @since 1.0
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoryMapper {

    /**
     * Maps CategoryCreateDTO to InventoryItemCategory entity.
     * Used when creating a new category from client request.
     * 
     * @param createDTO the category creation data
     * @return InventoryItemCategory entity ready for persistence
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "inventoryItems", ignore = true)
    @Mapping(target = "department", ignore = true)
    InventoryItemCategory toEntity(CategoryCreateDTO createDTO);

    /**
     * Maps InventoryItemCategory entity to CategoryResponseDTO.
     * Used when returning category data to client.
     * 
     * @param category the category entity
     * @return CategoryResponseDTO with complete category information
     */
    CategoryResponseDTO toResponseDTO(InventoryItemCategory category);

    /**
     * Maps InventoryItemCategory entity to CategorySummaryDTO.
     * Used for lightweight category listings.
     * 
     * @param category the category entity
     * @return CategorySummaryDTO with essential category information
     */
    CategorySummaryDTO toSummaryDTO(InventoryItemCategory category);

    /**
     * Maps CategoryUpdateDTO to existing InventoryItemCategory entity.
     * Updates only the provided fields, ignoring null values.
    * Note: departmentId is not updated as it's immutable after creation.
     * 
     * @param updateDTO the category update data
     * @param category the existing category entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "inventoryItems", ignore = true)
    @Mapping(target = "department", ignore = true)
    void updateEntityFromDTO(CategoryUpdateDTO updateDTO, @MappingTarget InventoryItemCategory category);

    /**
     * Maps list of InventoryItemCategory entities to list of CategoryResponseDTOs.
     * 
     * @param categories list of category entities
     * @return list of CategoryResponseDTOs
     */
    List<CategoryResponseDTO> toResponseDTOList(List<InventoryItemCategory> categories);

    /**
     * Maps list of InventoryItemCategory entities to list of CategorySummaryDTOs.
     * 
     * @param categories list of category entities
     * @return list of CategorySummaryDTOs
     */
    List<CategorySummaryDTO> toSummaryDTOList(List<InventoryItemCategory> categories);
}
