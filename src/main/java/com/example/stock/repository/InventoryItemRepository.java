package com.example.stock.repository;

import com.example.stock.entity.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for InventoryItem entity operations.
 * Provides CRUD operations and custom query methods for InventoryItem entities.
 * 
 * @author Generated
 * @since 1.0
 */
@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, String>, JpaSpecificationExecutor<InventoryItem> {
    
    /**
     * Find inventory items by name containing the specified text (case-insensitive) with pagination.
     * 
     * @param name the search term for name field
     * @param pageable pagination parameters
     * @return Page of matching InventoryItem entities
     */
    @Query("SELECT i FROM InventoryItem i WHERE " +
           "LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<InventoryItem> findByNameContainingIgnoreCase(
        @Param("name") String name, 
        Pageable pageable);

    /**
     * Find inventory items by branch ID.
     * 
     * @param branchId the branch ID
     * @return List of inventory items in the specified branch
     */
    List<InventoryItem> findByBranchId(String branchId);

    /**
     * Find inventory items by branch ID with pagination.
     * 
     * @param branchId the branch ID
     * @param pageable pagination parameters
     * @return Page of inventory items in the specified branch
     */
    Page<InventoryItem> findByBranchId(String branchId, Pageable pageable);

    /**
     * Find inventory items by department ID.
     * 
     * @param departmentId the department ID
     * @return List of inventory items in the specified department
     */
    List<InventoryItem> findByDepartmentId(String departmentId);

    /**
     * Find inventory items by department ID with pagination.
     * 
     * @param departmentId the department ID
     * @param pageable pagination parameters
     * @return Page of inventory items in the specified department
     */
    Page<InventoryItem> findByDepartmentId(String departmentId, Pageable pageable);

    /**
     * Find inventory items by branch ID and department ID.
     * 
     * @param branchId the branch ID
     * @param departmentId the department ID
     * @return List of inventory items in the specified branch and department
     */
    List<InventoryItem> findByBranchIdAndDepartmentId(String branchId, String departmentId);

    /**
     * Find inventory items by branch ID and department ID with pagination.
     * 
     * @param branchId the branch ID
     * @param departmentId the department ID
     * @param pageable pagination parameters
     * @return Page of inventory items in the specified branch and department
     */
    Page<InventoryItem> findByBranchIdAndDepartmentId(String branchId, String departmentId, Pageable pageable);

    /**
     * Find inventory items by category ID.
     * 
     * @param categoryId the category ID
     * @return List of inventory items in the specified category
     */
    @Query("SELECT i FROM InventoryItem i WHERE i.category.id = :categoryId")
    List<InventoryItem> findByCategoryId(@Param("categoryId") String categoryId);

    /**
     * Find inventory items by unit ID.
     * 
     * @param unitId the unit ID
     * @return List of inventory items using the specified unit
     */
    @Query("SELECT i FROM InventoryItem i WHERE i.unit.id = :unitId")
    List<InventoryItem> findByUnitId(@Param("unitId") String unitId);

    /**
     * Check if branch has at least one inventory item.
     */
    boolean existsByBranchId(String branchId);

    /**
     * Check if department has at least one inventory item.
     */
    boolean existsByDepartmentId(String departmentId);

    /**
     * Check if category has at least one inventory item.
     */
    @Query("SELECT COUNT(i) > 0 FROM InventoryItem i WHERE i.category.id = :categoryId")
    boolean existsByCategoryId(@Param("categoryId") String categoryId);

    /**
     * Check if unit has at least one inventory item.
     */
    @Query("SELECT COUNT(i) > 0 FROM InventoryItem i WHERE i.unit.id = :unitId")
    boolean existsByUnitId(@Param("unitId") String unitId);
}