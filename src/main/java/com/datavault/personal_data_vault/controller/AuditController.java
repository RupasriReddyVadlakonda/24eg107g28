package com.datavault.personal_data_vault.controller;

import com.datavault.personal_data_vault.dto.response.AccessLogResponse;
import com.datavault.personal_data_vault.dto.response.ApiResponse;
import com.datavault.personal_data_vault.repository.AccessLogRepository;
import com.datavault.personal_data_vault.security.UserPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.Generated;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="Audit", description="Paginated access logs; users can view only their own records")
@RequestMapping(value={"/api/audit/logs"})
public class AuditController {
    private final AccessLogRepository accessLogRepository;

    @GetMapping
    public ApiResponse<List<AccessLogResponse>> own(@AuthenticationPrincipal UserPrincipal user, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="50") int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 100));
        return ApiResponse.success("Audit logs retrieved", this.accessLogRepository.findByUserId(user.id(), (Pageable)PageRequest.of((int)safePage, (int)safeSize, (Sort)Sort.by((Sort.Direction)Sort.Direction.DESC, (String[])new String[]{"timestamp"}))).map(AccessLogResponse::from).getContent());
    }

    @GetMapping(value={"/{userId}"})
    public ApiResponse<List<AccessLogResponse>> byUser(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long userId, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="50") int size) {
        if (!user.role().name().equals("ADMIN") && !user.id().equals(userId)) {
            throw new AccessDeniedException("Audit access denied");
        }
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 100));
        return ApiResponse.success("Audit logs retrieved", this.accessLogRepository.findByUserId(userId, (Pageable)PageRequest.of((int)safePage, (int)safeSize, (Sort)Sort.by((Sort.Direction)Sort.Direction.DESC, (String[])new String[]{"timestamp"}))).map(AccessLogResponse::from).getContent());
    }

    @Generated
    public AuditController(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }
}

