package com.sownly.nufli.presentation.web.nutrition.dto;

import com.sownly.nufli.domain.nutrition.RecipeIngredient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record RecipeIngredientRequest(
    @NotNull(message = "Food ID is required")
    UUID foodId,

    @NotNull(message = "Quantity in grams is required")
    @DecimalMin(value = "0.1", message = "Quantity must be greater than 0")
    BigDecimal quantityGrams
) {}
