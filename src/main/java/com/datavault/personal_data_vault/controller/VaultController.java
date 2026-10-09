package com.datavault.personal_data_vault.controller;

import com.datavault.personal_data_vault.dto.request.PersonalDataRequest;
import com.datavault.personal_data_vault.dto.request.PersonalDataPatchRequest;
import com.datavault.personal_data_vault.dto.response.ApiResponse;
import com.datavault.personal_data_vault.dto.response.PersonalDataResponse;
import com.datavault.personal_data_vault.security.UserPrincipal;
import com.datavault.personal_data_vault.service.PersonalDataService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="Personal Data Vault", description="Manage the authenticated user's AES-256-GCM-encrypted personal records")
@RequestMapping(value={"/api/vault/data"})
public class VaultController {
    private final PersonalDataService personalDataService;

    @PostMapping
    public ResponseEntity<ApiResponse<PersonalDataResponse>> create(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody PersonalDataRequest request) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.CREATED).body(ApiResponse.success("Personal data saved", this.personalDataService.create(user.id(), request)));
    }

    @GetMapping
    public ApiResponse<List<PersonalDataResponse>> list(@AuthenticationPrincipal UserPrincipal user) {
        return ApiResponse.success("Personal data retrieved", this.personalDataService.list(user.id()));
    }

    @GetMapping(value={"/{id}"})
    public ApiResponse<PersonalDataResponse> get(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id) {
        return ApiResponse.success("Personal data retrieved", this.personalDataService.get(user.id(), id));
    }

    @PutMapping(value={"/{id}"})
    public ApiResponse<PersonalDataResponse> update(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id, @Valid @RequestBody PersonalDataRequest request) {
        return ApiResponse.success("Personal data updated", this.personalDataService.update(user.id(), id, request));
    }

    @PatchMapping(value={"/{id}"})
    public ApiResponse<PersonalDataResponse> patch(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id, @Valid @RequestBody PersonalDataPatchRequest request) {
        return ApiResponse.success("Personal data updated", this.personalDataService.patch(user.id(), id, request));
    }

    @DeleteMapping(value={"/{id}"})
    public ApiResponse<Void> delete(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id) {
        this.personalDataService.delete(user.id(), id);
        return ApiResponse.success("Personal data deleted");
    }

    @Generated
    public VaultController(PersonalDataService personalDataService) {
        this.personalDataService = personalDataService;
    }
}
