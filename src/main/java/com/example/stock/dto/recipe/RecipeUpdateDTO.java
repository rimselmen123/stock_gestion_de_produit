package com.example.stock.dto.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeUpdateDTO {

    @NotBlank(message = "sellable_item_id is required")
    @JsonProperty("sellable_item_id")
    private String sellableItemId;

    @NotEmpty(message = "ingredients list cannot be empty")
    @Valid
    private List<IngredientDTO> ingredients;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class IngredientDTO {
        @NotBlank(message = "inventory_item_id is required")
        @JsonProperty("inventory_item_id")
        private String inventoryItemId;

        @JsonProperty("quantity")
        private BigDecimal quantity;
    }
}