package com.example.stock.dto.department;

import lombok.*;

/**
 * Lightweight Department representation for dropdowns and listings.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentSummaryDTO {
	private String id;
	private String name;
	private String branchId;
}
