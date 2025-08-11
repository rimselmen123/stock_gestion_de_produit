package com.example.stock.service;

import com.example.stock.entity.Unit;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Unit entity operations.
 * Defines business logic operations for Unit management.
 * 
 * @author Generated
 * @since 1.0
 */
public interface UnitService {
    
    /**
     * Create a new unit.
     * 
     * @param unit the unit entity to create
     * @return the created unit
     * @throws IllegalArgumentException if unit data is invalid
     */
    Unit createUnit(Unit unit);
    
    /**
     * Update an existing unit.
     * 
     * @param id the unit ID
     * @param unit the updated unit data
     * @return the updated unit
     * @throws RuntimeException if unit not found
     */
    Unit updateUnit(String id, Unit unit);
    
    /**
     * Find unit by ID.
     * 
     * @param id the unit ID
     * @return Optional containing the unit if found
     */
    Optional<Unit> findById(String id);
    
    /**
     * Find unit by name.
     * 
     * @param name the unit name
     * @return Optional containing the unit if found
     */
    Optional<Unit> findByName(String name);
    
    /**
     * Find unit by symbol.
     * 
     * @param symbol the unit symbol
     * @return Optional containing the unit if found
     */
    Optional<Unit> findBySymbol(String symbol);
    
    /**
     * Get all units ordered by name.
     * 
     * @return list of all units ordered by name
     */
    List<Unit> findAllOrderByName();
    
    /**
     * Search units by name (partial match, case-insensitive).
     * 
     * @param name the partial name to search
     * @return list of matching units
     */
    List<Unit> searchByName(String name);
    
    /**
     * Delete unit by ID.
     * 
     * @param id the unit ID to delete
     * @throws RuntimeException if unit not found or has dependencies
     */
    void deleteById(String id);
    
    /**
     * Check if unit exists by symbol.
     * 
     * @param symbol the unit symbol
     * @return true if exists, false otherwise
     */
    boolean existsBySymbol(String symbol);
}
