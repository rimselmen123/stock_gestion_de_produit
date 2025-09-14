package com.example.stock.mapper;

import com.example.stock.dto.department.DepartmentCreateDTO;
import com.example.stock.dto.department.DepartmentResponseDTO;
import com.example.stock.dto.department.DepartmentSummaryDTO;
import com.example.stock.dto.department.DepartmentUpdateDTO;
import com.example.stock.entity.Branch;
import com.example.stock.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper interface for Department entity and DTO conversions.
 * Handles mapping between Department entity and various DTOs with proper null handling.
 *
 * @author Generated
 * @since 1.0
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DepartmentMapper {

    /**
     * Maps DepartmentCreateDTO to Department entity.
     * Used when creating a new department from client request.
     * 
     * @param createDTO the department creation data
     * @return Department entity ready for persistence
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "branch", ignore = true)
    Department toEntity(DepartmentCreateDTO createDTO);

    /**
     * Maps Department entity to DepartmentResponseDTO.
     * Used when returning department data to client.
     * 
     * @param department the department entity
     * @return DepartmentResponseDTO with complete department information
     */
    @Mapping(target = "branch", source = "branch", qualifiedByName = "toBranchInfo")
    DepartmentResponseDTO toResponseDTO(Department department);

    /**
     * Maps Department entity to DepartmentSummaryDTO.
     * Used for lightweight department listings.
     * 
     * @param department the department entity
     * @return DepartmentSummaryDTO with essential department information
     */
    DepartmentSummaryDTO toSummaryDTO(Department department);

    /**
     * Maps DepartmentUpdateDTO to existing Department entity.
     * Updates only the provided fields, ignoring null values.
     * 
     * @param updateDTO the department update data
     * @param department the existing department entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "branch", ignore = true)
    void updateEntityFromDTO(DepartmentUpdateDTO updateDTO, @MappingTarget Department department);

    /**
     * Maps list of Department entities to list of DepartmentResponseDTOs.
     * 
     * @param departments list of department entities
     * @return list of DepartmentResponseDTOs
     */
    List<DepartmentResponseDTO> toResponseDTOList(List<Department> departments);

    /**
     * Maps list of Department entities to list of DepartmentSummaryDTOs.
     * 
     * @param departments list of department entities
     * @return list of DepartmentSummaryDTOs
     */
    List<DepartmentSummaryDTO> toSummaryDTOList(List<Department> departments);

    /**
     * Maps Branch entity to BranchInfo embedded object.
     * 
     * @param branch the branch entity
     * @return BranchInfo for embedded response
     */
    @Named("toBranchInfo")
    default DepartmentResponseDTO.BranchInfo toBranchInfo(Branch branch) {
        if (branch == null) {
            return null;
        }
        return DepartmentResponseDTO.BranchInfo.builder()
                .id(branch.getId())
                .name(branch.getName())
                .build();
    }
}