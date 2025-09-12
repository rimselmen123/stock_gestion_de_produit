package com.example.stock.dto.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO used to create a new Department.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentCreateDTO {

	@NotBlank(message = "Department name is required")
	@Size(max = 120, message = "Department name must not exceed 120 characters")
	private String name;

	@Size(max = 500, message = "Description must not exceed 500 characters")
	private String description;

	@NotNull(message = "Branch ID is required")
	@Size(max = 64, message = "Branch ID must not exceed 64 characters")
	private String branchId;
}
