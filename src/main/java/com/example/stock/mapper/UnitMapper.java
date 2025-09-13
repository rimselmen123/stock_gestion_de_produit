package com.example.stock.mapper;

import com.example.stock.dto.unit.UnitCreateDTO;
import com.example.stock.dto.unit.UnitResponseDTO;
import com.example.stock.dto.unit.UnitSummaryDTO;
import com.example.stock.dto.unit.UnitUpdateDTO;
import com.example.stock.entity.Branch;
import com.example.stock.entity.Department;
import com.example.stock.entity.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper interface for Unit entity and DTO conversions.
 * Handles mapping between Unit entity and various DTOs with proper null handling.
 * 
 * @author Generated
 * @since 1.0
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UnitMapper {

    /**
     * Maps UnitCreateDTO to Unit entity.
     * Used when creating a new unit from client request.
     * 
     * @param createDTO the unit creation data
     * @return Unit entity ready for persistence
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "inventoryItems", ignore = true)
    Unit toEntity(UnitCreateDTO createDTO);

    /**
     * Maps Unit entity to UnitResponseDTO.
     * Used when returning unit data to client.
     * 
     * @param unit the unit entity
     * @return UnitResponseDTO with complete unit information
     */
    @Mapping(target = "branch", source = "branch", qualifiedByName = "toBranchInfo")
    @Mapping(target = "department", source = "department", qualifiedByName = "toDepartmentInfo")
    UnitResponseDTO toResponseDTO(Unit unit);

    /**
     * Maps Unit entity to UnitSummaryDTO.
     * Used for lightweight unit listings.
     * 
     * @param unit the unit entity
     * @return UnitSummaryDTO with essential unit information
     */
    UnitSummaryDTO toSummaryDTO(Unit unit);

    /**
     * Maps UnitUpdateDTO to existing Unit entity.
     * Updates only the provided fields, ignoring null values.
     * 
     * @param updateDTO the unit update data
     * @param unit the existing unit entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "inventoryItems", ignore = true)
    void updateEntityFromDTO(UnitUpdateDTO updateDTO, @MappingTarget Unit unit);

    /**
     * Maps list of Unit entities to list of UnitResponseDTOs.
     * 
     * @param units list of unit entities
     * @return list of UnitResponseDTOs
     */
    List<UnitResponseDTO> toResponseDTOList(List<Unit> units);

    /**
     * Maps list of Unit entities to list of UnitSummaryDTOs.
     * 
     * @param units list of unit entities
     * @return list of UnitSummaryDTOs
     */
    List<UnitSummaryDTO> toSummaryDTOList(List<Unit> units);

    /**
     * Maps Branch entity to BranchInfo embedded object.
     * 
     * @param branch the branch entity
     * @return BranchInfo for embedded response
     */
    @Named("toBranchInfo")
    default UnitResponseDTO.BranchInfo toBranchInfo(Branch branch) {
        if (branch == null) {
            return null;
        }
        return UnitResponseDTO.BranchInfo.builder()
                .id(branch.getId())
                .name(branch.getName())
                .description(branch.getLocation()) // Map location to description as per simplified response
                .build();
    }

    /**
     * Maps Department entity to DepartmentInfo embedded object.
     * 
     * @param department the department entity
     * @return DepartmentInfo for embedded response
     */
    @Named("toDepartmentInfo")
    default UnitResponseDTO.DepartmentInfo toDepartmentInfo(Department department) {
        if (department == null) {
            return null;
        }
        return UnitResponseDTO.DepartmentInfo.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .build();
    }
}
