package com.sownly.nufli.presentation.web.fitness;

import com.sownly.nufli.application.fitness.FitnessService;
import com.sownly.nufli.infrastructure.security.CurrentUser;
import com.sownly.nufli.infrastructure.security.UserPrincipal;
import com.sownly.nufli.presentation.web.fitness.dto.WorkoutRequest;
import com.sownly.nufli.presentation.web.fitness.dto.WorkoutResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fitness/workouts")
@Tag(name = "Fitness - Workouts", description = "Endpoints for logging, updating, and completing workout sessions")
@SecurityRequirement(name = "bearerAuth")
public class WorkoutController {

    private final FitnessService fitnessService;

    public WorkoutController(FitnessService fitnessService) {
        this.fitnessService = fitnessService;
    }

    @PostMapping
    @Operation(summary = "Start a new workout session (or log a finished one)")
    public ResponseEntity<WorkoutResponse> startWorkout(
        @CurrentUser UserPrincipal principal,
        @Valid @RequestBody WorkoutRequest request
    ) {
        WorkoutResponse response = fitnessService.startWorkout(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update workout session (add/edit sets, mark completed, etc.)")
    public ResponseEntity<WorkoutResponse> updateWorkout(
        @CurrentUser UserPrincipal principal,
        @PathVariable UUID id,
        @Valid @RequestBody WorkoutRequest request
    ) {
        WorkoutResponse response = fitnessService.updateWorkout(principal.getId(), id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get paginated workout history")
    public ResponseEntity<Page<WorkoutResponse>> getWorkouts(
        @CurrentUser UserPrincipal principal,
        @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<WorkoutResponse> response = fitnessService.getWorkouts(principal.getId(), pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get workout session detail with exercises and sets")
    public ResponseEntity<WorkoutResponse> getWorkout(
        @CurrentUser UserPrincipal principal,
        @PathVariable UUID id
    ) {
        WorkoutResponse response = fitnessService.getWorkout(principal.getId(), id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete workout session")
    public ResponseEntity<Void> deleteWorkout(
        @CurrentUser UserPrincipal principal,
        @PathVariable UUID id
    ) {
        fitnessService.deleteWorkout(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
