package com.example.stock.repository;

import com.example.stock.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
public interface UnitRepository extends JpaRepository<Unit, String> {
    
    /**
     * Find a unit by its name.
     * 
     * @param name the unit name to search for
     * @return Optional containing the Unit if found
     */
    Optional<Unit> findByName(String name);
    
    /**
     * Find a unit by its symbol.
     * 
     * @param symbol the unit symbol to search for
     * @return Optional containing the Unit if found
     */
    Optional<Unit> findBySymbol(String symbol);
    
    /**
     * Find units by name containing the specified text (case-insensitive).
     * 
     * @param name the partial name to search for
     * @return list of matching Unit entities
     */
    List<Unit> findByNameContainingIgnoreCase(String name);
    
    /**
     * Check if a unit exists by symbol.
     * 
     * @param symbol the unit symbol to check
     * @return true if unit exists, false otherwise
     */
    boolean existsBySymbol(String symbol);
    
    /**
     * Find all units ordered by name ascending.
     * 
     * @return list of Unit entities ordered by name
     */
    @Query("SELECT u FROM Unit u ORDER BY u.name ASC")
    List<Unit> findAllOrderByName();
}