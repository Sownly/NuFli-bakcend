package com.sownly.nufli.presentation.web.nutrition.dto;

import com.sownly.nufli.domain.nutrition.GoalType;
import com.sownly.nufli.domain.nutrition.NutritionGoal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record NutritionGoalResponse(
    UUID id,
    UUID userId,
    LocalDate effectiveDate,
    BigDecimal targetCalories,
    BigDecimal targetProteinGrams,
    BigDecimal targetCarbsGrams,
    BigDecimal targetFatGrams,
    GoalType goalType,
    boolean active
) {
    public static NutritionGoalResponse from(NutritionGoal goal) {
        if (goal == null) return null;
        return new NutritionGoalResponse(
            goal.getId(),
            goal.getUserId(),
            goal.getEffectiveDate(),
            goal.getTargetCalories(),
            goal.getTargetProteinGrams(),
            goal.getTargetCarbsGrams(),
            goal.getTargetFatGrams(),
            goal.getGoalType(),
            goal.isActive()
        );
    }
}
