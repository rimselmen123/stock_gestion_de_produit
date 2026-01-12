package com.example.stock.service.impl;

import com.example.stock.dto.recipe.RecipeCreateDTO;
import com.example.stock.dto.recipe.RecipeResponseDTO;
import com.example.stock.dto.recipe.RecipeUpdateDTO;
import com.example.stock.entity.Ingredient;
import com.example.stock.entity.InventoryItem;
import com.example.stock.entity.Recipes;
import com.example.stock.entity.SellableItem;
import com.example.stock.exception.ForeignKeyConstraintException;
import com.example.stock.exception.ResourceNotFoundException;
import com.example.stock.mapper.RecipeMapper;
import com.example.stock.repository.IngredientRepository;
import com.example.stock.repository.InventoryItemRepository;
import com.example.stock.repository.RecipesRepository;
import com.example.stock.repository.SellableItemRepository;
import com.example.stock.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private final RecipesRepository recipesRepository;
    private final IngredientRepository ingredientRepository;
    private final SellableItemRepository sellableItemRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final RecipeMapper recipeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RecipeResponseDTO> findAll() {
        log.debug("Finding all recipes");
        
        List<Recipes> recipes = recipesRepository.findAll();
        return recipes.stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeResponseDTO findById(String id) {
        log.debug("Finding recipe by ID: {}", id);
        
        Recipes recipe = recipesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Recipe", id));
        
        return convertToResponseDTO(recipe);
    }

    @Override
    public RecipeResponseDTO create(RecipeCreateDTO createDTO) {
        log.info("Creating new recipe for sellable_item_id: {}", createDTO.getSellableItemId());
        
        // Validate sellable item exists
        Long sellableItemId = Long.parseLong(createDTO.getSellableItemId());
        SellableItem sellableItem = sellableItemRepository.findById(sellableItemId)
            .orElseThrow(() -> new ForeignKeyConstraintException("sellable_item_id", createDTO.getSellableItemId()));
        
        // Check if recipe already exists for this sellable item
        if (recipesRepository.existsBySellableItemId(sellableItemId)) {
            throw new com.example.stock.exception.DuplicateResourceException(
                "Recipe already exists for sellable_item_id: " + sellableItemId);
        }
        
        // Validate all inventory items exist
        validateInventoryItems(createDTO.getIngredients());
        
        // Create recipe entity
        LocalDateTime now = LocalDateTime.now();
        Recipes recipe = Recipes.builder()
            .id(UUID.randomUUID().toString())
            .sellableItemId(sellableItemId)
            .createdAt(now)
            .updatedAt(now)
            .build();
        
        Recipes savedRecipe = recipesRepository.save(recipe);
        log.info("Recipe created with ID: {}", savedRecipe.getId());
        
        // Create ingredients
        List<Ingredient> ingredients = createDTO.getIngredients().stream()
            .map(ingredientDTO -> {
                Ingredient ingredient = Ingredient.builder()
                    .id(UUID.randomUUID().toString())
                    .recipeId(savedRecipe.getId())
                    .inventoryItemId(ingredientDTO.getInventoryItemId())
                    .quantity(ingredientDTO.getQuantity())
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
                return ingredientRepository.save(ingredient);
            })
            .collect(Collectors.toList());
        
        savedRecipe.setIngredients(ingredients);
        
        return convertToResponseDTO(savedRecipe);
    }

    @Override
    public RecipeResponseDTO update(String id, RecipeUpdateDTO updateDTO) {
        log.info("Updating recipe with ID: {}", id);
        
        // Find existing recipe
        Recipes recipe = recipesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Recipe", id));
        
        // Validate sellable item if changed
        Long newSellableItemId = Long.parseLong(updateDTO.getSellableItemId());
        if (!recipe.getSellableItemId().equals(newSellableItemId)) {
            SellableItem sellableItem = sellableItemRepository.findById(newSellableItemId)
                .orElseThrow(() -> new ForeignKeyConstraintException("sellable_item_id", updateDTO.getSellableItemId()));
            
            // Check if another recipe exists for this sellable item
            recipesRepository.findBySellableItemId(newSellableItemId)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new com.example.stock.exception.DuplicateResourceException(
                            "Recipe already exists for sellable_item_id: " + newSellableItemId);
                    }
                });
            
            recipe.setSellableItemId(newSellableItemId);
        }
        
        // Validate all inventory items exist
        validateInventoryItemsForUpdate(updateDTO.getIngredients());
        
        // Delete existing ingredients
        ingredientRepository.deleteByRecipeId(id);
        
        // Create new ingredients
        LocalDateTime now = LocalDateTime.now();
        List<Ingredient> ingredients = updateDTO.getIngredients().stream()
            .map(ingredientDTO -> {
                Ingredient ingredient = Ingredient.builder()
                    .id(UUID.randomUUID().toString())
                    .recipeId(id)
                    .inventoryItemId(ingredientDTO.getInventoryItemId())
                    .quantity(ingredientDTO.getQuantity())
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
                return ingredientRepository.save(ingredient);
            })
            .collect(Collectors.toList());
        
        recipe.setIngredients(ingredients);
        recipe.setUpdatedAt(now);
        
        Recipes updatedRecipe = recipesRepository.save(recipe);
        log.info("Recipe updated successfully with ID: {}", updatedRecipe.getId());
        
        return convertToResponseDTO(updatedRecipe);
    }

    @Override
    public void delete(String id) {
        log.info("Deleting recipe with ID: {}", id);
        
        Recipes recipe = recipesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Recipe", id));
        
        // Delete ingredients (cascade should handle this, but being explicit)
        ingredientRepository.deleteByRecipeId(id);
        
        // Delete recipe
        recipesRepository.deleteById(id);
        
        log.info("Recipe deleted successfully with ID: {}", id);
    }

    private void validateInventoryItems(List<RecipeCreateDTO.IngredientDTO> ingredients) {
        for (RecipeCreateDTO.IngredientDTO ingredientDTO : ingredients) {
            if (!inventoryItemRepository.existsById(ingredientDTO.getInventoryItemId())) {
                throw new ForeignKeyConstraintException("inventory_item_id", ingredientDTO.getInventoryItemId());
            }
        }
    }

    private void validateInventoryItemsForUpdate(List<RecipeUpdateDTO.IngredientDTO> ingredients) {
        for (RecipeUpdateDTO.IngredientDTO ingredientDTO : ingredients) {
            if (!inventoryItemRepository.existsById(ingredientDTO.getInventoryItemId())) {
                throw new ForeignKeyConstraintException("inventory_item_id", ingredientDTO.getInventoryItemId());
            }
        }
    }

    private RecipeResponseDTO convertToResponseDTO(Recipes recipe) {
        RecipeResponseDTO responseDTO = RecipeResponseDTO.builder()
            .id(recipe.getId())
            .sellableItemId(recipe.getSellableItemId())
            .createdAt(recipe.getCreatedAt())
            .updatedAt(recipe.getUpdatedAt())
            .build();
        
        if (recipe.getIngredients() != null) {
            List<RecipeResponseDTO.IngredientResponseDTO> ingredientDTOs = recipe.getIngredients().stream()
                .map(ingredient -> RecipeResponseDTO.IngredientResponseDTO.builder()
                    .id(ingredient.getId())
                    .inventoryItemId(ingredient.getInventoryItemId())
                    .quantity(ingredient.getQuantity())
                    .createdAt(ingredient.getCreatedAt())
                    .updatedAt(ingredient.getUpdatedAt())
                    .build())
                .collect(Collectors.toList());
            responseDTO.setIngredients(ingredientDTOs);
        }
        
        return responseDTO;
    }
}