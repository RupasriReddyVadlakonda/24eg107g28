package com.datavault.personal_data_vault.controller;

import com.datavault.personal_data_vault.dto.request.DataAccessRequest;
import com.datavault.personal_data_vault.dto.response.ApiResponse;
import com.datavault.personal_data_vault.dto.response.PersonalDataResponse;
import com.datavault.personal_data_vault.entity.PersonalData;
import com.datavault.personal_data_vault.security.ApplicationPrincipal;
import com.datavault.personal_data_vault.service.DataAccessService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="Application Data Access", description="App-JWT-only data requests requiring exact user, type, purpose, operation and active consent")
@RequestMapping(value={"/api/data-access"})
@Validated
public class DataAccessController {
    private final DataAccessService dataAccessService;

    @GetMapping(value={"/{dataType}"})
    public ApiResponse<PersonalDataResponse> read(@AuthenticationPrincipal ApplicationPrincipal app, @PathVariable PersonalData.DataType dataType, @RequestParam @NotNull Long userId, @RequestParam @NotBlank String purpose, HttpServletRequest request) {
        return ApiResponse.success("Authorized data returned", this.dataAccessService.read(userId, app.applicationId(), dataType, purpose, request.getRemoteAddr()));
    }

    @PutMapping(value={"/{dataType}"})
    public ApiResponse<PersonalDataResponse> write(@AuthenticationPrincipal ApplicationPrincipal app, @PathVariable PersonalData.DataType dataType, @Valid @RequestBody DataAccessRequest body, HttpServletRequest request) {
        this.dataAccessService.write(body.getUserId(), app.applicationId(), dataType, body.getPurpose(), body.getValue(), request.getRemoteAddr());
        return ApiResponse.success("Authorized data updated", null);
    }

    @Generated
    public DataAccessController(DataAccessService dataAccessService) {
        this.dataAccessService = dataAccessService;
    }
}

