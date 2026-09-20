package com.sownly.nufli.application.identity;

import com.sownly.nufli.common.exception.BadRequestException;
import com.sownly.nufli.common.exception.UnauthorizedException;
import com.sownly.nufli.domain.identity.*;
import com.sownly.nufli.infrastructure.security.JwtTokenProvider;
import com.sownly.nufli.presentation.web.identity.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final long refreshTokenExpirationMs;

    public AuthService(
        UserRepository userRepository,
        RefreshTokenRepository refreshTokenRepository,
        PasswordEncoder passwordEncoder,
        JwtTokenProvider tokenProvider,
        @Value("${jwt.refresh-token-expiration-ms:2592000000}") long refreshTokenExpirationMs
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BadRequestException("An account with this email already exists");
        }

        User user = new User(request.email().toLowerCase().trim(), passwordEncoder.encode(request.password()));
        UserProfile profile = new UserProfile(user);
        profile.setFirstName(request.firstName());
        profile.setLastName(request.lastName());
        user.setProfile(profile);

        User savedUser = userRepository.save(user);

        return generateTokens(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email().trim())
            .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UnauthorizedException("Account is not active");
        }

        return generateTokens(user);
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String hashedToken = hashToken(request.refreshToken());
        RefreshToken token = refreshTokenRepository.findByTokenHash(hashedToken)
            .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (token.isRevoked() || token.isExpired()) {
            refreshTokenRepository.revokeAllByUserId(token.getUserId());
            throw new UnauthorizedException("Refresh token is expired or revoked. Please log in again.");
        }

        // Token rotation: revoke current token
        token.setRevoked(true);
        refreshTokenRepository.save(token);

        User user = userRepository.findById(token.getUserId())
            .orElseThrow(() -> new UnauthorizedException("User not found"));

        return generateTokens(user);
    }

    public void logout(String rawRefreshToken) {
        if (rawRefreshToken != null && !rawRefreshToken.isBlank()) {
            String hashed = hashToken(rawRefreshToken);
            refreshTokenRepository.findByTokenHash(hashed).ifPresent(t -> {
                t.setRevoked(true);
                refreshTokenRepository.save(t);
            });
        }
    }

    private AuthResponse generateTokens(User user) {
        String accessToken = tokenProvider.generateAccessToken(user.getId(), user.getEmail());
        String rawRefreshToken = UUID.randomUUID().toString() + UUID.randomUUID().toString();
        String hashedToken = hashToken(rawRefreshToken);

        Instant expiresAt = Instant.now().plusMillis(refreshTokenExpirationMs);
        RefreshToken refreshToken = new RefreshToken(user.getId(), hashedToken, expiresAt);
        refreshTokenRepository.save(refreshToken);

        long expiresInSeconds = 900; // 15 mins
        return AuthResponse.of(accessToken, rawRefreshToken, expiresInSeconds, UserResponse.from(user));
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
