package com.sownly.nufli.presentation.web.nutrition;

import com.sownly.nufli.application.nutrition.NutritionService;
import com.sownly.nufli.infrastructure.security.CurrentUser;
import com.sownly.nufli.infrastructure.security.UserPrincipal;
import com.sownly.nufli.presentation.web.nutrition.dto.NutritionGoalRequest;
import com.sownly.nufli.presentation.web.nutrition.dto.NutritionGoalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/nutrition/goals")
@Tag(name = "Nutrition - Goals", description = "Endpoints for setting and viewing nutrition calorie and macro goals")
@SecurityRequirement(name = "bearerAuth")
public class NutritionGoalController {

    private final NutritionService nutritionService;

    public NutritionGoalController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @PostMapping
    @Operation(summary = "Set new nutrition goal (auto-calculates targets from profile if null)")
    public ResponseEntity<NutritionGoalResponse> setGoal(
        @CurrentUser UserPrincipal principal,
        @Valid @RequestBody NutritionGoalRequest request
    ) {
        NutritionGoalResponse response = nutritionService.setGoal(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/active")
    @Operation(summary = "Get currently active nutrition goal")
    public ResponseEntity<NutritionGoalResponse> getActiveGoal(@CurrentUser UserPrincipal principal) {
        NutritionGoalResponse response = nutritionService.getActiveGoal(principal.getId());
        return ResponseEntity.ok(response);
    }
}
