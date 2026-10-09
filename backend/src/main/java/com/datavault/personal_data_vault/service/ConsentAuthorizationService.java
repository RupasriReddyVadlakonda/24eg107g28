package com.datavault.personal_data_vault.service;

import com.datavault.personal_data_vault.entity.Consent;
import com.datavault.personal_data_vault.entity.PersonalData;
import com.datavault.personal_data_vault.entity.ThirdPartyApplication;
import com.datavault.personal_data_vault.exception.ConsentAccessDeniedException;
import com.datavault.personal_data_vault.repository.ConsentRepository;
import com.datavault.personal_data_vault.repository.ThirdPartyApplicationRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Generated;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsentAuthorizationService {
    private final ConsentRepository consentRepository;
    private final ThirdPartyApplicationRepository applicationRepository;

    @Transactional(noRollbackFor={ConsentAccessDeniedException.class})
    public Consent authorize(Long userId, Long applicationId, PersonalData.DataType dataType, String purpose, Consent.AllowedOperation operation) {
        ThirdPartyApplication application = this.applicationRepository.findById(applicationId).filter(app -> app.isActive()).orElseThrow(() -> new ConsentAccessDeniedException("Application is inactive or unknown", "APPLICATION_INACTIVE"));
        List<Consent> consents = this.consentRepository.findByUserIdAndApplicationIdAndDataType(userId, applicationId, dataType);
        LocalDateTime now = LocalDateTime.now();
        boolean expiredMatch = false;
        for (Consent consent : consents) {
            if (!consent.getPurpose().equals(purpose.trim())) continue;
            if (consent.getStatus() == Consent.ConsentStatus.GRANTED && consent.getExpirationTime() != null && !consent.getExpirationTime().isAfter(now)) {
                consent.setStatus(Consent.ConsentStatus.EXPIRED);
                expiredMatch = true;
            }
            if (consent.getStatus() == Consent.ConsentStatus.EXPIRED) {
                expiredMatch = true;
            }
            if (consent.getStatus() != Consent.ConsentStatus.GRANTED || consent.getStartTime() != null && consent.getStartTime().isAfter(now) || consent.getExpirationTime() != null && !consent.getExpirationTime().isAfter(now) || !this.allows(consent.getAllowedOperation(), operation)) continue;
            return consent;
        }
        if (expiredMatch) {
            throw new ConsentAccessDeniedException("Consent has expired", "CONSENT_EXPIRED");
        }
        throw new ConsentAccessDeniedException("No active consent permits this application, data type, purpose, and operation", "CONSENT_NOT_FOUND");
    }

    public boolean isAccessAllowed(Long userId, Long applicationId, String dataType, String purpose, String operation) {
        try {
            this.authorize(userId, applicationId, PersonalData.DataType.valueOf(dataType), purpose, Consent.AllowedOperation.valueOf(operation));
            return true;
        }
        catch (ConsentAccessDeniedException | IllegalArgumentException ex) {
            return false;
        }
    }

    private boolean allows(Consent.AllowedOperation allowed, Consent.AllowedOperation requested) {
        if (requested == Consent.AllowedOperation.READ_WRITE) {
            return allowed == Consent.AllowedOperation.READ_WRITE;
        }
        return allowed == requested || allowed == Consent.AllowedOperation.READ_WRITE;
    }

    @Generated
    public ConsentAuthorizationService(ConsentRepository consentRepository, ThirdPartyApplicationRepository applicationRepository) {
        this.consentRepository = consentRepository;
        this.applicationRepository = applicationRepository;
    }
}

