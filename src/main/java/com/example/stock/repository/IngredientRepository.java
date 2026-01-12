package com.example.stock.repository;

import com.example.stock.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, String> {
    
    List<Ingredient> findByRecipeId(String recipeId);
    
    void deleteByRecipeId(String recipeId);
}