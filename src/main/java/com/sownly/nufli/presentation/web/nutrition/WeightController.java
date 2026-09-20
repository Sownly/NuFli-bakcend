package com.sownly.nufli.presentation.web.nutrition;

import com.sownly.nufli.application.nutrition.NutritionService;
import com.sownly.nufli.infrastructure.security.CurrentUser;
import com.sownly.nufli.infrastructure.security.UserPrincipal;
import com.sownly.nufli.presentation.web.nutrition.dto.WeightEntryRequest;
import com.sownly.nufli.presentation.web.nutrition.dto.WeightEntryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/nutrition/weight")
@Tag(name = "Nutrition - Weight", description = "Endpoints for daily weight tracking and history")
@SecurityRequirement(name = "bearerAuth")
public class WeightController {

    private final NutritionService nutritionService;

    public WeightController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @PostMapping
    @Operation(summary = "Log or update daily weight")
    public ResponseEntity<WeightEntryResponse> logWeight(
        @CurrentUser UserPrincipal principal,
        @Valid @RequestBody WeightEntryRequest request
    ) {
        WeightEntryResponse response = nutritionService.logWeight(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get historical weight logs")
    public ResponseEntity<List<WeightEntryResponse>> getWeightHistory(
        @CurrentUser UserPrincipal principal,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<WeightEntryResponse> response = nutritionService.getWeightHistory(principal.getId(), startDate, endDate);
        return ResponseEntity.ok(response);
    }
}
