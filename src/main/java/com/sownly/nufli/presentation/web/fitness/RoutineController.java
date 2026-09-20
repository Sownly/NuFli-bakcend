package com.sownly.nufli.presentation.web.fitness;

import com.sownly.nufli.application.fitness.FitnessService;
import com.sownly.nufli.infrastructure.security.CurrentUser;
import com.sownly.nufli.infrastructure.security.UserPrincipal;
import com.sownly.nufli.presentation.web.fitness.dto.RoutineRequest;
import com.sownly.nufli.presentation.web.fitness.dto.RoutineResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fitness/routines")
@Tag(name = "Fitness - Routines", description = "Endpoints for creating and managing workout routine templates")
@SecurityRequirement(name = "bearerAuth")
public class RoutineController {

    private final FitnessService fitnessService;

    public RoutineController(FitnessService fitnessService) {
        this.fitnessService = fitnessService;
    }

    @PostMapping
    @Operation(summary = "Create workout routine template")
    public ResponseEntity<RoutineResponse> createRoutine(
        @CurrentUser UserPrincipal principal,
        @Valid @RequestBody RoutineRequest request
    ) {
        RoutineResponse response = fitnessService.createRoutine(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "List all workout routine templates for user")
    public ResponseEntity<List<RoutineResponse>> getRoutines(@CurrentUser UserPrincipal principal) {
        List<RoutineResponse> response = fitnessService.getUserRoutines(principal.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get routine template by ID")
    public ResponseEntity<RoutineResponse> getRoutine(
        @CurrentUser UserPrincipal principal,
        @PathVariable UUID id
    ) {
        RoutineResponse response = fitnessService.getRoutine(principal.getId(), id);
        return ResponseEntity.ok(response);
    }
}
