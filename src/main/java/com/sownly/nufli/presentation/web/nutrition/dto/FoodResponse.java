package com.sownly.nufli.presentation.web.nutrition.dto;

import com.sownly.nufli.domain.nutrition.Food;

import java.math.BigDecimal;
import java.util.UUID;

public record FoodResponse(
    UUID id,
    UUID userId,
    String name,
    String brand,
    String servingUnit,
    BigDecimal servingSizeGrams,
    BigDecimal caloriesPer100g,
    BigDecimal proteinPer100g,
    BigDecimal carbsPer100g,
    BigDecimal fatPer100g,
    BigDecimal fiberPer100g,
    boolean verified
) {
    public static FoodResponse from(Food food) {
        return new FoodResponse(
            food.getId(),
            food.getUserId(),
            food.getName(),
            food.getBrand(),
            food.getServingUnit(),
            food.getServingSizeGrams(),
            food.getCaloriesPer100g(),
            food.getProteinPer100g(),
            food.getCarbsPer100g(),
            food.getFatPer100g(),
            food.getFiberPer100g(),
            food.isVerified()
        );
    }
}
