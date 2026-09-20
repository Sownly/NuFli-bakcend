package com.sownly.nufli.presentation.web.fitness.dto;

import com.sownly.nufli.domain.fitness.ExerciseSet;
import com.sownly.nufli.domain.fitness.WorkoutExercise;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record WorkoutExerciseResponse(
    UUID id,
    UUID exerciseId,
    String exerciseName,
    String primaryMuscleGroup,
    int orderIndex,
    String notes,
    BigDecimal exerciseVolumeKg,
    List<ExerciseSetResponse> sets
) {
    public static WorkoutExerciseResponse from(WorkoutExercise workoutExercise) {
        List<ExerciseSetResponse> setResponses = workoutExercise.getSets().stream()
            .map(ExerciseSetResponse::from)
            .toList();

        BigDecimal volume = workoutExercise.getSets().stream()
            .filter(ExerciseSet::isCompleted)
            .map(s -> s.getWeightKg().multiply(BigDecimal.valueOf(s.getReps())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new WorkoutExerciseResponse(
            workoutExercise.getId(),
            workoutExercise.getExercise() != null ? workoutExercise.getExercise().getId() : null,
            workoutExercise.getExercise() != null ? workoutExercise.getExercise().getName() : "Unknown",
            workoutExercise.getExercise() != null ? workoutExercise.getExercise().getPrimaryMuscleGroup().name() : null,
            workoutExercise.getOrderIndex(),
            workoutExercise.getNotes(),
            volume,
            setResponses
        );
    }
}
