package com.datavault.personal_data_vault.service;

import com.datavault.personal_data_vault.dto.response.AppResponse;
import com.datavault.personal_data_vault.dto.response.SecurityAlertResponse;
import com.datavault.personal_data_vault.dto.response.UserResponse;
import com.datavault.personal_data_vault.entity.SecurityAlert;
import com.datavault.personal_data_vault.entity.ThirdPartyApplication;
import com.datavault.personal_data_vault.entity.User;
import com.datavault.personal_data_vault.exception.ResourceNotFoundException;
import com.datavault.personal_data_vault.redis.RateLimiterService;
import com.datavault.personal_data_vault.repository.SecurityAlertRepository;
import com.datavault.personal_data_vault.repository.ThirdPartyApplicationRepository;
import com.datavault.personal_data_vault.repository.UserRepository;
import java.util.List;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityAlertService {
    private final SecurityAlertRepository alertRepository;
    private final ThirdPartyApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final RateLimiterService rateLimiterService;
    @Value(value="${app.security-alert.unauthorized-threshold}")
    private int unauthorizedThreshold;

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void recordUnauthorizedAttempt(User user, ThirdPartyApplication application, String reason) {
        long count = this.rateLimiterService.incrementUnauthorizedCounter(application.getId() + ":" + user.getId());
        if (count == (long)this.unauthorizedThreshold) {
            this.alertRepository.save(SecurityAlert.builder().user(user).application(application).alertType(SecurityAlert.AlertType.EXCESSIVE_UNAUTHORIZED_ACCESS).severity(SecurityAlert.Severity.HIGH).description("Application exceeded the configured unauthorized access threshold within the alert window").resolved(false).build());
        }
        if (reason != null && reason.contains("EXPIRED")) {
            this.alertRepository.save(SecurityAlert.builder().user(user).application(application).alertType(SecurityAlert.AlertType.EXPIRED_CONSENT_ACCESS_ATTEMPT).severity(SecurityAlert.Severity.MEDIUM).description("Application attempted access after consent expiration").resolved(false).build());
        }
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void recordRateLimitExceeded(User user, ThirdPartyApplication application) {
        this.alertRepository.save(SecurityAlert.builder().user(user).application(application).alertType(SecurityAlert.AlertType.RATE_LIMIT_EXCEEDED).severity(SecurityAlert.Severity.MEDIUM).description("Application exceeded its configured request rate limit").resolved(false).build());
    }

    @Transactional(readOnly=true)
    public List<SecurityAlertResponse> listOwn(Long userId) {
        return this.alertRepository.findByUserId(userId, (Pageable)PageRequest.of((int)0, (int)100)).stream().map(SecurityAlertResponse::from).toList();
    }

    @Transactional(readOnly=true)
    public List<SecurityAlertResponse> listAll() {
        return this.alertRepository.findAll((Pageable)PageRequest.of((int)0, (int)500)).stream().map(SecurityAlertResponse::from).toList();
    }

    @Transactional
    public SecurityAlertResponse resolve(Long userId, boolean admin, Long alertId) {
        SecurityAlert alert = this.alertRepository.findById(alertId).filter(item -> admin || item.getUser() != null && item.getUser().getId().equals(userId)).orElseThrow(() -> new ResourceNotFoundException("Security alert not found"));
        alert.setResolved(true);
        return SecurityAlertResponse.from((SecurityAlert)this.alertRepository.save(alert));
    }

    @Transactional
    public void disableApplication(Long applicationId) {
        ThirdPartyApplication application = (ThirdPartyApplication)this.applicationRepository.findById(applicationId).orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        application.setActive(false);
        this.applicationRepository.save(application);
    }

    @Transactional(readOnly=true)
    public List<UserResponse> listUsers() {
        return this.userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    @Transactional(readOnly=true)
    public List<AppResponse> listApplications() {
        return this.applicationRepository.findAll().stream().map(AppResponse::from).toList();
    }

    @Generated
    public SecurityAlertService(SecurityAlertRepository alertRepository, ThirdPartyApplicationRepository applicationRepository, UserRepository userRepository, RateLimiterService rateLimiterService) {
        this.alertRepository = alertRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.rateLimiterService = rateLimiterService;
    }
}

