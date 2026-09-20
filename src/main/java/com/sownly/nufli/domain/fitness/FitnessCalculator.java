package com.sownly.nufli.domain.fitness;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class FitnessCalculator {

    private FitnessCalculator() {}

    /**
     * Calculates volume load for a set: weight * reps
     */
    public static BigDecimal calculateSetVolume(BigDecimal weightKg, int reps) {
        if (weightKg == null || reps <= 0) {
            return BigDecimal.ZERO;
        }
        return weightKg.multiply(BigDecimal.valueOf(reps)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates estimated One Repetition Maximum (1RM) using the Epley formula:
     * 1RM = Weight * (1 + Reps / 30)
     */
    public static BigDecimal calculateEstimated1RM(BigDecimal weightKg, int reps) {
        if (weightKg == null || reps <= 0) {
            return BigDecimal.ZERO;
        }
        if (reps == 1) {
            return weightKg.setScale(2, RoundingMode.HALF_UP);
        }
        double w = weightKg.doubleValue();
        double epley = w * (1.0 + (reps / 30.0));
        return BigDecimal.valueOf(epley).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Estimates calories burned during cardio using Metabolic Equivalent of Task (MET):
     * Calories = (Duration in minutes / 60) * MET * User Weight (kg)
     */
    public static BigDecimal estimateCardioCaloriesBurned(ActivityType activityType, int durationMinutes, BigDecimal userWeightKg) {
        if (durationMinutes <= 0 || userWeightKg == null || userWeightKg.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        ActivityType type = activityType != null ? activityType : ActivityType.OTHER;
        double met = type.getDefaultMet();
        double hours = durationMinutes / 60.0;
        double weight = userWeightKg.doubleValue();

        double burned = hours * met * weight;
        return BigDecimal.valueOf(burned).setScale(2, RoundingMode.HALF_UP);
    }
}
