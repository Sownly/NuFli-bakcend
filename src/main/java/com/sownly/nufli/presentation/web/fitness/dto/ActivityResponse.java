package com.sownly.nufli.presentation.web.fitness.dto;

import com.sownly.nufli.domain.fitness.Activity;
import com.sownly.nufli.domain.fitness.ActivityType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ActivityResponse(
    UUID id,
    UUID userId,
    ActivityType activityType,
    Instant startedAt,
    int durationMinutes,
    BigDecimal distanceKm,
    BigDecimal estimatedCaloriesBurned,
    String notes,
    Instant createdAt
) {
    public static ActivityResponse from(Activity activity) {
        return new ActivityResponse(
            activity.getId(),
            activity.getUserId(),
            activity.getActivityType(),
            activity.getStartedAt(),
            activity.getDurationMinutes(),
            activity.getDistanceKm(),
            activity.getEstimatedCaloriesBurned(),
            activity.getNotes(),
            activity.getCreatedAt()
        );
    }
}
