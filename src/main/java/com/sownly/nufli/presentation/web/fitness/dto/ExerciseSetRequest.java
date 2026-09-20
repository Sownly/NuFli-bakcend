package com.sownly.nufli.presentation.web.fitness.dto;

import com.sownly.nufli.domain.fitness.ExerciseSet;
import com.sownly.nufli.domain.fitness.SetType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.UUID;

public record ExerciseSetRequest(
    int setNumber,
    SetType setType,

    @DecimalMin(value = "0.0", message = "Weight cannot be negative")
    BigDecimal weightKg,

    @Min(value = 0, message = "Reps cannot be negative")
    int reps,

    BigDecimal rpe,
    boolean isCompleted
) {}
