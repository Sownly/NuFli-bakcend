package com.sownly.nufli.domain.nutrition;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "nutrition_goals")
public class NutritionGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "target_calories", nullable = false, precision = 8, scale = 2)
    private BigDecimal targetCalories;

    @Column(name = "target_protein_grams", nullable = false, precision = 8, scale = 2)
    private BigDecimal targetProteinGrams;

    @Column(name = "target_carbs_grams", nullable = false, precision = 8, scale = 2)
    private BigDecimal targetCarbsGrams;

    @Column(name = "target_fat_grams", nullable = false, precision = 8, scale = 2)
    private BigDecimal targetFatGrams;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_type", nullable = false)
    private GoalType goalType = GoalType.MAINTAIN;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public NutritionGoal() {}

    public NutritionGoal(UUID userId, LocalDate effectiveDate, BigDecimal targetCalories,
                         BigDecimal targetProteinGrams, BigDecimal targetCarbsGrams,
                         BigDecimal targetFatGrams, GoalType goalType, boolean active) {
        this.userId = userId;
        this.effectiveDate = effectiveDate;
        this.targetCalories = targetCalories;
        this.targetProteinGrams = targetProteinGrams;
        this.targetCarbsGrams = targetCarbsGrams;
        this.targetFatGrams = targetFatGrams;
        this.goalType = goalType;
        this.active = active;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public BigDecimal getTargetCalories() { return targetCalories; }
    public void setTargetCalories(BigDecimal targetCalories) { this.targetCalories = targetCalories; }
    public BigDecimal getTargetProteinGrams() { return targetProteinGrams; }
    public void setTargetProteinGrams(BigDecimal targetProteinGrams) { this.targetProteinGrams = targetProteinGrams; }
    public BigDecimal getTargetCarbsGrams() { return targetCarbsGrams; }
    public void setTargetCarbsGrams(BigDecimal targetCarbsGrams) { this.targetCarbsGrams = targetCarbsGrams; }
    public BigDecimal getTargetFatGrams() { return targetFatGrams; }
    public void setTargetFatGrams(BigDecimal targetFatGrams) { this.targetFatGrams = targetFatGrams; }
    public GoalType getGoalType() { return goalType; }
    public void setGoalType(GoalType goalType) { this.goalType = goalType; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Instant getCreatedAt() { return createdAt; }
}
