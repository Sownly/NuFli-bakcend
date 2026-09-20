package com.sownly.nufli.presentation.web.summary;

import com.sownly.nufli.application.summary.SummaryService;
import com.sownly.nufli.infrastructure.security.CurrentUser;
import com.sownly.nufli.infrastructure.security.UserPrincipal;
import com.sownly.nufli.presentation.web.summary.dto.DailyEnergyBalanceSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/summary")
@Tag(name = "Ecosystem Summary", description = "Cross-domain daily energy balance and combined overview")
@SecurityRequirement(name = "bearerAuth")
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping("/daily")
    @Operation(summary = "Get cross-domain daily energy balance summary (food intake vs exercise burned)")
    public ResponseEntity<DailyEnergyBalanceSummaryResponse> getDailySummary(
        @CurrentUser UserPrincipal principal,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LocalDate queryDate = date != null ? date : LocalDate.now();
        DailyEnergyBalanceSummaryResponse response = summaryService.getDailySummary(principal.getId(), queryDate);
        return ResponseEntity.ok(response);
    }
}
