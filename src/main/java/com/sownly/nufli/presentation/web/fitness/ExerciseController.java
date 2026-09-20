package com.sownly.nufli.presentation.web.fitness;

import com.sownly.nufli.application.fitness.FitnessService;
import com.sownly.nufli.domain.fitness.ExerciseCategory;
import com.sownly.nufli.domain.fitness.MuscleGroup;
import com.sownly.nufli.infrastructure.security.CurrentUser;
import com.sownly.nufli.infrastructure.security.UserPrincipal;
import com.sownly.nufli.presentation.web.fitness.dto.ExerciseRequest;
import com.sownly.nufli.presentation.web.fitness.dto.ExerciseResponse;
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

@RestController
@RequestMapping("/api/v1/fitness/exercises")
@Tag(name = "Fitness - Exercises", description = "Endpoints for searching and managing exercise library")
@SecurityRequirement(name = "bearerAuth")
public class ExerciseController {

    private final FitnessService fitnessService;

    public ExerciseController(FitnessService fitnessService) {
        this.fitnessService = fitnessService;
    }

    @PostMapping
    @Operation(summary = "Create custom exercise")
    public ResponseEntity<ExerciseResponse> createExercise(
        @CurrentUser UserPrincipal principal,
        @Valid @RequestBody ExerciseRequest request
    ) {
        ExerciseResponse response = fitnessService.createExercise(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Search exercise library (verified exercises + custom exercises)")
    public ResponseEntity<Page<ExerciseResponse>> searchExercises(
        @CurrentUser UserPrincipal principal,
        @RequestParam(required = false) MuscleGroup muscleGroup,
        @RequestParam(required = false) ExerciseCategory category,
        @RequestParam(required = false) String query,
        @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<ExerciseResponse> response = fitnessService.searchExercises(principal.getId(), muscleGroup, category, query, pageable);
        return ResponseEntity.ok(response);
    }
}
