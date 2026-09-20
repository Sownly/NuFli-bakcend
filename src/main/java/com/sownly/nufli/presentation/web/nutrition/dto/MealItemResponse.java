package com.sownly.nufli.presentation.web.nutrition.dto;

import com.sownly.nufli.domain.nutrition.MealItem;

import java.math.BigDecimal;
import java.util.UUID;

public record MealItemResponse(
    UUID id,
    UUID foodId,
    String foodName,
    String brand,
    BigDecimal quantityGrams,
    BigDecimal calculatedCalories,
    BigDecimal calculatedProtein,
    BigDecimal calculatedCarbs,
    BigDecimal calculatedFat
) {
    public static MealItemResponse from(MealItem item) {
        return new MealItemResponse(
            item.getId(),
            item.getFood() != null ? item.getFood().getId() : null,
            item.getFood() != null ? item.getFood().getName() : "Unknown Food",
            item.getFood() != null ? item.getFood().getBrand() : null,
            item.getQuantityGrams(),
            item.getCalculatedCalories(),
            item.getCalculatedProtein(),
            item.getCalculatedCarbs(),
            item.getCalculatedFat()
        );
    }
}
