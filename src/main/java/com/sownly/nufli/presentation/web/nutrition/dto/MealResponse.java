package com.sownly.nufli.presentation.web.nutrition.dto;

import com.sownly.nufli.domain.nutrition.Meal;
import com.sownly.nufli.domain.nutrition.MealType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record MealResponse(
    UUID id,
    UUID userId,
    MealType mealType,
    LocalDate loggedDate,
    Instant loggedAt,
    BigDecimal totalCalories,
    BigDecimal totalProtein,
    BigDecimal totalCarbs,
    BigDecimal totalFat,
    List<MealItemResponse> items
) {
    public static MealResponse from(Meal meal) {
        List<MealItemResponse> itemResponses = meal.getItems().stream()
            .map(MealItemResponse::from)
            .toList();

        return new MealResponse(
            meal.getId(),
            meal.getUserId(),
            meal.getMealType(),
            meal.getLoggedDate(),
            meal.getLoggedAt(),
            meal.getTotalCalories(),
            meal.getTotalProtein(),
            meal.getTotalCarbs(),
            meal.getTotalFat(),
            itemResponses
        );
    }
}
