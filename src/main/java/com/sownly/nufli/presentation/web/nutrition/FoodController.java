package com.sownly.nufli.presentation.web.nutrition;

import com.sownly.nufli.application.nutrition.NutritionService;
import com.sownly.nufli.infrastructure.security.CurrentUser;
import com.sownly.nufli.infrastructure.security.UserPrincipal;
import com.sownly.nufli.presentation.web.nutrition.dto.FoodRequest;
import com.sownly.nufli.presentation.web.nutrition.dto.FoodResponse;
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
@RequestMapping("/api/v1/nutrition/foods")
@Tag(name = "Nutrition - Foods", description = "Endpoints for searching and managing food library")
@SecurityRequirement(name = "bearerAuth")
public class FoodController {

    private final NutritionService nutritionService;

    public FoodController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @PostMapping
    @Operation(summary = "Create custom user food")
    public ResponseEntity<FoodResponse> createFood(
        @CurrentUser UserPrincipal principal,
        @Valid @RequestBody FoodRequest request
    ) {
        FoodResponse response = nutritionService.createFood(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Search food library (verified foods + user custom foods)")
    public ResponseEntity<Page<FoodResponse>> searchFoods(
        @CurrentUser UserPrincipal principal,
        @RequestParam(required = false) String query,
        @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<FoodResponse> response = nutritionService.searchFoods(principal.getId(), query, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get food details by ID")
    public ResponseEntity<FoodResponse> getFood(
        @CurrentUser UserPrincipal principal,
        @PathVariable UUID id
    ) {
        FoodResponse response = nutritionService.getFood(id, principal.getId());
        return ResponseEntity.ok(response);
    }
}
