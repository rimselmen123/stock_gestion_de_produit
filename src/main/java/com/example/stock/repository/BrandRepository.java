package com.example.stock.repository;

import com.example.stock.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Brand entity operations.
 * Provides CRUD operations and custom query methods for Brand entities.
 * 
 * @author Generated
 * @since 1.0
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {
    
    /**
     * Find a brand by its name (case-sensitive).
     * 
     * @param name the brand name to search for
     * @return the Brand entity if found, null otherwise
     */
    Brand findByName(String name);
    
    /**
     * Find a brand by its name ignoring case.
     * 
     * @param name the brand name to search for (case-insensitive)
     * @return the Brand entity if found, null otherwise
     */
    Brand findByNameIgnoreCase(String name);
    
    /**
     * Check if a brand exists by name.
     * 
     * @param name the brand name to check
     * @return true if brand exists, false otherwise
     */
    boolean existsByName(String name);
}
