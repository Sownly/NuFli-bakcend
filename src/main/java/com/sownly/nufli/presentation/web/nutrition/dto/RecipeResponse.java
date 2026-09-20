package com.sownly.nufli.presentation.web.nutrition.dto;

import com.sownly.nufli.domain.nutrition.Recipe;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RecipeResponse(
    UUID id,
    UUID userId,
    String name,
    String description,
    int servings,
    int preparationTimeMinutes,
    String instructions,
    BigDecimal totalCalories,
    BigDecimal totalProtein,
    BigDecimal totalCarbs,
    BigDecimal totalFat,
    BigDecimal caloriesPerServing,
    BigDecimal proteinPerServing,
    BigDecimal carbsPerServing,
    BigDecimal fatPerServing,
    List<RecipeIngredientResponse> ingredients,
    Instant createdAt
) {
    public static RecipeResponse from(Recipe recipe) {
        int s = Math.max(1, recipe.getServings());
        BigDecimal servingsBd = BigDecimal.valueOf(s);

        List<RecipeIngredientResponse> ingredientResponses = recipe.getIngredients().stream()
            .map(RecipeIngredientResponse::from)
            .toList();

        return new RecipeResponse(
            recipe.getId(),
            recipe.getUserId(),
            recipe.getName(),
            recipe.getDescription(),
            recipe.getServings(),
            recipe.getPreparationTimeMinutes(),
            recipe.getInstructions(),
            recipe.getTotalCalories(),
            recipe.getTotalProtein(),
            recipe.getTotalCarbs(),
            recipe.getTotalFat(),
            recipe.getTotalCalories().divide(servingsBd, 2, RoundingMode.HALF_UP),
            recipe.getTotalProtein().divide(servingsBd, 2, RoundingMode.HALF_UP),
            recipe.getTotalCarbs().divide(servingsBd, 2, RoundingMode.HALF_UP),
            recipe.getTotalFat().divide(servingsBd, 2, RoundingMode.HALF_UP),
            ingredientResponses,
            recipe.getCreatedAt()
        );
    }
}
