package com.sownly.nufli.presentation.web.fitness.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record WorkoutExerciseRequest(
    @NotNull(message = "Exercise ID is required")
    UUID exerciseId,

    int orderIndex,
    String notes,

    @Valid
    List<ExerciseSetRequest> sets
) {}
