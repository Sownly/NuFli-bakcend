package com.sownly.nufli.presentation.web.nutrition;

import com.sownly.nufli.application.nutrition.NutritionService;
import com.sownly.nufli.infrastructure.security.CurrentUser;
import com.sownly.nufli.infrastructure.security.UserPrincipal;
import com.sownly.nufli.presentation.web.nutrition.dto.MealRequest;
import com.sownly.nufli.presentation.web.nutrition.dto.MealResponse;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/nutrition/meals")
@Tag(name = "Nutrition - Meals", description = "Endpoints for logging and viewing meals")
@SecurityRequirement(name = "bearerAuth")
public class MealController {

    private final NutritionService nutritionService;

    public MealController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @PostMapping
    @Operation(summary = "Log a meal with food items and quantities")
    public ResponseEntity<MealResponse> logMeal(
        @CurrentUser UserPrincipal principal,
        @Valid @RequestBody MealRequest request
    ) {
        MealResponse response = nutritionService.logMeal(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all meals logged on a specific date")
    public ResponseEntity<List<MealResponse>> getMeals(
        @CurrentUser UserPrincipal principal,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LocalDate queryDate = date != null ? date : LocalDate.now();
        List<MealResponse> response = nutritionService.getMealsForDate(principal.getId(), queryDate);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a logged meal")
    public ResponseEntity<Void> deleteMeal(
        @CurrentUser UserPrincipal principal,
        @PathVariable UUID id
    ) {
        nutritionService.deleteMeal(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
