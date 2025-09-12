package com.example.stock.dto.department;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO used to update an existing Department.
 * All fields optional; only provided fields will be updated.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentUpdateDTO {

	@Size(max = 120, message = "Department name must not exceed 120 characters")
	private String name;

	@Size(max = 500, message = "Description must not exceed 500 characters")
	private String description;
}
