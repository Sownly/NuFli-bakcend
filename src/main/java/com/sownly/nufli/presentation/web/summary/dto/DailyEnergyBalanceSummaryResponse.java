package com.sownly.nufli.presentation.web.summary.dto;

import com.sownly.nufli.presentation.web.fitness.dto.DailyFitnessSummaryResponse;
import com.sownly.nufli.presentation.web.nutrition.dto.DailyNutritionSummaryResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyEnergyBalanceSummaryResponse(
    LocalDate date,
    DailyNutritionSummaryResponse nutrition,
    DailyFitnessSummaryResponse fitness,
    BigDecimal netCalories,
    String energyBalancePhilosophy
) {}
