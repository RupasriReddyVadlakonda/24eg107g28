package com.datavault.personal_data_vault.service;

import com.datavault.personal_data_vault.audit.AuditService;
import com.datavault.personal_data_vault.dto.response.PersonalDataResponse;
import com.datavault.personal_data_vault.entity.Consent;
import com.datavault.personal_data_vault.entity.PersonalData;
import com.datavault.personal_data_vault.entity.ThirdPartyApplication;
import com.datavault.personal_data_vault.entity.User;
import com.datavault.personal_data_vault.exception.ConsentAccessDeniedException;
import com.datavault.personal_data_vault.exception.ResourceNotFoundException;
import com.datavault.personal_data_vault.redis.RateLimiterService;
import com.datavault.personal_data_vault.repository.ThirdPartyApplicationRepository;
import com.datavault.personal_data_vault.repository.UserRepository;
import com.datavault.personal_data_vault.service.ConsentAuthorizationService;
import com.datavault.personal_data_vault.service.PersonalDataService;
import com.datavault.personal_data_vault.service.SecurityAlertService;
import lombok.Generated;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataAccessService {
    private final ConsentAuthorizationService authorizationService;
    private final PersonalDataService personalDataService;
    private final UserRepository userRepository;
    private final ThirdPartyApplicationRepository applicationRepository;
    private final AuditService auditService;
    private final SecurityAlertService securityAlertService;
    private final RateLimiterService rateLimiterService;

    @Transactional(noRollbackFor={ConsentAccessDeniedException.class})
    public PersonalDataResponse read(Long userId, Long applicationId, PersonalData.DataType dataType, String purpose, String ipAddress) {
        User user = (User)this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ThirdPartyApplication application = this.findApplication(applicationId);
        try {
            if (!this.rateLimiterService.isAllowed(String.valueOf(applicationId))) {
                throw new ConsentAccessDeniedException("Application rate limit exceeded", "RATE_LIMIT_EXCEEDED");
            }
            this.authorizationService.authorize(userId, applicationId, dataType, purpose, Consent.AllowedOperation.READ);
            PersonalDataResponse response = this.personalDataService.getAuthorized(userId, dataType);
            this.auditService.logAccess(user, application, dataType, "READ", purpose, ipAddress, true, null);
            return response;
        }
        catch (RuntimeException ex) {
            String reason;
            boolean consentFailure = ex instanceof ConsentAccessDeniedException;
            if (ex instanceof ConsentAccessDeniedException) {
                ConsentAccessDeniedException denied = (ConsentAccessDeniedException)ex;
                reason = denied.getErrorCode();
            } else {
                reason = "DATA_ACCESS_FAILED";
            }
            if (consentFailure && reason.startsWith("CONSENT_")) {
                this.securityAlertService.recordUnauthorizedAttempt(user, application, reason);
            }
            if ("RATE_LIMIT_EXCEEDED".equals(reason) && this.rateLimiterService.isFirstRejectedRequest(String.valueOf(applicationId))) {
                this.securityAlertService.recordRateLimitExceeded(user, application);
            }
            this.auditService.logAccess(user, application, dataType, "READ", purpose, ipAddress, false, reason);
            throw ex;
        }
    }

    @Transactional(noRollbackFor={ConsentAccessDeniedException.class})
    public void write(Long userId, Long applicationId, PersonalData.DataType dataType, String purpose, String value, String ipAddress) {
        User user = (User)this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ThirdPartyApplication application = this.findApplication(applicationId);
        try {
            if (!this.rateLimiterService.isAllowed(String.valueOf(applicationId))) {
                throw new ConsentAccessDeniedException("Application rate limit exceeded", "RATE_LIMIT_EXCEEDED");
            }
            this.authorizationService.authorize(userId, applicationId, dataType, purpose, Consent.AllowedOperation.WRITE);
            this.personalDataService.writeAuthorized(user, dataType, value);
            this.auditService.logAccess(user, application, dataType, "WRITE", purpose, ipAddress, true, null);
        }
        catch (RuntimeException ex) {
            String reason;
            boolean consentFailure = ex instanceof ConsentAccessDeniedException;
            if (ex instanceof ConsentAccessDeniedException) {
                ConsentAccessDeniedException denied = (ConsentAccessDeniedException)ex;
                reason = denied.getErrorCode();
            } else {
                reason = "DATA_ACCESS_FAILED";
            }
            if (consentFailure && reason.startsWith("CONSENT_")) {
                this.securityAlertService.recordUnauthorizedAttempt(user, application, reason);
            }
            if ("RATE_LIMIT_EXCEEDED".equals(reason) && this.rateLimiterService.isFirstRejectedRequest(String.valueOf(applicationId))) {
                this.securityAlertService.recordRateLimitExceeded(user, application);
            }
            this.auditService.logAccess(user, application, dataType, "WRITE", purpose, ipAddress, false, reason);
            throw ex;
        }
    }

    private ThirdPartyApplication findApplication(Long applicationId) {
        return (ThirdPartyApplication)this.applicationRepository.findById(applicationId).orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }

    @Generated
    public DataAccessService(ConsentAuthorizationService authorizationService, PersonalDataService personalDataService, UserRepository userRepository, ThirdPartyApplicationRepository applicationRepository, AuditService auditService, SecurityAlertService securityAlertService, RateLimiterService rateLimiterService) {
        this.authorizationService = authorizationService;
        this.personalDataService = personalDataService;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.auditService = auditService;
        this.securityAlertService = securityAlertService;
        this.rateLimiterService = rateLimiterService;
    }
}
