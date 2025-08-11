package com.example.stock.service.impl;

import com.example.stock.entity.Brand;
import com.example.stock.repository.BrandRepository;
import com.example.stock.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of BrandService interface.
 * Handles all business logic for Brand entity operations.
 * 
 * @author Generated
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BrandServiceImpl implements BrandService {
    
    private final BrandRepository brandRepository;
    
    @Override
    public Brand createBrand(Brand brand) {
        log.info("Creating new brand with name: {}", brand.getName());
        
        validateBrandData(brand);
        
        if (existsByName(brand.getName())) {
            throw new IllegalArgumentException("Brand with name '" + brand.getName() + "' already exists");
        }
        
        brand.setId(UUID.randomUUID().toString());
        brand.setCreatedAt(LocalDateTime.now());
        brand.setUpdatedAt(LocalDateTime.now());
        
        Brand savedBrand = brandRepository.save(brand);
        log.info("Brand created successfully with ID: {}", savedBrand.getId());
        
        return savedBrand;
    }
    
    @Override
    public Brand updateBrand(String id, Brand brand) {
        log.info("Updating brand with ID: {}", id);
        
        Brand existingBrand = brandRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Brand not found with ID: " + id));
        
        validateBrandData(brand);
        
        // Check if name is being changed and if new name already exists
        if (!existingBrand.getName().equals(brand.getName()) && existsByName(brand.getName())) {
            throw new IllegalArgumentException("Brand with name '" + brand.getName() + "' already exists");
        }
        
        existingBrand.setName(brand.getName());
        existingBrand.setUpdatedAt(LocalDateTime.now());
        
        Brand updatedBrand = brandRepository.save(existingBrand);
        log.info("Brand updated successfully with ID: {}", updatedBrand.getId());
        
        return updatedBrand;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Brand> findById(String id) {
        log.debug("Finding brand by ID: {}", id);
        return brandRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Brand findByName(String name) {
        log.debug("Finding brand by name: {}", name);
        return brandRepository.findByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Brand> findAll() {
        log.debug("Finding all brands");
        return brandRepository.findAll();
    }
    
    @Override
    public void deleteById(String id) {
        log.info("Deleting brand with ID: {}", id);
        
        Brand brand = brandRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Brand not found with ID: " + id));
        
        // Check if brand has associated inventory items
        if (brand.getInventoryItems() != null && !brand.getInventoryItems().isEmpty()) {
            throw new RuntimeException("Cannot delete brand. It has associated inventory items.");
        }
        
        brandRepository.deleteById(id);
        log.info("Brand deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return brandRepository.existsByName(name);
    }
    
    private void validateBrandData(Brand brand) {
        if (brand == null) {
            throw new IllegalArgumentException("Brand cannot be null");
        }
        
        if (!StringUtils.hasText(brand.getName())) {
            throw new IllegalArgumentException("Brand name cannot be empty");
        }
        
        if (brand.getName().trim().length() < 2) {
            throw new IllegalArgumentException("Brand name must be at least 2 characters long");
        }
    }
}