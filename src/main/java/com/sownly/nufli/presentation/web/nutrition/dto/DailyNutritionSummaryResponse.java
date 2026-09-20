package com.sownly.nufli.presentation.web.nutrition.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DailyNutritionSummaryResponse(
    LocalDate date,
    BigDecimal consumedCalories,
    BigDecimal targetCalories,
    BigDecimal remainingCalories,
    BigDecimal consumedProtein,
    BigDecimal targetProtein,
    BigDecimal consumedCarbs,
    BigDecimal targetCarbs,
    BigDecimal consumedFat,
    BigDecimal targetFat,
    int mealsCount,
    List<MealResponse> meals
) {}
