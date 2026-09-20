package com.sownly.nufli.domain.fitness;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId; // Nullable if system verified exercise

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_muscle_group", nullable = false)
    private MuscleGroup primaryMuscleGroup;

    @Column(name = "secondary_muscle_groups")
    private String secondaryMuscleGroups;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExerciseCategory category = ExerciseCategory.BARBELL;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "is_verified", nullable = false)
    private boolean verified = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Exercise() {}

    public Exercise(UUID userId, String name, MuscleGroup primaryMuscleGroup,
                    String secondaryMuscleGroups, ExerciseCategory category,
                    String instructions, boolean verified) {
        this.userId = userId;
        this.name = name;
        this.primaryMuscleGroup = primaryMuscleGroup;
        this.secondaryMuscleGroups = secondaryMuscleGroups;
        this.category = category != null ? category : ExerciseCategory.BARBELL;
        this.instructions = instructions;
        this.verified = verified;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public MuscleGroup getPrimaryMuscleGroup() { return primaryMuscleGroup; }
    public void setPrimaryMuscleGroup(MuscleGroup primaryMuscleGroup) { this.primaryMuscleGroup = primaryMuscleGroup; }
    public String getSecondaryMuscleGroups() { return secondaryMuscleGroups; }
    public void setSecondaryMuscleGroups(String secondaryMuscleGroups) { this.secondaryMuscleGroups = secondaryMuscleGroups; }
    public ExerciseCategory getCategory() { return category; }
    public void setCategory(ExerciseCategory category) { this.category = category; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public Instant getCreatedAt() { return createdAt; }
}
