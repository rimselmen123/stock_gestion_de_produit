package com.example.stock.mapper;

import com.example.stock.dto.branch.*;
import com.example.stock.entity.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper for Branch entity and DTOs.
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BranchMapper {

    /**
     * Convert Branch entity to BranchResponseDTO.
     */
    @Mapping(target = "departmentsCount", ignore = true)
    @Mapping(target = "movementsCount", ignore = true)
    BranchResponseDTO toResponseDTO(Branch branch);

    /**
     * Convert Branch entity to BranchSummaryDTO for dropdowns.
     */
    BranchSummaryDTO toSummaryDTO(Branch branch);
    
    /**
     * Convert BranchCreateDTO to Branch entity.
     * MapStruct automatically maps: name, location, code, isActive
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "movements", ignore = true)
    @Mapping(target = "departments", ignore = true)
    Branch toEntity(BranchCreateDTO createDTO);
    
    /**
     * Update existing Branch entity from BranchUpdateDTO.
     * MapStruct automatically updates: name, location, code, isActive (if not null)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "movements", ignore = true)
    @Mapping(target = "departments", ignore = true)
    void updateEntityFromDTO(BranchUpdateDTO updateDTO, @MappingTarget Branch branch);
}
