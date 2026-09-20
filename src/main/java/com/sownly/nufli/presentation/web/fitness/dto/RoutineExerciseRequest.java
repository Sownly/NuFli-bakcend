package com.sownly.nufli.presentation.web.fitness.dto;

import com.sownly.nufli.domain.fitness.RoutineExercise;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RoutineExerciseRequest(
    @NotNull(message = "Exercise ID is required")
    UUID exerciseId,

    int orderIndex,

    @Min(value = 1, message = "Target sets must be at least 1")
    int targetSets,

    @Min(value = 1, message = "Target reps must be at least 1")
    int targetReps
) {}
