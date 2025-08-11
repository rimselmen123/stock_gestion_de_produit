package com.example.stock.service;

import com.example.stock.entity.Brand;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Brand entity operations.
 * Defines business logic operations for Brand management.
 * 
 * @author Generated
 * @since 1.0
 */
public interface BrandService {
    
    /**
     * Create a new brand.
     * 
     * @param brand the brand entity to create
     * @return the created brand
     * @throws IllegalArgumentException if brand data is invalid
     */
    Brand createBrand(Brand brand);
    
    /**
     * Update an existing brand.
     * 
     * @param id the brand ID
     * @param brand the updated brand data
     * @return the updated brand
     * @throws RuntimeException if brand not found
     */
    Brand updateBrand(String id, Brand brand);
    
    /**
     * Find brand by ID.
     * 
     * @param id the brand ID
     * @return Optional containing the brand if found
     */
    Optional<Brand> findById(String id);
    
    /**
     * Find brand by name.
     * 
     * @param name the brand name
     * @return the brand if found, null otherwise
     */
    Brand findByName(String name);
    
    /**
     * Get all brands.
     * 
     * @return list of all brands
     */
    List<Brand> findAll();
    
    /**
     * Delete brand by ID.
     * 
     * @param id the brand ID to delete
     * @throws RuntimeException if brand not found or has dependencies
     */
    void deleteById(String id);
    
    /**
     * Check if brand exists by name.
     * 
     * @param name the brand name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
    
}