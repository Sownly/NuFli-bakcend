package com.sownly.nufli.presentation.web.nutrition.dto;

import com.sownly.nufli.domain.nutrition.GoalType;
import com.sownly.nufli.domain.nutrition.NutritionGoal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record NutritionGoalRequest(
    LocalDate effectiveDate,

    @NotNull(message = "Goal type is required")
    GoalType goalType,

    // If targetCalories is null, backend auto-computes from profile BMR/TDEE
    @DecimalMin(value = "500.0", message = "Target calories must be at least 500")
    BigDecimal targetCalories,

    BigDecimal targetProteinGrams,
    BigDecimal targetCarbsGrams,
    BigDecimal targetFatGrams
) {}
