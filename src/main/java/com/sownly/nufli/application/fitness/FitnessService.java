package com.sownly.nufli.application.fitness;

import com.sownly.nufli.common.exception.BadRequestException;
import com.sownly.nufli.common.exception.ResourceNotFoundException;
import com.sownly.nufli.domain.fitness.*;
import com.sownly.nufli.domain.nutrition.WeightEntry;
import com.sownly.nufli.domain.nutrition.WeightEntryRepository;
import com.sownly.nufli.presentation.web.fitness.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FitnessService {

    private final ExerciseRepository exerciseRepository;
    private final RoutineRepository routineRepository;
    private final WorkoutRepository workoutRepository;
    private final ActivityRepository activityRepository;
    private final WeightEntryRepository weightEntryRepository;

    public FitnessService(
        ExerciseRepository exerciseRepository,
        RoutineRepository routineRepository,
        WorkoutRepository workoutRepository,
        ActivityRepository activityRepository,
        WeightEntryRepository weightEntryRepository
    ) {
        this.exerciseRepository = exerciseRepository;
        this.routineRepository = routineRepository;
        this.workoutRepository = workoutRepository;
        this.activityRepository = activityRepository;
        this.weightEntryRepository = weightEntryRepository;
    }

    // ---------------- Exercises ----------------
    public ExerciseResponse createExercise(UUID userId, ExerciseRequest request) {
        Exercise exercise = new Exercise(
            userId,
            request.name().trim(),
            request.primaryMuscleGroup(),
            request.secondaryMuscleGroups(),
            request.category(),
            request.instructions(),
            false
        );
        return ExerciseResponse.from(exerciseRepository.save(exercise));
    }

    @Transactional(readOnly = true)
    public Page<ExerciseResponse> searchExercises(
        UUID userId,
        MuscleGroup muscleGroup,
        ExerciseCategory category,
        String query,
        Pageable pageable
    ) {
        String cleanQuery = (query != null && !query.isBlank()) ? query.trim() : null;
        return exerciseRepository.searchExercises(userId, muscleGroup, category, cleanQuery, pageable)
            .map(ExerciseResponse::from);
    }

    // ---------------- Routines ----------------
    public RoutineResponse createRoutine(UUID userId, RoutineRequest request) {
        Routine routine = new Routine(userId, request.name().trim(), request.description());

        for (RoutineExerciseRequest itemReq : request.exercises()) {
            Exercise exercise = exerciseRepository.findByIdAndUserIdOrVerifiedTrue(itemReq.exerciseId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found: " + itemReq.exerciseId()));

            RoutineExercise re = new RoutineExercise(
                exercise, itemReq.orderIndex(), itemReq.targetSets(), itemReq.targetReps()
            );
            routine.addExercise(re);
        }

        return RoutineResponse.from(routineRepository.save(routine));
    }

    @Transactional(readOnly = true)
    public List<RoutineResponse> getUserRoutines(UUID userId) {
        return routineRepository.findByUserIdWithExercises(userId).stream()
            .map(RoutineResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public RoutineResponse getRoutine(UUID userId, UUID routineId) {
        Routine routine = routineRepository.findByIdAndUserIdWithExercises(routineId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Routine not found: " + routineId));
        return RoutineResponse.from(routine);
    }

    // ---------------- Workouts ----------------
    public WorkoutResponse startWorkout(UUID userId, WorkoutRequest request) {
        Instant start = request.startedAt() != null ? request.startedAt() : Instant.now();
        Workout workout = new Workout(userId, request.routineId(), request.name().trim(), start);
        workout.setNotes(request.notes());
        if (request.status() != null) workout.setStatus(request.status());
        if (request.completedAt() != null) workout.setCompletedAt(request.completedAt());

        if (request.exercises() != null) {
            for (WorkoutExerciseRequest exReq : request.exercises()) {
                Exercise exercise = exerciseRepository.findByIdAndUserIdOrVerifiedTrue(exReq.exerciseId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise not found: " + exReq.exerciseId()));

                WorkoutExercise we = new WorkoutExercise(exercise, exReq.orderIndex(), exReq.notes());
                if (exReq.sets() != null) {
                    for (ExerciseSetRequest setReq : exReq.sets()) {
                        ExerciseSet set = new ExerciseSet(
                            setReq.setNumber(),
                            setReq.setType(),
                            setReq.weightKg(),
                            setReq.reps(),
                            setReq.rpe(),
                            setReq.isCompleted()
                        );
                        we.addSet(set);
                    }
                }
                workout.addExercise(we);
            }
        }

        return WorkoutResponse.from(workoutRepository.save(workout));
    }

    public WorkoutResponse updateWorkout(UUID userId, UUID workoutId, WorkoutRequest request) {
        Workout workout = workoutRepository.findByIdAndUserIdWithDetails(workoutId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Workout not found: " + workoutId));

        workout.setName(request.name().trim());
        workout.setNotes(request.notes());
        if (request.status() != null) workout.setStatus(request.status());
        if (request.completedAt() != null) workout.setCompletedAt(request.completedAt());
        if (workout.getStatus() == WorkoutStatus.COMPLETED && workout.getCompletedAt() == null) {
            workout.setCompletedAt(Instant.now());
        }

        // Replace exercises and sets
        workout.getExercises().clear();
        if (request.exercises() != null) {
            for (WorkoutExerciseRequest exReq : request.exercises()) {
                Exercise exercise = exerciseRepository.findByIdAndUserIdOrVerifiedTrue(exReq.exerciseId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise not found: " + exReq.exerciseId()));

                WorkoutExercise we = new WorkoutExercise(exercise, exReq.orderIndex(), exReq.notes());
                if (exReq.sets() != null) {
                    for (ExerciseSetRequest setReq : exReq.sets()) {
                        ExerciseSet set = new ExerciseSet(
                            setReq.setNumber(),
                            setReq.setType(),
                            setReq.weightKg(),
                            setReq.reps(),
                            setReq.rpe(),
                            setReq.isCompleted()
                        );
                        we.addSet(set);
                    }
                }
                workout.addExercise(we);
            }
        }

        return WorkoutResponse.from(workoutRepository.save(workout));
    }

    @Transactional(readOnly = true)
    public Page<WorkoutResponse> getWorkouts(UUID userId, Pageable pageable) {
        return workoutRepository.findByUserIdOrderByStartedAtDesc(userId, pageable)
            .map(WorkoutResponse::from);
    }

    @Transactional(readOnly = true)
    public WorkoutResponse getWorkout(UUID userId, UUID workoutId) {
        Workout workout = workoutRepository.findByIdAndUserIdWithDetails(workoutId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Workout not found: " + workoutId));
        return WorkoutResponse.from(workout);
    }

    public void deleteWorkout(UUID userId, UUID workoutId) {
        Workout workout = workoutRepository.findById(workoutId)
            .orElseThrow(() -> new ResourceNotFoundException("Workout not found: " + workoutId));
        if (!workout.getUserId().equals(userId)) {
            throw new BadRequestException("You do not own this workout");
        }
        workoutRepository.delete(workout);
    }

    // ---------------- Activities ----------------
    public ActivityResponse logActivity(UUID userId, ActivityRequest request) {
        BigDecimal burned = request.estimatedCaloriesBurned();
        if (burned == null || burned.compareTo(BigDecimal.ZERO) <= 0) {
            BigDecimal weight = weightEntryRepository.findTopByUserIdOrderByRecordedDateDesc(userId)
                .map(WeightEntry::getWeightKg)
                .orElse(BigDecimal.valueOf(70.0));
            burned = FitnessCalculator.estimateCardioCaloriesBurned(request.activityType(), request.durationMinutes(), weight);
        }

        Instant start = request.startedAt() != null ? request.startedAt() : Instant.now();
        Activity activity = new Activity(
            userId,
            request.activityType(),
            start,
            request.durationMinutes(),
            request.distanceKm(),
            burned,
            request.notes()
        );

        return ActivityResponse.from(activityRepository.save(activity));
    }

    @Transactional(readOnly = true)
    public Page<ActivityResponse> getActivities(UUID userId, Pageable pageable) {
        return activityRepository.findByUserIdOrderByStartedAtDesc(userId, pageable)
            .map(ActivityResponse::from);
    }

    // ---------------- Daily Fitness Summary ----------------
    @Transactional(readOnly = true)
    public DailyFitnessSummaryResponse getDailyFitnessSummary(UUID userId, LocalDate date) {
        ZoneId zone = ZoneId.systemDefault();
        Instant dayStart = date.atStartOfDay(zone).toInstant();
        Instant dayEnd = date.plusDays(1).atStartOfDay(zone).toInstant();

        List<Workout> workouts = workoutRepository.findByUserIdAndStartedAtBetweenWithDetails(userId, dayStart, dayEnd);
        List<Activity> activities = activityRepository.findByUserIdAndStartedAtBetween(userId, dayStart, dayEnd);

        int completedWorkouts = 0;
        long totalWorkoutMins = 0;
        BigDecimal totalVolume = BigDecimal.ZERO;

        for (Workout w : workouts) {
            if (w.getStatus() == WorkoutStatus.COMPLETED) {
                completedWorkouts++;
                if (w.getStartedAt() != null && w.getCompletedAt() != null) {
                    totalWorkoutMins += Duration.between(w.getStartedAt(), w.getCompletedAt()).toMinutes();
                }
                totalVolume = totalVolume.add(w.calculateTotalVolume());
            }
        }

        int totalCardioMins = 0;
        BigDecimal totalBurned = BigDecimal.ZERO;

        for (Activity a : activities) {
            totalCardioMins += a.getDurationMinutes();
            totalBurned = totalBurned.add(a.getEstimatedCaloriesBurned());
        }

        List<WorkoutResponse> workoutResponses = workouts.stream().map(WorkoutResponse::from).toList();
        List<ActivityResponse> activityResponses = activities.stream().map(ActivityResponse::from).toList();

        return new DailyFitnessSummaryResponse(
            date,
            completedWorkouts,
            totalWorkoutMins,
            totalVolume.setScale(2, RoundingMode.HALF_UP),
            activities.size(),
            totalCardioMins,
            totalBurned.setScale(2, RoundingMode.HALF_UP),
            workoutResponses,
            activityResponses
        );
    }
}
