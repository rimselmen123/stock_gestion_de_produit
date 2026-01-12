package com.example.stock.service;

import com.example.stock.dto.recipe.RecipeCreateDTO;
import com.example.stock.dto.recipe.RecipeResponseDTO;
import com.example.stock.dto.recipe.RecipeUpdateDTO;

import java.util.List;

public interface RecipeService {
    
    List<RecipeResponseDTO> findAll();
    
    RecipeResponseDTO findById(String id);
    
    RecipeResponseDTO create(RecipeCreateDTO createDTO);
    
    RecipeResponseDTO update(String id, RecipeUpdateDTO updateDTO);
    
    void delete(String id);
}