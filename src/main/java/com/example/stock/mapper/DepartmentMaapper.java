
package com.example.stock.mapper;

import com.example.stock.dto.department.DepartmentCreateDTO;
import com.example.stock.dto.department.DepartmentResponseDTO;
import com.example.stock.dto.department.DepartmentSummaryDTO;
import com.example.stock.dto.department.DepartmentUpdateDTO;
import com.example.stock.entity.Department;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Department entity and DTOs.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DepartmentMaapper {

	// Create mapping
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "branch", ignore = true)
	@Mapping(target = "categories", ignore = true)
	Department toEntity(DepartmentCreateDTO dto);

	// Update mapping (branchId immutable)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "branchId", ignore = true)
	@Mapping(target = "branch", ignore = true)
	@Mapping(target = "categories", ignore = true)
	void updateEntityFromDTO(DepartmentUpdateDTO dto, @MappingTarget Department entity);

	// Entity -> Response
	@Mapping(target = "branch", expression = "java( entity.getBranch() == null ? null : new DepartmentResponseDTO.BranchInfo(entity.getBranch().getId(), entity.getBranch().getName()) )")
	DepartmentResponseDTO toResponseDTO(Department entity);

	// Entity -> Summary
	DepartmentSummaryDTO toSummaryDTO(Department entity);

	List<DepartmentResponseDTO> toResponseDTOList(List<Department> entities);
	List<DepartmentSummaryDTO> toSummaryDTOList(List<Department> entities);
}

