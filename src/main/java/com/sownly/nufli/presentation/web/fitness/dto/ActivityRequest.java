package com.sownly.nufli.presentation.web.fitness.dto;

import com.sownly.nufli.domain.fitness.Activity;
import com.sownly.nufli.domain.fitness.ActivityType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ActivityRequest(
    @NotNull(message = "Activity type is required")
    ActivityType activityType,

    Instant startedAt,

    @Min(value = 1, message = "Duration must be at least 1 minute")
    int durationMinutes,

    @DecimalMin(value = "0.0", message = "Distance cannot be negative")
    BigDecimal distanceKm,

    // If null, backend auto-estimates from MET & user profile weight
    BigDecimal estimatedCaloriesBurned,

    String notes
) {}
