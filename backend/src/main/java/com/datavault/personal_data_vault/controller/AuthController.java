package com.datavault.personal_data_vault.controller;

import com.datavault.personal_data_vault.dto.request.LoginRequest;
import com.datavault.personal_data_vault.dto.request.RefreshTokenRequest;
import com.datavault.personal_data_vault.dto.request.RegisterRequest;
import com.datavault.personal_data_vault.dto.response.ApiResponse;
import com.datavault.personal_data_vault.dto.response.AuthResponse;
import com.datavault.personal_data_vault.dto.response.UserResponse;
import com.datavault.personal_data_vault.security.UserPrincipal;
import com.datavault.personal_data_vault.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="Authentication", description="User registration, login, refresh-token rotation and logout")
@RequestMapping(value={"/api/auth"})
public class AuthController {
    private final AuthService authService;

    @PostMapping(value={"/register"})
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.CREATED).body(ApiResponse.success("Registration successful", this.authService.register(request)));
    }

    @PostMapping(value={"/login"})
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Login successful", this.authService.login(request)));
    }

    @PostMapping(value={"/refresh"})
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Tokens rotated", this.authService.refresh(request.getRefreshToken())));
    }

    @PostMapping(value={"/logout"})
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody RefreshTokenRequest request) {
        this.authService.logout(request.getRefreshToken(), user.id());
        return ResponseEntity.ok(ApiResponse.success("Logged out"));
    }

    @GetMapping(value={"/me"})
    public ApiResponse<UserResponse> profile(@AuthenticationPrincipal UserPrincipal user) {
        return ApiResponse.success("Profile retrieved", this.authService.profile(user.id()));
    }

    @Generated
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
}
