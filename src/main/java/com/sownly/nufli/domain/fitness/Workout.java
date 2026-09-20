package com.sownly.nufli.domain.fitness;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workouts")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "routine_id")
    private UUID routineId;

    @Column(nullable = false)
    private String name;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt = Instant.now();

    @Column(name = "completed_at")
    private Instant completedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkoutStatus status = WorkoutStatus.IN_PROGRESS;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @org.hibernate.annotations.BatchSize(size = 30)
    private List<WorkoutExercise> exercises = new ArrayList<>();

    public Workout() {}

    public Workout(UUID userId, UUID routineId, String name, Instant startedAt) {
        this.userId = userId;
        this.routineId = routineId;
        this.name = name;
        this.startedAt = startedAt != null ? startedAt : Instant.now();
        this.status = WorkoutStatus.IN_PROGRESS;
        this.createdAt = Instant.now();
    }

    public void addExercise(WorkoutExercise exercise) {
        exercises.add(exercise);
        exercise.setWorkout(this);
    }

    public BigDecimal calculateTotalVolume() {
        return exercises.stream()
            .flatMap(e -> e.getSets().stream())
            .filter(ExerciseSet::isCompleted)
            .map(s -> s.getWeightKg().multiply(BigDecimal.valueOf(s.getReps())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public UUID getRoutineId() { return routineId; }
    public void setRoutineId(UUID routineId) { this.routineId = routineId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
    public WorkoutStatus getStatus() { return status; }
    public void setStatus(WorkoutStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Instant getCreatedAt() { return createdAt; }
    public List<WorkoutExercise> getExercises() { return exercises; }
    public void setExercises(List<WorkoutExercise> exercises) { this.exercises = exercises; }
}
