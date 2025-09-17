package com.example.stock.repository;

import com.example.stock.entity.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Tax entity
 */
@Repository
public interface TaxRepository extends JpaRepository<Tax, Long>, JpaSpecificationExecutor<Tax> {

    /**
     * Find all taxes by branch ID
     */
    List<Tax> findByBranchId(String branchId);

    /**
     * Find tax by name and branch ID
     */
    Optional<Tax> findByNameAndBranchId(String name, String branchId);

    /**
     * Find taxes by rate range
     */
    @Query("SELECT t FROM Tax t WHERE t.rate BETWEEN :minRate AND :maxRate")
    List<Tax> findByRateBetween(@Param("minRate") BigDecimal minRate, @Param("maxRate") BigDecimal maxRate);

    /**
     * Find taxes by branch ID and rate greater than
     */
    List<Tax> findByBranchIdAndRateGreaterThan(String branchId, BigDecimal rate);

    /**
     * Check if tax name exists for a branch (excluding specific tax ID)
     */
    @Query("SELECT COUNT(t) > 0 FROM Tax t WHERE t.name = :name AND t.branchId = :branchId AND t.id != :excludeId")
    boolean existsByNameAndBranchIdAndIdNot(@Param("name") String name, @Param("branchId") String branchId, @Param("excludeId") Long excludeId);

    /**
     * Check if tax name exists for a branch
     */
    boolean existsByNameAndBranchId(String name, String branchId);

    /**
     * Find all taxes ordered by name
     */
    @Query("SELECT t FROM Tax t ORDER BY t.name ASC")
    List<Tax> findAllOrderByName();

    /**
     * Find taxes by branch ID ordered by rate
     */
    @Query("SELECT t FROM Tax t WHERE t.branchId = :branchId ORDER BY t.rate ASC")
    List<Tax> findByBranchIdOrderByRate(@Param("branchId") String branchId);

    /**
     * Count taxes by branch ID
     */
    long countByBranchId(String branchId);
}