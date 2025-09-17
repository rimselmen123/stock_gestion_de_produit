package com.example.stock.mapper;

import com.example.stock.dto.tax.TaxRequestDTO;
import com.example.stock.dto.tax.TaxResponseDTO;
import com.example.stock.entity.Tax;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for Tax entity and DTOs
 */
@Mapper(componentModel = "spring")
public interface TaxMapper {

    /**
     * Convert TaxRequestDTO to Tax entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "inventoryItems", ignore = true)
    Tax toEntity(TaxRequestDTO requestDTO);

    /**
     * Convert Tax entity to TaxResponseDTO
     */
    @Mapping(target = "branchName", source = "branch.name")
    @Mapping(target = "displayName", expression = "java(tax.getName() + \" (\" + tax.getRate() + \"%)\")")
    @Mapping(target = "branch", source = "branch")
    TaxResponseDTO toResponseDTO(Tax tax);

    /**
     * Update existing Tax entity from TaxRequestDTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "inventoryItems", ignore = true)
    void updateEntityFromRequest(TaxRequestDTO requestDTO, @MappingTarget Tax tax);

    /**
     * Convert Branch entity to BranchInfo DTO
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    TaxResponseDTO.BranchInfo toBranchInfo(com.example.stock.entity.Branch branch);
}