package com.datavault.personal_data_vault.controller;

import com.datavault.personal_data_vault.dto.response.AccessLogResponse;
import com.datavault.personal_data_vault.dto.response.ApiResponse;
import com.datavault.personal_data_vault.dto.response.AppResponse;
import com.datavault.personal_data_vault.dto.response.ConsentResponse;
import com.datavault.personal_data_vault.dto.response.SecurityAlertResponse;
import com.datavault.personal_data_vault.dto.response.UserResponse;
import com.datavault.personal_data_vault.repository.AccessLogRepository;
import com.datavault.personal_data_vault.service.ConsentService;
import com.datavault.personal_data_vault.service.SecurityAlertService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.Generated;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="Administration", description="Role-protected system-wide user, app, consent, audit and alert operations")
@RequestMapping(value={"/api/admin"})
public class AdminController {
    private final SecurityAlertService securityAlertService;
    private final ConsentService consentService;
    private final AccessLogRepository accessLogRepository;

    @GetMapping(value={"/users"})
    public ApiResponse<List<UserResponse>> users() {
        return ApiResponse.success("Users retrieved", this.securityAlertService.listUsers());
    }

    @GetMapping(value={"/apps"})
    public ApiResponse<List<AppResponse>> apps() {
        return ApiResponse.success("Applications retrieved", this.securityAlertService.listApplications());
    }

    @GetMapping(value={"/consents"})
    public ApiResponse<List<ConsentResponse>> consents() {
        return ApiResponse.success("Consents retrieved", this.consentService.listAll());
    }

    @GetMapping(value={"/audit"})
    public ApiResponse<List<AccessLogResponse>> audit(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="50") int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 100));
        return ApiResponse.success("Audit logs retrieved", this.accessLogRepository.findAll((Pageable)PageRequest.of((int)safePage, (int)safeSize, (Sort)Sort.by((Sort.Direction)Sort.Direction.DESC, (String[])new String[]{"timestamp"}))).map(AccessLogResponse::from).getContent());
    }

    @GetMapping(value={"/alerts"})
    public ApiResponse<List<SecurityAlertResponse>> alerts() {
        return ApiResponse.success("Security alerts retrieved", this.securityAlertService.listAll());
    }

    @PutMapping(value={"/alerts/{id}/resolve"})
    public ApiResponse<SecurityAlertResponse> resolve(@PathVariable Long id) {
        return ApiResponse.success("Security alert resolved", this.securityAlertService.resolve(null, true, id));
    }

    @PutMapping(value={"/apps/{id}/disable"})
    public ApiResponse<Void> disableApplication(@PathVariable Long id) {
        this.securityAlertService.disableApplication(id);
        return ApiResponse.success("Application disabled");
    }

    @Generated
    public AdminController(SecurityAlertService securityAlertService, ConsentService consentService, AccessLogRepository accessLogRepository) {
        this.securityAlertService = securityAlertService;
        this.consentService = consentService;
        this.accessLogRepository = accessLogRepository;
    }
}

