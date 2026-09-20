package com.sownly.nufli.presentation.web.fitness.dto;

import com.sownly.nufli.domain.fitness.ExerciseSet;
import com.sownly.nufli.domain.fitness.FitnessCalculator;
import com.sownly.nufli.domain.fitness.SetType;

import java.math.BigDecimal;
import java.util.UUID;

public record ExerciseSetResponse(
    UUID id,
    int setNumber,
    SetType setType,
    BigDecimal weightKg,
    int reps,
    BigDecimal rpe,
    boolean isCompleted,
    BigDecimal estimated1RM,
    BigDecimal volumeKg
) {
    public static ExerciseSetResponse from(ExerciseSet set) {
        BigDecimal e1rm = FitnessCalculator.calculateEstimated1RM(set.getWeightKg(), set.getReps());
        BigDecimal vol = FitnessCalculator.calculateSetVolume(set.getWeightKg(), set.getReps());

        return new ExerciseSetResponse(
            set.getId(),
            set.getSetNumber(),
            set.getSetType(),
            set.getWeightKg(),
            set.getReps(),
            set.getRpe(),
            set.isCompleted(),
            e1rm,
            vol
        );
    }
}
