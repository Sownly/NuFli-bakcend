package com.sownly.nufli.domain.fitness;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "routine_exercises")
public class RoutineExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "order_index", nullable = false)
    private int orderIndex = 0;

    @Column(name = "target_sets", nullable = false)
    private int targetSets = 3;

    @Column(name = "target_reps", nullable = false)
    private int targetReps = 10;

    public RoutineExercise() {}

    public RoutineExercise(Exercise exercise, int orderIndex, int targetSets, int targetReps) {
        this.exercise = exercise;
        this.orderIndex = orderIndex;
        this.targetSets = targetSets;
        this.targetReps = targetReps;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Routine getRoutine() { return routine; }
    public void setRoutine(Routine routine) { this.routine = routine; }
    public Exercise getExercise() { return exercise; }
    public void setExercise(Exercise exercise) { this.exercise = exercise; }
    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }
    public int getTargetSets() { return targetSets; }
    public void setTargetSets(int targetSets) { this.targetSets = targetSets; }
    public int getTargetReps() { return targetReps; }
    public void setTargetReps(int targetReps) { this.targetReps = targetReps; }
}
