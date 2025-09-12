package com.example.stock.dto.department;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Detailed Department representation for API responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponseDTO {
	private String id;
	private String name;
	private String description;
	private String branchId;
	// Embedded branch info (nullable if not fetched)
	private BranchInfo branch;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	/**
	 * Lightweight embedded branch representation.
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class BranchInfo {
		private String id;
		private String name;
	}
}
