package com.example.stock.mapper;

import com.example.stock.dto.brand.BrandCreateDTO;
import com.example.stock.dto.brand.BrandResponseDTO;
import com.example.stock.dto.brand.BrandSummaryDTO;
import com.example.stock.dto.brand.BrandUpdateDTO;
import com.example.stock.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper interface for Brand entity and DTO conversions.
 * Handles mapping between Brand entity and various DTOs with proper null handling.
 * 
 * @author Generated
 * @since 1.0
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BrandMapper {

    /**
     * Maps BrandCreateDTO to Brand entity.
     * Used when creating a new brand from client request.
     * 
     * @param createDTO the brand creation data
     * @return Brand entity ready for persistence
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "inventoryItems", ignore = true)
    Brand toEntity(BrandCreateDTO createDTO);

    /**
     * Maps Brand entity to BrandResponseDTO.
     * Used when returning brand data to client.
     *
     * @param brand the brand entity
     * @return BrandResponseDTO with complete brand information
     */
    BrandResponseDTO toResponseDTO(Brand brand);

    /**
     * Maps Brand entity to BrandSummaryDTO.
     * Used for lightweight brand listings.
     *
     * @param brand the brand entity
     * @return BrandSummaryDTO with essential brand information
     */
    BrandSummaryDTO toSummaryDTO(Brand brand);

    /**
     * Maps BrandUpdateDTO to existing Brand entity.
     * Updates only the provided fields, ignoring null values.
     * 
     * @param updateDTO the brand update data
     * @param brand the existing brand entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "inventoryItems", ignore = true)
    void updateEntityFromDTO(BrandUpdateDTO updateDTO, @MappingTarget Brand brand);

    /**
     * Maps list of Brand entities to list of BrandResponseDTOs.
     * 
     * @param brands list of brand entities
     * @return list of BrandResponseDTOs
     */
    List<BrandResponseDTO> toResponseDTOList(List<Brand> brands);

    /**
     * Maps list of Brand entities to list of BrandSummaryDTOs.
     *
     * @param brands list of brand entities
     * @return list of BrandSummaryDTOs
     */
    List<BrandSummaryDTO> toSummaryDTOList(List<Brand> brands);
}
