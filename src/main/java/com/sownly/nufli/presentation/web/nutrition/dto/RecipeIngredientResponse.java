package com.sownly.nufli.presentation.web.nutrition.dto;

import com.sownly.nufli.domain.nutrition.RecipeIngredient;

import java.math.BigDecimal;
import java.util.UUID;

public record RecipeIngredientResponse(
    UUID id,
    UUID foodId,
    String foodName,
    String brand,
    BigDecimal quantityGrams
) {
    public static RecipeIngredientResponse from(RecipeIngredient ingredient) {
        return new RecipeIngredientResponse(
            ingredient.getId(),
            ingredient.getFood() != null ? ingredient.getFood().getId() : null,
            ingredient.getFood() != null ? ingredient.getFood().getName() : "Unknown Food",
            ingredient.getFood() != null ? ingredient.getFood().getBrand() : null,
            ingredient.getQuantityGrams()
        );
    }
}
