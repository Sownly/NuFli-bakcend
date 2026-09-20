package com.sownly.nufli.application.identity;

import com.sownly.nufli.common.exception.BadRequestException;
import com.sownly.nufli.domain.identity.RefreshTokenRepository;
import com.sownly.nufli.domain.identity.User;
import com.sownly.nufli.domain.identity.UserRepository;
import com.sownly.nufli.infrastructure.security.JwtTokenProvider;
import com.sownly.nufli.presentation.web.identity.dto.AuthResponse;
import com.sownly.nufli.presentation.web.identity.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, refreshTokenRepository, passwordEncoder, tokenProvider, 2592000000L);
    }

    @Test
    @DisplayName("Register new user successfully hashes password and returns tokens")
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest("test@example.com", "Password123!", "John", "Doe");

        when(userRepository.existsByEmailIgnoreCase(anyString())).thenReturn(false);
        when(passwordEncoder.encode("Password123!")).thenReturn("hashed_secret");

        UUID generatedId = UUID.randomUUID();
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(generatedId);
            return u;
        });

        when(tokenProvider.generateAccessToken(eq(generatedId), eq("test@example.com"))).thenReturn("mocked.jwt.token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("mocked.jwt.token", response.accessToken());
        assertNotNull(response.refreshToken());
        assertEquals("test@example.com", response.user().email());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("hashed_secret", userCaptor.getValue().getPasswordHash());
        assertEquals("test@example.com", userCaptor.getValue().getEmail());
    }

    @Test
    @DisplayName("Register duplicate email throws BadRequestException")
    void testRegisterDuplicateEmailThrows() {
        RegisterRequest request = new RegisterRequest("existing@example.com", "Password123!", "Jane", "Doe");
        when(userRepository.existsByEmailIgnoreCase("existing@example.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }
}
