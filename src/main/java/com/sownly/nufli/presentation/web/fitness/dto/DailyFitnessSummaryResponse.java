package com.sownly.nufli.presentation.web.fitness.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DailyFitnessSummaryResponse(
    LocalDate date,
    int workoutsCompleted,
    long totalWorkoutDurationMinutes,
    BigDecimal totalVolumeKg,
    int cardioActivitiesCompleted,
    int totalCardioDurationMinutes,
    BigDecimal totalEstimatedCaloriesBurned,
    List<WorkoutResponse> workouts,
    List<ActivityResponse> activities
) {}
