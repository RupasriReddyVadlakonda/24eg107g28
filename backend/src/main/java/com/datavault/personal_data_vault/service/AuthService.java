package com.datavault.personal_data_vault.service;

import com.datavault.personal_data_vault.dto.request.LoginRequest;
import com.datavault.personal_data_vault.dto.request.RegisterRequest;
import com.datavault.personal_data_vault.dto.response.AuthResponse;
import com.datavault.personal_data_vault.dto.response.UserResponse;
import com.datavault.personal_data_vault.entity.RefreshToken;
import com.datavault.personal_data_vault.entity.User;
import com.datavault.personal_data_vault.exception.DuplicateResourceException;
import com.datavault.personal_data_vault.exception.ResourceNotFoundException;
import com.datavault.personal_data_vault.exception.UnauthorizedException;
import com.datavault.personal_data_vault.repository.RefreshTokenRepository;
import com.datavault.personal_data_vault.repository.UserRepository;
import com.datavault.personal_data_vault.security.JwtService;
import com.datavault.personal_data_vault.security.UserPrincipal;
import com.datavault.personal_data_vault.service.TokenHashingService;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenHashingService tokenHashingService;
    private final SecureRandom secureRandom = new SecureRandom();
    @Value(value="${app.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (this.userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email is already registered");
        }
        User user = (User)this.userRepository.save(User.builder().fullName(request.getFullName().trim()).email(email).passwordHash(this.passwordEncoder.encode((CharSequence)request.getPassword())).phoneNumber(request.getPhoneNumber()).role(User.Role.USER).enabled(true).build());
        return this.issueTokens(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        Authentication authentication = this.authenticationManager.authenticate((Authentication)new UsernamePasswordAuthenticationToken((Object)email, (Object)request.getPassword()));
        UserPrincipal principal = (UserPrincipal)authentication.getPrincipal();
        User user = (User)this.userRepository.findById(principal.id()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return this.issueTokens(user);
    }

    @Transactional
    public AuthResponse refresh(String rawToken) {
        String tokenHash = this.tokenHashingService.sha256(rawToken);
        RefreshToken token = this.refreshTokenRepository.findByTokenHash(tokenHash).orElseThrow(() -> new UnauthorizedException("Refresh token is invalid"));
        LocalDateTime now = LocalDateTime.now();
        if (token.isRevoked() || !token.getExpiresAt().isAfter(now) || !token.getUser().isEnabled() || this.refreshTokenRepository.revokeIfActive(token.getId(), now) != 1) {
            throw new UnauthorizedException("Refresh token is expired or revoked");
        }
        token.setRevoked(true);
        return this.issueTokens(token.getUser());
    }

    @Transactional
    public void logout(String rawToken, Long userId) {
        this.refreshTokenRepository.findByTokenHash(this.tokenHashingService.sha256(rawToken)).filter(token -> token.getUser().getId().equals(userId)).ifPresent(token -> token.setRevoked(true));
    }

    @Transactional(readOnly=true)
    public UserResponse profile(Long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserResponse.from(user);
    }

    public com.datavault.personal_data_vault.dto.response.UserResponse profile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return com.datavault.personal_data_vault.dto.response.UserResponse.from(user);
    }

    private AuthResponse issueTokens(User user) {
        UserPrincipal principal = UserPrincipal.from(user);
        String access = this.jwtService.generateAccessToken(principal, Map.of("userId", user.getId(), "role", user.getRole().name(), "tokenType", "USER"));
        byte[] bytes = new byte[48];
        this.secureRandom.nextBytes(bytes);
        String rawRefreshToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        this.refreshTokenRepository.save(RefreshToken.builder().tokenHash(this.tokenHashingService.sha256(rawRefreshToken)).user(user).expiresAt(LocalDateTime.now().plusNanos(Math.multiplyExact(this.refreshTokenExpiration, 1000000))).revoked(false).build());
        return AuthResponse.builder().accessToken(access).refreshToken(rawRefreshToken).userId(user.getId()).email(user.getEmail()).role(user.getRole().name()).build();
    }

    @Generated
    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtService jwtService, TokenHashingService tokenHashingService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenHashingService = tokenHashingService;
    }
}
