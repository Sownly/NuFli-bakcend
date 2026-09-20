package com.sownly.nufli.presentation.web.nutrition;

import com.sownly.nufli.application.nutrition.NutritionService;
import com.sownly.nufli.infrastructure.security.CurrentUser;
import com.sownly.nufli.infrastructure.security.UserPrincipal;
import com.sownly.nufli.presentation.web.nutrition.dto.DailyNutritionSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/nutrition/summary")
@Tag(name = "Nutrition - Summary", description = "Endpoints for daily nutritional aggregation and progress")
@SecurityRequirement(name = "bearerAuth")
public class NutritionSummaryController {

    private final NutritionService nutritionService;

    public NutritionSummaryController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @GetMapping
    @Operation(summary = "Get daily nutrition summary (consumed vs target calories and macros)")
    public ResponseEntity<DailyNutritionSummaryResponse> getSummary(
        @CurrentUser UserPrincipal principal,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LocalDate queryDate = date != null ? date : LocalDate.now();
        DailyNutritionSummaryResponse response = nutritionService.getDailySummary(principal.getId(), queryDate);
        return ResponseEntity.ok(response);
    }
}
