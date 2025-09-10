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
     */
    Optional<Branch> findByName(String name);
    
    /**
     * Find branch by name (case-insensitive).
     */
    Optional<Branch> findByNameIgnoreCase(String name);
    
    /**
     * Find branch by code (utilise index unique sur code).
     */
    Optional<Branch> findByCode(String code);
    
    /**
     * Find branch by code (case-insensitive).
     */
    Optional<Branch> findByCodeIgnoreCase(String code);
    
    /**
     * Check if branch exists by name.
     */
    boolean existsByName(String name);
    
    /**
     * Check if branch exists by name (case-insensitive).
     */
    boolean existsByNameIgnoreCase(String name);
    
    /**
     * Check if branch exists by code.
     */
    boolean existsByCode(String code);
    
    /**
     * Find all active branches ordered by name (for dropdowns/select).
     */
    List<Branch> findByIsActiveTrueOrderByNameAsc();
    
    /**
     * Find all branches ordered by name (actives ET inactives).
     */
    List<Branch> findAllByOrderByNameAsc();

    // =============================================
    // RECHERCHE PAR NOM AVEC PAGINATION
    // =============================================
    
    /**
     * Find branches by name containing the specified text (case-insensitive) with pagination.
     */
    @Query("SELECT b FROM Branch b WHERE " +
           "LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Branch> findByNameContainingIgnoreCase(
        @Param("name") String name, 
        Pageable pageable);

    /**
     * Find ACTIVE branches by name containing the specified text.
     */
    @Query("SELECT b FROM Branch b WHERE " +
           "b.isActive = true AND " +
           "LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Branch> findActiveByNameContainingIgnoreCase(
        @Param("name") String name, 
        Pageable pageable);

    // =============================================
    // RECHERCHE PAR NOM SANS PAGINATION (pour autocomplete)
    // =============================================
    
    /**
     * Find branches by name containing the specified text (case-insensitive) - NO pagination.
     */
    @Query("SELECT b FROM Branch b WHERE " +
           "LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "ORDER BY b.name ASC")
    List<Branch> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find ACTIVE branches by name containing the specified text - NO pagination.
     */
    @Query("SELECT b FROM Branch b WHERE " +
           "b.isActive = true AND " +
           "LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "ORDER BY b.name ASC")
    List<Branch> findActiveByNameContainingIgnoreCase(@Param("name") String name);

    // =============================================
    // MÉTHODE UNIVERSELLE - GÈRE TOUT !
    // =============================================
    
    /**
     * Find branches with comprehensive filtering support.
     * Global search, specific filters, active status, date range, pagination.
     */
    @Query("SELECT b FROM Branch b WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "   LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "   LOWER(COALESCE(b.location, '')) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "   LOWER(COALESCE(b.code, '')) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:name IS NULL OR :name = '' OR " +
           "   LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:location IS NULL OR :location = '' OR " +
           "   LOWER(COALESCE(b.location, '')) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:code IS NULL OR :code = '' OR " +
           "   LOWER(COALESCE(b.code, '')) LIKE LOWER(CONCAT('%', :code, '%'))) AND " +
           "(:isActive IS NULL OR b.isActive = :isActive) AND " +
           "(:createdAfter IS NULL OR b.createdAt >= :createdAfter) AND " +
           "(:createdBefore IS NULL OR b.createdAt <= :createdBefore)")
    Page<Branch> findAllWithFilters(
        @Param("search") String search,
        @Param("name") String name,
        @Param("location") String location,
        @Param("code") String code,
        @Param("isActive") Boolean isActive,
        @Param("createdAfter") LocalDateTime createdAfter,
        @Param("createdBefore") LocalDateTime createdBefore,
        Pageable pageable
    );

    // =============================================
    // MÉTHODES DE VALIDATION BUSINESS
    // =============================================
    
    /**
     * Check if branch is used in inventory movements (for delete validation).
     */
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
           "FROM InventoryMovement m WHERE m.branchId = :branchId")
    boolean isUsedInMovements(@Param("branchId") String branchId);
    
    /**
     * Check if branch has departments (for delete validation).
     */
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
           "FROM Department d WHERE d.branchId = :branchId")
    boolean hasDepartments(@Param("branchId") String branchId);
    
    /**
     * Get total count of branches (for statistics).
     */
    @Query("SELECT COUNT(b) FROM Branch b")
    long countAllBranches();
    
    /**
     * Get count of active branches (for statistics).
     */
    @Query("SELECT COUNT(b) FROM Branch b WHERE b.isActive = true")
    long countActiveBranches();
}
