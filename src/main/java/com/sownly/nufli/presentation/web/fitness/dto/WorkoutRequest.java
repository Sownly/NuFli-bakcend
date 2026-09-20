package com.sownly.nufli.presentation.web.fitness.dto;

import com.sownly.nufli.domain.fitness.WorkoutStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record WorkoutRequest(
    UUID routineId,

    @NotBlank(message = "Workout name is required")
    String name,

    Instant startedAt,
    Instant completedAt,
    WorkoutStatus status,
    String notes,

    @Valid
    List<WorkoutExerciseRequest> exercises
) {}
