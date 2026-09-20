package com.sownly.nufli.domain.fitness;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false)
    private ActivityType activityType;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt = Instant.now();

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(name = "distance_km", precision = 6, scale = 2)
    private BigDecimal distanceKm;

    @Column(name = "estimated_calories_burned", nullable = false, precision = 8, scale = 2)
    private BigDecimal estimatedCaloriesBurned;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Activity() {}

    public Activity(UUID userId, ActivityType activityType, Instant startedAt, int durationMinutes,
                    BigDecimal distanceKm, BigDecimal estimatedCaloriesBurned, String notes) {
        this.userId = userId;
        this.activityType = activityType;
        this.startedAt = startedAt != null ? startedAt : Instant.now();
        this.durationMinutes = durationMinutes;
        this.distanceKm = distanceKm;
        this.estimatedCaloriesBurned = estimatedCaloriesBurned;
        this.notes = notes;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public ActivityType getActivityType() { return activityType; }
    public void setActivityType(ActivityType activityType) { this.activityType = activityType; }
    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public BigDecimal getDistanceKm() { return distanceKm; }
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }
    public BigDecimal getEstimatedCaloriesBurned() { return estimatedCaloriesBurned; }
    public void setEstimatedCaloriesBurned(BigDecimal estimatedCaloriesBurned) { this.estimatedCaloriesBurned = estimatedCaloriesBurned; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Instant getCreatedAt() { return createdAt; }
}
