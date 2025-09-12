package com.example.stock.repository;

import com.example.stock.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

/**
 * Repository for Department with advanced filtering and integrity helpers.
 */
public interface DepartmentRepository extends JpaRepository<Department, String>, JpaSpecificationExecutor<Department> {

    // ----------- Existence / Uniqueness -----------
    boolean existsByBranchId(String branchId);
    boolean existsByBranchIdAndNameIgnoreCase(String branchId, String name);

    // ----------- Simple finders / pagination -----------
    Page<Department> findByBranchId(String branchId, Pageable pageable);
    long countByBranchId(String branchId);

    // ----------- Advanced filtering (fallback if Specifications not used) -----------
    @Query("SELECT d FROM Department d WHERE " +
	    "(:search IS NULL OR :search = '' OR " +
	    "  LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
	    "  LOWER(COALESCE(d.description, '')) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
	    "(:name IS NULL OR :name = '' OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
	    "(:branchId IS NULL OR :branchId = '' OR d.branchId = :branchId) AND " +
	    "(:createdAfter IS NULL OR d.createdAt >= :createdAfter) AND " +
	    "(:createdBefore IS NULL OR d.createdAt <= :createdBefore)")
    Page<Department> findAllWithFilters(@Param("search") String search,
					     @Param("name") String name,
					     @Param("branchId") String branchId,
					     @Param("createdAfter") LocalDateTime createdAfter,
					     @Param("createdBefore") LocalDateTime createdBefore,
					     Pageable pageable);

    // ----------- Integrity checks (business delete constraints) -----------
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
	    "FROM InventoryItemCategory c WHERE c.departmentId = :departmentId")
    boolean hasCategories(@Param("departmentId") String departmentId);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
	    "FROM InventoryMovement m WHERE m.departmentId = :departmentId OR m.destinationDepartmentId = :departmentId")
    boolean isUsedInMovements(@Param("departmentId") String departmentId);
}
