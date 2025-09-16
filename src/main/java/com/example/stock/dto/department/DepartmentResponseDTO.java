package com.example.stock.dto.department;

import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Detailed Department representation for API responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponseDTO {
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	@JsonProperty("branch_id")
	private String branchId;
	// Embedded branch info (nullable if not fetched)
	private BranchInfo branch;
	@JsonProperty("created_at")
	private LocalDateTime createdAt;
	@JsonProperty("updated_at")
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
		@JsonProperty("name")
		private String name;
	}
}
