package com.example.stock.repository;

import com.example.stock.entity.InventoryItemCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for InventoryItemCategory entity operations.
 * Provides CRUD operations and custom query methods for InventoryItemCategory
 * entities.
 * 
 * @author Generated
 * @since 1.0
 */
@Repository
public interface InventoryItemCategoryRepository
        extends JpaRepository<InventoryItemCategory, String>, JpaSpecificationExecutor<InventoryItemCategory> {

    /**
     * Find categories by name containing the specified text (case-insensitive) with
     * pagination.
     * 
     * @param name     the search term for name field
     * @param pageable pagination parameters
     * @return Page of matching InventoryItemCategory entities
     */
    @Query("SELECT c FROM InventoryItemCategory c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<InventoryItemCategory> findByNameContainingIgnoreCase(
            @Param("name") String name,
            Pageable pageable);

    /**
     * Count how many InventoryItem entities reference the given category id.
     */
    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.category.id = :id")
    long countInventoryItemsByCategoryId(@Param("id") String id);

    /**
     * Find categories by branch ID.
     * 
     * @param branchId the branch ID
     * @return List of categories in the specified branch
     */
    List<InventoryItemCategory> findByBranchId(String branchId);

    /**
     * Find categories by department ID.
     * 
     * @param departmentId the department ID
     * @return List of categories in the specified department
     */
    List<InventoryItemCategory> findByDepartmentId(String departmentId);

    /**
     * Find categories by branch ID and department ID.
     * 
     * @param branchId     the branch ID
     * @param departmentId the department ID
     * @return List of categories in the specified branch and department
     */
    List<InventoryItemCategory> findByBranchIdAndDepartmentId(String branchId, String departmentId);

    /**
     * Check if branch has at least one category.
     */
    boolean existsByBranchId(String branchId);

    /**
     * Check if department has at least one category.
     */
    boolean existsByDepartmentId(String departmentId);

    /**
     * Check if branch and department combination has at least one category.
     */
    boolean existsByBranchIdAndDepartmentId(String branchId, String departmentId);
}