package com.datavault.personal_data_vault.controller;

import com.datavault.personal_data_vault.dto.request.AppRegistrationRequest;
import com.datavault.personal_data_vault.dto.request.AppTokenRequest;
import com.datavault.personal_data_vault.dto.request.AppUpdateRequest;
import com.datavault.personal_data_vault.dto.response.ApiResponse;
import com.datavault.personal_data_vault.dto.response.AppRegistrationResponse;
import com.datavault.personal_data_vault.dto.response.AppResponse;
import com.datavault.personal_data_vault.dto.response.AppTokenResponse;
import com.datavault.personal_data_vault.security.UserPrincipal;
import com.datavault.personal_data_vault.service.ApplicationService;
import com.datavault.personal_data_vault.redis.RateLimiterService;
import com.datavault.personal_data_vault.exception.RateLimitExceededException;
import jakarta.servlet.http.HttpServletRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="Applications", description="Register and manage owner-scoped client applications and exchange client credentials for app JWTs")
@RequestMapping(value={"/api/apps"})
public class ApplicationController {
    private final ApplicationService applicationService;
    private final RateLimiterService rateLimiterService;

    @PostMapping
    @Operation(security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<AppRegistrationResponse>> register(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody AppRegistrationRequest request) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.CREATED).body(ApiResponse.success("Application registered", this.applicationService.register(user.id(), request)));
    }

    @GetMapping
    public ApiResponse<List<AppResponse>> list(@AuthenticationPrincipal UserPrincipal user) {
        return ApiResponse.success("Applications retrieved", this.applicationService.list(user.id()));
    }

    @GetMapping(value={"/{id}"})
    public ApiResponse<AppResponse> get(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id) {
        return ApiResponse.success("Application retrieved", this.applicationService.get(user.id(), id));
    }

    @PutMapping(value={"/{id}"})
    public ApiResponse<AppResponse> update(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id, @Valid @RequestBody AppUpdateRequest request) {
        return ApiResponse.success("Application updated", this.applicationService.update(user.id(), id, request));
    }

    @DeleteMapping(value={"/{id}"})
    public ApiResponse<Void> delete(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id) {
        this.applicationService.delete(user.id(), id);
        return ApiResponse.success("Application disabled; credentials can no longer obtain tokens");
    }

    @PostMapping(value={"/token"})
    @Operation(security = {})
    public ResponseEntity<ApiResponse<AppTokenResponse>> token(
            @Valid @RequestBody AppTokenRequest request,
            HttpServletRequest servletRequest
    ) {
        if (!rateLimiterService.isAllowed("app-token:" + servletRequest.getRemoteAddr())) {
            throw new RateLimitExceededException();
        }
        return ResponseEntity.ok(ApiResponse.success("Application token issued", this.applicationService.token(request.getClientId(), request.getClientSecret())));
    }

    @Generated
    public ApplicationController(ApplicationService applicationService, RateLimiterService rateLimiterService) {
        this.applicationService = applicationService;
        this.rateLimiterService = rateLimiterService;
    }
}

