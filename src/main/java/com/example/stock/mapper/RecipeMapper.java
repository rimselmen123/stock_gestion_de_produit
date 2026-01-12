package com.example.stock.mapper;

import com.example.stock.dto.recipe.RecipeCreateDTO;
import com.example.stock.dto.recipe.RecipeResponseDTO;
import com.example.stock.entity.Ingredient;
import com.example.stock.entity.Recipes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RecipeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "sellableItem", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    Recipes toEntity(RecipeCreateDTO createDTO);

    @Mapping(target = "ingredients", source = "ingredients")
    RecipeResponseDTO toResponseDTO(Recipes recipe);

    List<RecipeResponseDTO> toResponseDTOList(List<Recipes> recipes);

    default RecipeResponseDTO.IngredientResponseDTO ingredientToResponseDTO(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }
        return RecipeResponseDTO.IngredientResponseDTO.builder()
            .id(ingredient.getId())
            .inventoryItemId(ingredient.getInventoryItemId())
            .quantity(ingredient.getQuantity())
            .createdAt(ingredient.getCreatedAt())
            .updatedAt(ingredient.getUpdatedAt())
            .build();
    }

    List<RecipeResponseDTO.IngredientResponseDTO> ingredientsToResponseDTOList(List<Ingredient> ingredients);
}