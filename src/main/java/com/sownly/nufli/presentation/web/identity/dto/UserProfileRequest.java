package com.sownly.nufli.presentation.web.identity.dto;

import com.sownly.nufli.domain.identity.ActivityLevel;
import com.sownly.nufli.domain.identity.BiologicalSex;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserProfileRequest(
    @Size(max = 100)
    String firstName,

    @Size(max = 100)
    String lastName,

    LocalDate birthDate,

    @NotNull(message = "Biological sex is required")
    BiologicalSex biologicalSex,

    @DecimalMin(value = "50.0", message = "Height must be at least 50 cm")
    @DecimalMax(value = "300.0", message = "Height cannot exceed 300 cm")
    BigDecimal heightCm,

    @NotNull(message = "Activity level is required")
    ActivityLevel activityLevel
) {}
