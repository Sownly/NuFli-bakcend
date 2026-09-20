package com.sownly.nufli.presentation.web.nutrition.dto;

import com.sownly.nufli.domain.nutrition.Recipe;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RecipeRequest(
    @NotBlank(message = "Recipe name is required")
    String name,

    String description,

    @Min(value = 1, message = "Servings must be at least 1")
    int servings,

    @Min(value = 0, message = "Preparation time cannot be negative")
    int preparationTimeMinutes,

    String instructions,

    @NotEmpty(message = "Recipe must contain at least one ingredient")
    @Valid
    List<RecipeIngredientRequest> ingredients
) {}
