package com.sownly.nufli.presentation.web.nutrition.dto;

import com.sownly.nufli.domain.nutrition.WeightEntry;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record WeightEntryResponse(
    UUID id,
    UUID userId,
    LocalDate recordedDate,
    BigDecimal weightKg,
    String notes,
    Instant recordedAt
) {
    public static WeightEntryResponse from(WeightEntry entry) {
        return new WeightEntryResponse(
            entry.getId(),
            entry.getUserId(),
            entry.getRecordedDate(),
            entry.getWeightKg(),
            entry.getNotes(),
            entry.getRecordedAt()
        );
    }
}
