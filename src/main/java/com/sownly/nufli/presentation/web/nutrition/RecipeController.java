package com.sownly.nufli.presentation.web.nutrition;

import com.sownly.nufli.application.nutrition.NutritionService;
import com.sownly.nufli.infrastructure.security.CurrentUser;
import com.sownly.nufli.infrastructure.security.UserPrincipal;
import com.sownly.nufli.presentation.web.nutrition.dto.RecipeRequest;
import com.sownly.nufli.presentation.web.nutrition.dto.RecipeResponse;
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

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/nutrition/recipes")
@Tag(name = "Nutrition - Recipes", description = "Endpoints for custom recipes and ingredient calculation")
@SecurityRequirement(name = "bearerAuth")
public class RecipeController {

    private final NutritionService nutritionService;

    public RecipeController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @PostMapping
    @Operation(summary = "Create custom recipe with ingredients and automatic macro rollup")
    public ResponseEntity<RecipeResponse> createRecipe(
        @CurrentUser UserPrincipal principal,
        @Valid @RequestBody RecipeRequest request
    ) {
        RecipeResponse response = nutritionService.createRecipe(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Browse and filter recipes (by max calories, min protein, etc.)")
    public ResponseEntity<Page<RecipeResponse>> getRecipes(
        @CurrentUser UserPrincipal principal,
        @RequestParam(required = false) BigDecimal maxCalories,
        @RequestParam(required = false) BigDecimal minProtein,
        @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<RecipeResponse> response = nutritionService.getRecipes(principal.getId(), maxCalories, minProtein, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get recipe detail with ingredients and per-serving macros")
    public ResponseEntity<RecipeResponse> getRecipe(
        @CurrentUser UserPrincipal principal,
        @PathVariable UUID id
    ) {
        RecipeResponse response = nutritionService.getRecipe(principal.getId(), id);
        return ResponseEntity.ok(response);
    }
}
