package com.example.stock.repository;

import com.example.stock.entity.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Branch entity operations.
 * Provides CRUD operations and custom query methods for Branch entities.
 * 
 * @author Generated
 * @since 1.0
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, String>, JpaSpecificationExecutor<Branch> {

    // =============================================
    // MÉTHODES BASIQUES ET ESSENTIELLES
    // =============================================
    
    /**
     * Find branch by exact name (utilise index unique sur name).
     * 
     * @param name the branch name
     * @return Optional containing the branch if found
     */
    Optional<Branch> findByName(String name);
    
    /**
     * Find branch by name (case-insensitive).
     * 
     * @param name the branch name
     * @return Optional containing the branch if found
     */
    Optional<Branch> findByNameIgnoreCase(String name);
    
    /**
     * Check if branch exists by name.
     * 
     * @param name the branch name
     * @return true if branch exists
     */
    boolean existsByName(String name);
    
    /**
     * Find all branches ordered by name (for dropdowns/select).
     * 
     * @return List of branches ordered by name ascending
     */
    List<Branch> findAllByOrderByNameAsc();

    // =============================================
    // RECHERCHE PAR NOM AVEC PAGINATION
    // =============================================
    
    /**
     * Find branches by name containing the specified text (case-insensitive) with pagination.
     * 
     * @param name the search term for name field
     * @param pageable pagination parameters
     * @return Page of matching Branch entities
     */
    @Query("SELECT b FROM Branch b WHERE " +
           "LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Branch> findByNameContainingIgnoreCase(
        @Param("name") String name, 
        Pageable pageable);

    // =============================================
    // MÉTHODE UNIVERSELLE - GÈRE TOUT !
    // =============================================
    
    /**
     * Find branches with comprehensive filtering support.
     * 
     * Supports:
     * - Global search across name and location fields
     * - Specific filters for name and location
     * - Date range filtering (created date)
     * - Pagination and sorting on any field
     * - Performance optimized with proper NULL handling
     * 
     * @param search global search term (searches in name OR location)
     * @param name specific name filter
     * @param location specific location filter  
     * @param createdAfter filter branches created after this date
     * @param createdBefore filter branches created before this date
     * @param pageable pagination and sorting parameters
     * @return Page of filtered Branch entities
     */
    @Query("SELECT b FROM Branch b WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "   LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "   LOWER(COALESCE(b.location, '')) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:name IS NULL OR :name = '' OR " +
           "   LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:location IS NULL OR :location = '' OR " +
           "   LOWER(COALESCE(b.location, '')) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:createdAfter IS NULL OR b.createdAt >= :createdAfter) AND " +
           "(:createdBefore IS NULL OR b.createdAt <= :createdBefore)")
    Page<Branch> findAllWithFilters(
        @Param("search") String search,
        @Param("name") String name,
        @Param("location") String location,
        @Param("createdAfter") LocalDateTime createdAfter,
        @Param("createdBefore") LocalDateTime createdBefore,
        Pageable pageable
    );

    // =============================================
    // MÉTHODES DE VALIDATION BUSINESS
    // =============================================
    
    /**
     * Check if branch is used in inventory movements (for delete validation).
     * 
     * @param branchId the branch ID to check
     * @return true if branch is referenced in movements
     */
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
           "FROM InventoryMovement m WHERE m.branchId = :branchId")
    boolean isUsedInMovements(@Param("branchId") String branchId);
    
    /**
     * Check if branch has departments (for delete validation).
     * 
     * @param branchId the branch ID to check  
     * @return true if branch has departments
     */
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
           "FROM Department d WHERE d.branchId = :branchId")
    boolean hasDepartments(@Param("branchId") String branchId);
    
    /**
     * Get total count of branches (for statistics).
     * 
     * @return total number of branches
     */
    @Query("SELECT COUNT(b) FROM Branch b")
    long countAllBranches();
}