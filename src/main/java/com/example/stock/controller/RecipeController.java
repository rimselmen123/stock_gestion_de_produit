package com.example.stock.controller;

import com.example.stock.dto.recipe.RecipeCreateDTO;
import com.example.stock.dto.recipe.RecipeResponseDTO;
import com.example.stock.dto.recipe.RecipeUpdateDTO;
import com.example.stock.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    @Operation(summary = "Get all recipes")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all recipes")
    public ResponseEntity<List<RecipeResponseDTO>> getAllRecipes() {
        log.debug("Getting all recipes");
        List<RecipeResponseDTO> recipes = recipeService.findAll();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get recipe by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved recipe")
    @ApiResponse(responseCode = "404", description = "Recipe not found")
    public ResponseEntity<RecipeResponseDTO> getRecipeById(
            @Parameter(description = "Recipe ID") @PathVariable String id) {
        log.debug("Getting recipe with ID: {}", id);
        RecipeResponseDTO recipe = recipeService.findById(id);
        return ResponseEntity.ok(recipe);
    }

    @PostMapping
    @Operation(summary = "Create a new recipe")
    @ApiResponse(responseCode = "201", description = "Recipe created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<RecipeResponseDTO> createRecipe(
            @Valid @RequestBody RecipeCreateDTO createDTO) {
        log.info("Creating new recipe for sellable_item_id: {}", createDTO.getSellableItemId());
        RecipeResponseDTO createdRecipe = recipeService.create(createDTO);
        return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing recipe")
    @ApiResponse(responseCode = "200", description = "Recipe updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Recipe not found")
    public ResponseEntity<RecipeResponseDTO> updateRecipe(
            @Parameter(description = "Recipe ID to update") @PathVariable String id,
            @Valid @RequestBody RecipeUpdateDTO updateDTO) {
        log.info("Updating recipe with ID: {}", id);
        RecipeResponseDTO updatedRecipe = recipeService.update(id, updateDTO);
        return ResponseEntity.ok(updatedRecipe);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a recipe by ID")
    @ApiResponse(responseCode = "200", description = "Recipe deleted successfully")
    @ApiResponse(responseCode = "404", description = "Recipe not found")
    public ResponseEntity<Void> deleteRecipe(
            @Parameter(description = "Recipe ID to delete") @PathVariable String id) {
        log.info("Deleting recipe with ID: {}", id);
        recipeService.delete(id);
        return ResponseEntity.ok().build();
    }
}