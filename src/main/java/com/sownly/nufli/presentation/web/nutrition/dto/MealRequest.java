package com.sownly.nufli.presentation.web.nutrition.dto;

import com.sownly.nufli.domain.nutrition.MealType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record MealRequest(
    @NotNull(message = "Meal type is required")
    MealType mealType,

    @NotNull(message = "Logged date is required")
    LocalDate loggedDate,

    Instant loggedAt,

    @NotEmpty(message = "Meal must contain at least one item")
    @Valid
    List<MealItemRequest> items
) {}
