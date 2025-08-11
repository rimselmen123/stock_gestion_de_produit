package com.example.stock.service.impl;

import com.example.stock.entity.Unit;
import com.example.stock.repository.UnitRepository;
import com.example.stock.service.UnitService;
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
 * Implementation of UnitService interface.
 * Handles all business logic for Unit entity operations.
 * 
 * @author Generated
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UnitServiceImpl implements UnitService {
    
    private final UnitRepository unitRepository;
    
    @Override
    public Unit createUnit(Unit unit) {
        log.info("Creating new unit with name: {} and symbol: {}", unit.getName(), unit.getSymbol());
        
        validateUnitData(unit);
        
        if (existsBySymbol(unit.getSymbol())) {
            throw new IllegalArgumentException("Unit with symbol '" + unit.getSymbol() + "' already exists");
        }
        
        unit.setId(UUID.randomUUID().toString());
        unit.setCreatedAt(LocalDateTime.now());
        unit.setUpdatedAt(LocalDateTime.now());
        
        Unit savedUnit = unitRepository.save(unit);
        log.info("Unit created successfully with ID: {}", savedUnit.getId());
        
        return savedUnit;
    }
    
    @Override
    public Unit updateUnit(String id, Unit unit) {
        log.info("Updating unit with ID: {}", id);
        
        Unit existingUnit = unitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Unit not found with ID: " + id));
        
        validateUnitData(unit);
        
        // Check if symbol is being changed and if new symbol already exists
        if (!existingUnit.getSymbol().equals(unit.getSymbol()) && existsBySymbol(unit.getSymbol())) {
            throw new IllegalArgumentException("Unit with symbol '" + unit.getSymbol() + "' already exists");
        }
        
        existingUnit.setName(unit.getName());
        existingUnit.setSymbol(unit.getSymbol());
        existingUnit.setUpdatedAt(LocalDateTime.now());
        
        Unit updatedUnit = unitRepository.save(existingUnit);
        log.info("Unit updated successfully with ID: {}", updatedUnit.getId());
        
        return updatedUnit;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Unit> findById(String id) {
        log.debug("Finding unit by ID: {}", id);
        return unitRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Unit> findByName(String name) {
        log.debug("Finding unit by name: {}", name);
        return unitRepository.findByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Unit> findBySymbol(String symbol) {
        log.debug("Finding unit by symbol: {}", symbol);
        return unitRepository.findBySymbol(symbol);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Unit> findAllOrderByName() {
        log.debug("Finding all units ordered by name");
        return unitRepository.findAllOrderByName();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Unit> searchByName(String name) {
        log.debug("Searching units by name: {}", name);
        return unitRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    public void deleteById(String id) {
        log.info("Deleting unit with ID: {}", id);
        
        Unit unit = unitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Unit not found with ID: " + id));
        
        // Check if unit has associated inventory items
        if (unit.getInventoryItems() != null && !unit.getInventoryItems().isEmpty()) {
            throw new RuntimeException("Cannot delete unit. It has associated inventory items.");
        }
        
        unitRepository.deleteById(id);
        log.info("Unit deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsBySymbol(String symbol) {
        return unitRepository.existsBySymbol(symbol);
    }
    
    private void validateUnitData(Unit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        
        if (!StringUtils.hasText(unit.getName())) {
            throw new IllegalArgumentException("Unit name cannot be empty");
        }
        
        if (!StringUtils.hasText(unit.getSymbol())) {
            throw new IllegalArgumentException("Unit symbol cannot be empty");
        }
        
        if (unit.getName().trim().length() < 2) {
            throw new IllegalArgumentException("Unit name must be at least 2 characters long");
        }
        
        if (unit.getSymbol().trim().length() < 1) {
            throw new IllegalArgumentException("Unit symbol must be at least 1 character long");
        }
    }
}
