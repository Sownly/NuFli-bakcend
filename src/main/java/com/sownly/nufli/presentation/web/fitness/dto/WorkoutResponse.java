package com.sownly.nufli.presentation.web.fitness.dto;

import com.sownly.nufli.domain.fitness.Workout;
import com.sownly.nufli.domain.fitness.WorkoutStatus;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record WorkoutResponse(
    UUID id,
    UUID userId,
    UUID routineId,
    String name,
    Instant startedAt,
    Instant completedAt,
    WorkoutStatus status,
    String notes,
    Long durationMinutes,
    BigDecimal totalVolumeKg,
    List<WorkoutExerciseResponse> exercises
) {
    public static WorkoutResponse from(Workout workout) {
        Long duration = null;
        if (workout.getStartedAt() != null && workout.getCompletedAt() != null) {
            duration = Duration.between(workout.getStartedAt(), workout.getCompletedAt()).toMinutes();
        }

        List<WorkoutExerciseResponse> exResponses = workout.getExercises().stream()
            .map(WorkoutExerciseResponse::from)
            .toList();

        return new WorkoutResponse(
            workout.getId(),
            workout.getUserId(),
            workout.getRoutineId(),
            workout.getName(),
            workout.getStartedAt(),
            workout.getCompletedAt(),
            workout.getStatus(),
            workout.getNotes(),
            duration,
            workout.calculateTotalVolume(),
            exResponses
        );
    }
}
