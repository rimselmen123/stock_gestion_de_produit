package com.example.stock.service;

import com.example.stock.dto.department.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface defining business operations for Department management.
 */
public interface DepartmentService {

	DepartmentResponseDTO create(DepartmentCreateDTO dto);

	DepartmentResponseDTO update(String id, DepartmentUpdateDTO dto);

	void delete(String id);

	DepartmentResponseDTO findById(String id);

	/**
	 * Search with dynamic filters (Specifications) + pagination externally (PageRequest) in impl.
	 */
	Page<DepartmentResponseDTO> search(DepartmentFilterDTO filter);

	List<DepartmentSummaryDTO> listByBranch(String branchId);

	/**
	 * Returns true if entity can be safely deleted (no categories, no movements, no stocks).
	 */
	boolean canDelete(String id);

	/**
	 * Filter DTO for flexible search (can be reused at controller level).
	 */
	@lombok.Data
	@lombok.NoArgsConstructor
	@lombok.AllArgsConstructor
	@lombok.Builder
	class DepartmentFilterDTO {
		private String search;
		private String name;
		private String branchId;
		private LocalDateTime createdFrom;
		private LocalDateTime createdTo;
		@lombok.Builder.Default
		private int page = 0; // zero-based
		@lombok.Builder.Default
		private int size = 10;
		@lombok.Builder.Default
		private String sortField = "createdAt";
		@lombok.Builder.Default
		private String sortDirection = "DESC"; // ASC / DESC
	}
}
