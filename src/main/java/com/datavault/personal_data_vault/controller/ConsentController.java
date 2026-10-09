package com.datavault.personal_data_vault.controller;

import com.datavault.personal_data_vault.dto.request.ConsentRequest;
import com.datavault.personal_data_vault.dto.response.ApiResponse;
import com.datavault.personal_data_vault.dto.response.ConsentResponse;
import com.datavault.personal_data_vault.security.ApplicationPrincipal;
import com.datavault.personal_data_vault.security.UserPrincipal;
import com.datavault.personal_data_vault.service.ConsentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="Consent Management", description="Submit, review, grant, deny and revoke purpose-bound, operation-specific consents")
@RequestMapping(value={"/api/consents"})
public class ConsentController {
    private final ConsentService consentService;

    @PostMapping(value={"/request"})
    public ResponseEntity<ApiResponse<ConsentResponse>> request(@AuthenticationPrincipal Object principal, @Valid @RequestBody ConsentRequest request) {
        Long appId;
        Long userId;
        if (principal instanceof UserPrincipal user) {
            userId = user.id();
        } else {
            userId = null;
        }
        if (principal instanceof ApplicationPrincipal application) {
            appId = application.applicationId();
        } else {
            appId = null;
        }
        if (userId == null && appId == null) {
            throw new AccessDeniedException("Authenticated user or application required");
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.CREATED).body(ApiResponse.success("Consent request created", this.consentService.request(userId, appId, request)));
    }

    @GetMapping
    public ApiResponse<List<ConsentResponse>> list(@AuthenticationPrincipal UserPrincipal user) {
        return ApiResponse.success("Consents retrieved", this.consentService.list(user.id()));
    }

    @GetMapping(value={"/{id}"})
    public ApiResponse<ConsentResponse> get(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id) {
        return ApiResponse.success("Consent retrieved", this.consentService.get(user.id(), id));
    }

    @PutMapping(value={"/{id}/grant"})
    public ApiResponse<ConsentResponse> grant(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id) {
        return ApiResponse.success("Consent granted", this.consentService.grant(user.id(), id));
    }

    @PutMapping(value={"/{id}/deny"})
    public ApiResponse<ConsentResponse> deny(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id) {
        return ApiResponse.success("Consent denied", this.consentService.deny(user.id(), id));
    }

    @PutMapping(value={"/{id}/revoke"})
    public ApiResponse<ConsentResponse> revoke(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id) {
        return ApiResponse.success("Consent revoked", this.consentService.revoke(user.id(), id));
    }

    @Generated
    public ConsentController(ConsentService consentService) {
        this.consentService = consentService;
    }
}
