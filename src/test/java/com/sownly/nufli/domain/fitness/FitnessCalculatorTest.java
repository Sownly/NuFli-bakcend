package com.sownly.nufli.domain.fitness;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FitnessCalculatorTest {

    @Test
    @DisplayName("Calculate Set Volume: 100kg x 8 reps = 800kg")
    void testCalculateSetVolume() {
        BigDecimal weight = BigDecimal.valueOf(100.0);
        int reps = 8;
        BigDecimal volume = FitnessCalculator.calculateSetVolume(weight, reps);
        assertEquals(new BigDecimal("800.00"), volume);
    }

    @Test
    @DisplayName("Calculate Estimated 1RM via Epley: 100kg x 10 reps = 133.33kg")
    void testCalculateEstimated1RM() {
        BigDecimal weight = BigDecimal.valueOf(100.0);
        int reps = 10;
        // 100 * (1 + 10/30) = 100 * 1.3333333333 = 133.33
        BigDecimal e1rm = FitnessCalculator.calculateEstimated1RM(weight, reps);
        assertEquals(new BigDecimal("133.33"), e1rm);
    }

    @Test
    @DisplayName("Calculate Estimated 1RM for 1 rep: exact weight")
    void testCalculateEstimated1RMSingleRep() {
        BigDecimal weight = BigDecimal.valueOf(140.0);
        BigDecimal e1rm = FitnessCalculator.calculateEstimated1RM(weight, 1);
        assertEquals(new BigDecimal("140.00"), e1rm);
    }

    @Test
    @DisplayName("Estimate Cardio Calories Burned: Running (MET 9.8) for 45 min at 75kg")
    void testEstimateCardioCalories() {
        // hours = 45 / 60 = 0.75
        // burned = 0.75 * 9.8 * 75 = 551.25
        BigDecimal burned = FitnessCalculator.estimateCardioCaloriesBurned(ActivityType.RUNNING, 45, BigDecimal.valueOf(75.0));
        assertEquals(new BigDecimal("551.25"), burned);
    }
}
