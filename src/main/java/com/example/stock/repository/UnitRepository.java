package com.example.stock.repository;

import com.example.stock.entity.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Unit entity operations.
 * Provides CRUD operations and custom query methods for Unit entities.
 * 
 * @author Generated
 * @since 1.0
 */
@Repository
public interface UnitRepository extends JpaRepository<Unit, String>, JpaSpecificationExecutor<Unit> {

    /**
     * Find a unit by its symbol.
     * 
     * @param symbol the unit symbol to search for
     * @return Optional containing the Unit if found
     */
    Optional<Unit> findBySymbol(String symbol);

    /**
     * Check if a unit exists by symbol.
     * 
     * @param symbol the unit symbol to check
     * @return true if unit exists, false otherwise
     */
    boolean existsBySymbol(String symbol);

    /**
     * Find units by branch ID.
     * 
     * @param branchId the branch ID to search for
     * @return List of units in the specified branch
     */
    List<Unit> findByBranchId(String branchId);

    /**
     * Find units by department ID.
     * 
     * @param departmentId the department ID to search for
     * @return List of units in the specified department
     */
    List<Unit> findByDepartmentId(String departmentId);

    /**
     * Find units by branch ID and department ID.
     * 
     * @param branchId the branch ID to search for
     * @param departmentId the department ID to search for
     * @return List of units in the specified branch and department
     */
    List<Unit> findByBranchIdAndDepartmentId(String branchId, String departmentId);

    /**
     * Find units by branch ID with pagination.
     * 
     * @param branchId the branch ID to search for
     * @param pageable pagination parameters
     * @return Page of units in the specified branch
     */
    Page<Unit> findByBranchId(String branchId, Pageable pageable);

    /**
     * Find units by department ID with pagination.
     * 
     * @param departmentId the department ID to search for
     * @param pageable pagination parameters
     * @return Page of units in the specified department
     */
    Page<Unit> findByDepartmentId(String departmentId, Pageable pageable);

    /**
     * Find units by branch ID and department ID with pagination.
     * 
     * @param branchId the branch ID to search for
     * @param departmentId the department ID to search for
     * @param pageable pagination parameters
     * @return Page of units in the specified branch and department
     */
    Page<Unit> findByBranchIdAndDepartmentId(String branchId, String departmentId, Pageable pageable);

    /**
     * Find a unit by ID with branch and department relations loaded.
     * 
     * @param id the unit ID
     * @return Optional containing the Unit with relations loaded
     */
    @Query("SELECT u FROM Unit u LEFT JOIN FETCH u.branch LEFT JOIN FETCH u.department WHERE u.id = :id")
    Optional<Unit> findByIdWithRelations(@Param("id") String id);

    /**
     * Find units by name or symbol containing the specified text (case-insensitive)
     * with pagination.
     * 
     * @param name     the search term for name field
     * @param symbol   the search term for symbol field
     * @param pageable pagination parameters
     * @return Page of matching Unit entities
     */
    @Query("SELECT u FROM Unit u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(u.symbol) LIKE LOWER(CONCAT('%', :symbol, '%'))")
    Page<Unit> findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(
            @Param("name") String name,
            @Param("symbol") String symbol,
            Pageable pageable);

    /**
     * Check if a unit with the given symbol exists excluding a specific ID.
     * 
     * @param symbol the symbol to check
     * @param excludeId the ID to exclude from the search
     * @return true if unit exists, false otherwise
     */
    boolean existsBySymbolAndIdNot(String symbol, String excludeId);
}