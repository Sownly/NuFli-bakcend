package com.sownly.nufli.presentation.web.fitness.dto;

import com.sownly.nufli.domain.fitness.RoutineExercise;

import java.util.UUID;

public record RoutineExerciseResponse(
    UUID id,
    UUID exerciseId,
    String exerciseName,
    String primaryMuscleGroup,
    int orderIndex,
    int targetSets,
    int targetReps
) {
    public static RoutineExerciseResponse from(RoutineExercise routineExercise) {
        return new RoutineExerciseResponse(
            routineExercise.getId(),
            routineExercise.getExercise() != null ? routineExercise.getExercise().getId() : null,
            routineExercise.getExercise() != null ? routineExercise.getExercise().getName() : "Unknown",
            routineExercise.getExercise() != null ? routineExercise.getExercise().getPrimaryMuscleGroup().name() : null,
            routineExercise.getOrderIndex(),
            routineExercise.getTargetSets(),
            routineExercise.getTargetReps()
        );
    }
}
