package com.sownly.nufli.presentation.web.fitness.dto;

import com.sownly.nufli.domain.fitness.Exercise;
import com.sownly.nufli.domain.fitness.ExerciseCategory;
import com.sownly.nufli.domain.fitness.MuscleGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ExerciseRequest(
    @NotBlank(message = "Exercise name is required")
    String name,

    @NotNull(message = "Primary muscle group is required")
    MuscleGroup primaryMuscleGroup,

    String secondaryMuscleGroups,

    ExerciseCategory category,

    String instructions
) {}
