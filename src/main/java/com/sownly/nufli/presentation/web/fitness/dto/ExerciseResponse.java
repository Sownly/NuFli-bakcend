package com.sownly.nufli.presentation.web.fitness.dto;

import com.sownly.nufli.domain.fitness.Exercise;
import com.sownly.nufli.domain.fitness.ExerciseCategory;
import com.sownly.nufli.domain.fitness.MuscleGroup;

import java.util.UUID;

public record ExerciseResponse(
    UUID id,
    UUID userId,
    String name,
    MuscleGroup primaryMuscleGroup,
    String secondaryMuscleGroups,
    ExerciseCategory category,
    String instructions,
    boolean verified
) {
    public static ExerciseResponse from(Exercise exercise) {
        return new ExerciseResponse(
            exercise.getId(),
            exercise.getUserId(),
            exercise.getName(),
            exercise.getPrimaryMuscleGroup(),
            exercise.getSecondaryMuscleGroups(),
            exercise.getCategory(),
            exercise.getInstructions(),
            exercise.isVerified()
        );
    }
}
