package com.example.stock.repository;

import com.example.stock.entity.Recipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipesRepository extends JpaRepository<Recipes, String> {
    
    Optional<Recipes> findBySellableItemId(Long sellableItemId);
    
    boolean existsBySellableItemId(Long sellableItemId);
}