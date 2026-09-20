package com.sownly.nufli.domain.nutrition;

import com.sownly.nufli.domain.identity.ActivityLevel;
import com.sownly.nufli.domain.identity.BiologicalSex;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class NutritionCalculator {

    private NutritionCalculator() {}

    /**
     * Calculates Basal Metabolic Rate (BMR) using the Mifflin-St Jeor Equation.
     * Men: 10 * weight(kg) + 6.25 * height(cm) - 5 * age + 5
     * Women: 10 * weight(kg) + 6.25 * height(cm) - 5 * age - 161
     */
    public static BigDecimal calculateBmr(BigDecimal weightKg, BigDecimal heightCm, int ageYears, BiologicalSex sex) {
        if (weightKg == null || heightCm == null || ageYears <= 0) {
            return BigDecimal.ZERO;
        }

        double w = weightKg.doubleValue();
        double h = heightCm.doubleValue();
        double base = (10.0 * w) + (6.25 * h) - (5.0 * ageYears);

        double bmr = switch (sex) {
            case MALE -> base + 5.0;
            case FEMALE -> base - 161.0;
            case UNSPECIFIED -> base - 78.0; // midpoint
        };

        return BigDecimal.valueOf(Math.max(bmr, 800.0)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates Total Daily Energy Expenditure (TDEE) based on activity multiplier.
     */
    public static BigDecimal calculateTdee(BigDecimal bmr, ActivityLevel activityLevel) {
        if (bmr == null || bmr.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        ActivityLevel level = activityLevel != null ? activityLevel : ActivityLevel.SEDENTARY;
        return bmr.multiply(BigDecimal.valueOf(level.getMultiplier())).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates target calories by applying Goal adjustment.
     */
    public static BigDecimal calculateTargetCalories(BigDecimal tdee, GoalType goalType) {
        if (tdee == null || tdee.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.valueOf(2000.0);
        }
        GoalType goal = goalType != null ? goalType : GoalType.MAINTAIN;
        BigDecimal target = tdee.add(BigDecimal.valueOf(goal.getCalorieAdjustment()));
        // Safe lower bound of 1200 kcal
        return target.max(BigDecimal.valueOf(1200.0)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Recommends macro targets based on body weight and calorie target:
     * - Protein: 2.0 g per kg of bodyweight
     * - Fat: 25% of target calories (9 kcal/g)
     * - Carbs: Remaining calories (4 kcal/g)
     */
    public static MacroTargets calculateMacroTargets(BigDecimal targetCalories, BigDecimal weightKg) {
        double kcal = targetCalories != null ? targetCalories.doubleValue() : 2000.0;
        double w = weightKg != null ? weightKg.doubleValue() : 70.0;

        double proteinG = w * 2.0;
        double proteinKcal = proteinG * 4.0;

        double fatKcal = kcal * 0.25;
        double fatG = fatKcal / 9.0;

        double remainingKcal = Math.max(kcal - proteinKcal - fatKcal, 0.0);
        double carbsG = remainingKcal / 4.0;

        return new MacroTargets(
            BigDecimal.valueOf(kcal).setScale(2, RoundingMode.HALF_UP),
            BigDecimal.valueOf(proteinG).setScale(2, RoundingMode.HALF_UP),
            BigDecimal.valueOf(carbsG).setScale(2, RoundingMode.HALF_UP),
            BigDecimal.valueOf(fatG).setScale(2, RoundingMode.HALF_UP)
        );
    }

    /**
     * Computes scaled nutritional values for a given quantity in grams against a base per 100g.
     */
    public static BigDecimal scaleNutrient(BigDecimal nutrientPer100g, BigDecimal quantityGrams) {
        if (nutrientPer100g == null || quantityGrams == null) {
            return BigDecimal.ZERO;
        }
        return nutrientPer100g.multiply(quantityGrams)
            .divide(BigDecimal.valueOf(100.0), 2, RoundingMode.HALF_UP);
    }

    public record MacroTargets(
        BigDecimal calories,
        BigDecimal proteinGrams,
        BigDecimal carbsGrams,
        BigDecimal fatGrams
    ) {}
}
