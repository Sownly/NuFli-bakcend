package com.sownly.nufli.presentation.web.fitness.dto;

import com.sownly.nufli.domain.fitness.Routine;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RoutineRequest(
    @NotBlank(message = "Routine name is required")
    String name,

    String description,

    @NotEmpty(message = "Routine must contain at least one exercise")
    @Valid
    List<RoutineExerciseRequest> exercises
) {}
