package com.datavault.personal_data_vault.controller;

import com.datavault.personal_data_vault.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Service status", description = "Public application liveness check")
public class RootController {

    @GetMapping("/")
    @Operation(security = {})
    public ApiResponse<Void> status() {
        return ApiResponse.success("Personal Data Vault API is running");
    }
}
