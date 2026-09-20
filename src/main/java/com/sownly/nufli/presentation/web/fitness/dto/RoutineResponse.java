package com.sownly.nufli.presentation.web.fitness.dto;

import com.sownly.nufli.domain.fitness.Routine;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RoutineResponse(
    UUID id,
    UUID userId,
    String name,
    String description,
    List<RoutineExerciseResponse> exercises,
    Instant createdAt
) {
    public static RoutineResponse from(Routine routine) {
        List<RoutineExerciseResponse> exResponses = routine.getExercises().stream()
            .map(RoutineExerciseResponse::from)
            .toList();

        return new RoutineResponse(
            routine.getId(),
            routine.getUserId(),
            routine.getName(),
            routine.getDescription(),
            exResponses,
            routine.getCreatedAt()
        );
    }
}
