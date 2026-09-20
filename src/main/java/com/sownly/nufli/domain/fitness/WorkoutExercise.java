package com.sownly.nufli.domain.fitness;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workout_exercises")
public class WorkoutExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "order_index", nullable = false)
    private int orderIndex = 0;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "workoutExercise", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @org.hibernate.annotations.BatchSize(size = 30)
    private List<ExerciseSet> sets = new ArrayList<>();

    public WorkoutExercise() {}

    public WorkoutExercise(Exercise exercise, int orderIndex, String notes) {
        this.exercise = exercise;
        this.orderIndex = orderIndex;
        this.notes = notes;
    }

    public void addSet(ExerciseSet set) {
        sets.add(set);
        set.setWorkoutExercise(this);
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Workout getWorkout() { return workout; }
    public void setWorkout(Workout workout) { this.workout = workout; }
    public Exercise getExercise() { return exercise; }
    public void setExercise(Exercise exercise) { this.exercise = exercise; }
    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<ExerciseSet> getSets() { return sets; }
    public void setSets(List<ExerciseSet> sets) { this.sets = sets; }
}
