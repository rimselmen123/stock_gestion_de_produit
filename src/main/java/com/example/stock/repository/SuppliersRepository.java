package com.example.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.stock.entity.Suppliers;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Suppliers entity operations.
 * Provides CRUD operations and custom query methods for Suppliers entities.
 * 
 * @author Generated
 * @since 1.0
 */
@Repository
public interface SuppliersRepository extends JpaRepository<Suppliers, String>, JpaSpecificationExecutor<Suppliers> {

    /**
     * Find a supplier by email.
     */
    Optional<Suppliers> findByEmail(String email);

    /**
     * Check if a supplier exists by email.
     */
    boolean existsByEmail(String email);

    /**
     * Check if a supplier exists by name.
     */
    boolean existsByName(String name);

    /**
     * Find suppliers by name or email containing the specified text (case-insensitive) with pagination.
     */
    @Query("SELECT s FROM Suppliers s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    Page<Suppliers> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        @Param("name") String name,
        @Param("email") String email,
        Pageable pageable);

    /**
     * Find suppliers by branch ID.
     * 
     * @param branchId the branch ID
     * @return List of suppliers in the specified branch
     */
    List<Suppliers> findByBranchId(String branchId);

    /**
     * Find suppliers by branch ID with pagination.
     * 
     * @param branchId the branch ID
     * @param pageable pagination parameters
     * @return Page of suppliers in the specified branch
     */
    Page<Suppliers> findByBranchId(String branchId, Pageable pageable);

    /**
     * Find suppliers by branch ID and name containing search term.
     * 
     * @param branchId the branch ID
     * @param name the search term for name field
     * @param pageable pagination parameters
     * @return Page of matching suppliers
     */
    @Query("SELECT s FROM Suppliers s WHERE s.branchId = :branchId AND " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Suppliers> findByBranchIdAndNameContainingIgnoreCase(
        @Param("branchId") String branchId,
        @Param("name") String name,
        Pageable pageable);

    /**
     * Check if branch has at least one supplier.
     * 
     * @param branchId the branch ID
     * @return true if branch has suppliers, false otherwise
     */
    boolean existsByBranchId(String branchId);

    /**
     * Count suppliers by branch ID.
     * 
     * @param branchId the branch ID
     * @return number of suppliers in the branch
     */
    long countByBranchId(String branchId);

    /**
     * Check if email is already taken by another supplier.
     * 
     * @param email the email to check
     * @param excludeId the supplier ID to exclude from the check
     * @return true if email exists for another supplier
     */
    @Query("SELECT COUNT(s) > 0 FROM Suppliers s WHERE LOWER(s.email) = LOWER(:email) AND s.id != :excludeId")
    boolean existsByEmailIgnoreCaseAndIdNot(@Param("email") String email, @Param("excludeId") String excludeId);

    /**
     * Check if email is already taken.
     * 
     * @param email the email to check
     * @return true if email exists
     */
    @Query("SELECT COUNT(s) > 0 FROM Suppliers s WHERE LOWER(s.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);
}
