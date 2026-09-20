package com.sownly.nufli.presentation.web.identity.dto;

import com.sownly.nufli.domain.identity.ActivityLevel;
import com.sownly.nufli.domain.identity.BiologicalSex;
import com.sownly.nufli.domain.identity.UserProfile;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserProfileResponse(
    String firstName,
    String lastName,
    LocalDate birthDate,
    BiologicalSex biologicalSex,
    BigDecimal heightCm,
    ActivityLevel activityLevel
) {
    public static UserProfileResponse from(UserProfile profile) {
        if (profile == null) return null;
        return new UserProfileResponse(
            profile.getFirstName(),
            profile.getLastName(),
            profile.getBirthDate(),
            profile.getBiologicalSex(),
            profile.getHeightCm(),
            profile.getActivityLevel()
        );
    }
}
