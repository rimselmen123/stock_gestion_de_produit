package com.example.stock.repository;

import com.example.stock.entity.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
     * Find units by name or symbol containing the specified text (case-insensitive) with pagination.
     * 
     * @param name the search term for name field
     * @param symbol the search term for symbol field
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
}