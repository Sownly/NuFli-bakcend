package com.sownly.nufli.application.summary;

import com.sownly.nufli.application.fitness.FitnessService;
import com.sownly.nufli.application.nutrition.NutritionService;
import com.sownly.nufli.presentation.web.fitness.dto.DailyFitnessSummaryResponse;
import com.sownly.nufli.presentation.web.nutrition.dto.DailyNutritionSummaryResponse;
import com.sownly.nufli.presentation.web.summary.dto.DailyEnergyBalanceSummaryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SummaryService {

    private final NutritionService nutritionService;
    private final FitnessService fitnessService;

    public SummaryService(NutritionService nutritionService, FitnessService fitnessService) {
        this.nutritionService = nutritionService;
        this.fitnessService = fitnessService;
    }

    public DailyEnergyBalanceSummaryResponse getDailySummary(UUID userId, LocalDate date) {
        DailyNutritionSummaryResponse nutrition = nutritionService.getDailySummary(userId, date);
        DailyFitnessSummaryResponse fitness = fitnessService.getDailyFitnessSummary(userId, date);

        BigDecimal netCalories = nutrition.consumedCalories().subtract(fitness.totalEstimatedCaloriesBurned())
            .setScale(2, RoundingMode.HALF_UP);

        String philosophyNote = "Simplify your life: Exercise calorie expenditure is informational and " +
            "not automatically added to your food target to prevent overestimation errors.";

        return new DailyEnergyBalanceSummaryResponse(
            date,
            nutrition,
            fitness,
            netCalories,
            philosophyNote
        );
    }
}
