package com.sownly.nufli.presentation.web.nutrition.dto;

import com.sownly.nufli.domain.nutrition.Food;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record FoodRequest(
    @NotBlank(message = "Food name is required")
    String name,

    String brand,

    String servingUnit,

    @DecimalMin(value = "0.1", message = "Serving size must be greater than 0")
    BigDecimal servingSizeGrams,

    @NotNull(message = "Calories per 100g is required")
    @DecimalMin(value = "0.0", message = "Calories cannot be negative")
    BigDecimal caloriesPer100g,

    BigDecimal proteinPer100g,
    BigDecimal carbsPer100g,
    BigDecimal fatPer100g,
    BigDecimal fiberPer100g
) {}
