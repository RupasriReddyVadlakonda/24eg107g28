package com.datavault.personal_data_vault.controller;

import com.datavault.personal_data_vault.dto.response.ApiResponse;
import com.datavault.personal_data_vault.dto.response.SecurityAlertResponse;
import com.datavault.personal_data_vault.security.UserPrincipal;
import com.datavault.personal_data_vault.service.SecurityAlertService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.Generated;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="Security Alerts", description="View and resolve alerts associated with the authenticated user")
@RequestMapping(value={"/api/security-alerts"})
public class SecurityAlertController {
    private final SecurityAlertService securityAlertService;

    @GetMapping
    public ApiResponse<List<SecurityAlertResponse>> list(@AuthenticationPrincipal UserPrincipal user) {
        return ApiResponse.success("Security alerts retrieved", this.securityAlertService.listOwn(user.id()));
    }

    @PutMapping(value={"/{id}/resolve"})
    public ApiResponse<SecurityAlertResponse> resolve(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id) {
        return ApiResponse.success("Security alert resolved", this.securityAlertService.resolve(user.id(), false, id));
    }

    @Generated
    public SecurityAlertController(SecurityAlertService securityAlertService) {
        this.securityAlertService = securityAlertService;
    }
}

