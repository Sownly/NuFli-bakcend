package com.sownly.nufli.presentation.web.fitness;

import com.sownly.nufli.application.fitness.FitnessService;
import com.sownly.nufli.infrastructure.security.CurrentUser;
import com.sownly.nufli.infrastructure.security.UserPrincipal;
import com.sownly.nufli.presentation.web.fitness.dto.DailyFitnessSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/fitness/summary")
@Tag(name = "Fitness - Summary", description = "Endpoints for daily fitness activity and volume summary")
@SecurityRequirement(name = "bearerAuth")
public class FitnessSummaryController {

    private final FitnessService fitnessService;

    public FitnessSummaryController(FitnessService fitnessService) {
        this.fitnessService = fitnessService;
    }

    @GetMapping
    @Operation(summary = "Get daily fitness summary (volume, workouts, cardio duration, estimated calories)")
    public ResponseEntity<DailyFitnessSummaryResponse> getSummary(
        @CurrentUser UserPrincipal principal,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LocalDate queryDate = date != null ? date : LocalDate.now();
        DailyFitnessSummaryResponse response = fitnessService.getDailyFitnessSummary(principal.getId(), queryDate);
        return ResponseEntity.ok(response);
    }
}
