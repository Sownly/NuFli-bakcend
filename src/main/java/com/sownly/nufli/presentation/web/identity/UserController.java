package com.sownly.nufli.presentation.web.identity;

import com.sownly.nufli.application.identity.UserService;
import com.sownly.nufli.infrastructure.security.CurrentUser;
import com.sownly.nufli.infrastructure.security.UserPrincipal;
import com.sownly.nufli.presentation.web.identity.dto.UserProfileRequest;
import com.sownly.nufli.presentation.web.identity.dto.UserProfileResponse;
import com.sownly.nufli.presentation.web.identity.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Endpoints for user profile and account details")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user profile")
    public ResponseEntity<UserResponse> getCurrentUser(@CurrentUser UserPrincipal principal) {
        UserResponse response = userService.getUser(principal.getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me/profile")
    @Operation(summary = "Update physical profile and demographic attributes")
    public ResponseEntity<UserProfileResponse> updateProfile(
        @CurrentUser UserPrincipal principal,
        @Valid @RequestBody UserProfileRequest request
    ) {
        UserProfileResponse response = userService.updateProfile(principal.getId(), request);
        return ResponseEntity.ok(response);
    }
}
