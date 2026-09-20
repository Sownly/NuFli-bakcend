package com.sownly.nufli.presentation.web.fitness;

import com.sownly.nufli.application.fitness.FitnessService;
import com.sownly.nufli.infrastructure.security.CurrentUser;
import com.sownly.nufli.infrastructure.security.UserPrincipal;
import com.sownly.nufli.presentation.web.fitness.dto.ActivityRequest;
import com.sownly.nufli.presentation.web.fitness.dto.ActivityResponse;
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
@RequestMapping("/api/v1/fitness/activities")
@Tag(name = "Fitness - Activities", description = "Endpoints for cardio and endurance activities")
@SecurityRequirement(name = "bearerAuth")
public class ActivityController {

    private final FitnessService fitnessService;

    public ActivityController(FitnessService fitnessService) {
        this.fitnessService = fitnessService;
    }

    @PostMapping
    @Operation(summary = "Log a cardio or endurance activity")
    public ResponseEntity<ActivityResponse> logActivity(
        @CurrentUser UserPrincipal principal,
        @Valid @RequestBody ActivityRequest request
    ) {
        ActivityResponse response = fitnessService.logActivity(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get paginated activity history")
    public ResponseEntity<Page<ActivityResponse>> getActivities(
        @CurrentUser UserPrincipal principal,
        @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<ActivityResponse> response = fitnessService.getActivities(principal.getId(), pageable);
        return ResponseEntity.ok(response);
    }
}
