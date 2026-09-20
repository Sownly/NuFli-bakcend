package com.sownly.nufli.presentation.web.identity.dto;

import com.sownly.nufli.domain.identity.User;
import com.sownly.nufli.domain.identity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String email,
    UserStatus status,
    UserProfileResponse profile,
    Instant createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getStatus(),
            UserProfileResponse.from(user.getProfile()),
            user.getCreatedAt()
        );
    }
}
