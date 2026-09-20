package com.sownly.nufli.domain.nutrition;

import com.sownly.nufli.domain.identity.ActivityLevel;
import com.sownly.nufli.domain.identity.BiologicalSex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class NutritionCalculatorTest {

    @Test
    @DisplayName("Calculate BMR for Male using Mifflin-St Jeor")
    void testCalculateBmrMale() {
        // 80kg, 180cm, 25 years old
        // BMR = 10 * 80 + 6.25 * 180 - 5 * 25 + 5 = 800 + 1125 - 125 + 5 = 1805.00
        BigDecimal weight = BigDecimal.valueOf(80.0);
        BigDecimal height = BigDecimal.valueOf(180.0);
        int age = 25;

        BigDecimal bmr = NutritionCalculator.calculateBmr(weight, height, age, BiologicalSex.MALE);
        assertEquals(new BigDecimal("1805.00"), bmr);
    }

    @Test
    @DisplayName("Calculate BMR for Female using Mifflin-St Jeor")
    void testCalculateBmrFemale() {
        // 60kg, 165cm, 30 years old
        // BMR = 10 * 60 + 6.25 * 165 - 5 * 30 - 161 = 600 + 1031.25 - 150 - 161 = 1320.25
        BigDecimal weight = BigDecimal.valueOf(60.0);
        BigDecimal height = BigDecimal.valueOf(165.0);
        int age = 30;

        BigDecimal bmr = NutritionCalculator.calculateBmr(weight, height, age, BiologicalSex.FEMALE);
        assertEquals(new BigDecimal("1320.25"), bmr);
    }

    @Test
    @DisplayName("Calculate TDEE for Moderately Active level (multiplier 1.55)")
    void testCalculateTdee() {
        BigDecimal bmr = BigDecimal.valueOf(1805.00);
        BigDecimal tdee = NutritionCalculator.calculateTdee(bmr, ActivityLevel.MODERATELY_ACTIVE);
        // 1805.00 * 1.55 = 2797.75
        assertEquals(new BigDecimal("2797.75"), tdee);
    }

    @Test
    @DisplayName("Calculate Target Calories with Cut Goal (-500 kcal)")
    void testCalculateTargetCaloriesCut() {
        BigDecimal tdee = BigDecimal.valueOf(2500.00);
        BigDecimal target = NutritionCalculator.calculateTargetCalories(tdee, GoalType.CUT);
        assertEquals(new BigDecimal("2000.00"), target);
    }

    @Test
    @DisplayName("Calculate Target Calories with Bulk Goal (+300 kcal)")
    void testCalculateTargetCaloriesBulk() {
        BigDecimal tdee = BigDecimal.valueOf(2500.00);
        BigDecimal target = NutritionCalculator.calculateTargetCalories(tdee, GoalType.BULK);
        assertEquals(new BigDecimal("2800.00"), target);
    }

    @Test
    @DisplayName("Scale Nutrient for 200g of food with 165 kcal per 100g")
    void testScaleNutrient() {
        BigDecimal per100g = BigDecimal.valueOf(165.00);
        BigDecimal quantity = BigDecimal.valueOf(200.00);

        BigDecimal result = NutritionCalculator.scaleNutrient(per100g, quantity);
        assertEquals(new BigDecimal("330.00"), result);
    }
}
