package com.sownly.nufli.domain.fitness;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "exercise_sets")
public class ExerciseSet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExercise workoutExercise;

    @Column(name = "set_number", nullable = false)
    private int setNumber = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_type", nullable = false)
    private SetType setType = SetType.NORMAL;

    @Column(name = "weight_kg", nullable = false, precision = 6, scale = 2)
    private BigDecimal weightKg = BigDecimal.ZERO;

    @Column(nullable = false)
    private int reps = 0;

    @Column(precision = 3, scale = 1)
    private BigDecimal rpe;

    @Column(name = "is_completed", nullable = false)
    private boolean completed = false;

    public ExerciseSet() {}

    public ExerciseSet(int setNumber, SetType setType, BigDecimal weightKg, int reps, BigDecimal rpe, boolean completed) {
        this.setNumber = setNumber;
        this.setType = setType != null ? setType : SetType.NORMAL;
        this.weightKg = weightKg != null ? weightKg : BigDecimal.ZERO;
        this.reps = reps;
        this.rpe = rpe;
        this.completed = completed;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public WorkoutExercise getWorkoutExercise() { return workoutExercise; }
    public void setWorkoutExercise(WorkoutExercise workoutExercise) { this.workoutExercise = workoutExercise; }
    public int getSetNumber() { return setNumber; }
    public void setSetNumber(int setNumber) { this.setNumber = setNumber; }
    public SetType getSetType() { return setType; }
    public void setSetType(SetType setType) { this.setType = setType; }
    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }
    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }
    public BigDecimal getRpe() { return rpe; }
    public void setRpe(BigDecimal rpe) { this.rpe = rpe; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
