package com.datavault.personal_data_vault.service;

import com.datavault.personal_data_vault.dto.request.ConsentRequest;
import com.datavault.personal_data_vault.dto.response.ConsentResponse;
import com.datavault.personal_data_vault.entity.Consent;
import com.datavault.personal_data_vault.entity.ThirdPartyApplication;
import com.datavault.personal_data_vault.entity.User;
import com.datavault.personal_data_vault.exception.ResourceNotFoundException;
import com.datavault.personal_data_vault.repository.ConsentRepository;
import com.datavault.personal_data_vault.repository.ThirdPartyApplicationRepository;
import com.datavault.personal_data_vault.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Generated;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsentService {
    private final ConsentRepository consentRepository;
    private final UserRepository userRepository;
    private final ThirdPartyApplicationRepository applicationRepository;

    @Transactional
    public ConsentResponse request(Long requestedByUserId, Long requestedByAppId, ConsentRequest request) {
        Long targetUserId;
        Long l = targetUserId = requestedByAppId == null ? requestedByUserId : request.getUserId();
        if (targetUserId == null) {
            throw new IllegalArgumentException("userId is required for application consent requests");
        }
        if (requestedByAppId != null && !requestedByAppId.equals(request.getApplicationId())) {
            throw new AccessDeniedException("Application cannot request consent for another application");
        }
        User user = (User)this.userRepository.findById(targetUserId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ThirdPartyApplication app = this.applicationRepository.findById(request.getApplicationId()).filter(ThirdPartyApplication::isActive).orElseThrow(() -> new ResourceNotFoundException("Active application not found"));
        Consent consent = Consent.builder().user(user).application(app).dataType(request.getDataType()).purpose(request.getPurpose().trim()).allowedOperation(request.getOperation()).requestedDurationDays(request.getRequestedDurationDays()).status(Consent.ConsentStatus.PENDING).build();
        return ConsentResponse.from((Consent)this.consentRepository.save(consent));
    }

    @Transactional(readOnly=true)
    public List<ConsentResponse> list(Long userId) {
        return this.consentRepository.findByUserId(userId).stream().map(ConsentResponse::from).toList();
    }

    @Transactional(readOnly=true)
    public ConsentResponse get(Long userId, Long consentId) {
        return ConsentResponse.from(this.findOwned(userId, consentId));
    }

    @Transactional
    public ConsentResponse grant(Long userId, Long consentId) {
        Consent consent = this.findOwned(userId, consentId);
        this.requirePending(consent);
        LocalDateTime start = LocalDateTime.now();
        consent.setStatus(Consent.ConsentStatus.GRANTED);
        consent.setStartTime(start);
        consent.setExpirationTime(start.plusDays(consent.getRequestedDurationDays()));
        return ConsentResponse.from((Consent)this.consentRepository.save(consent));
    }

    @Transactional
    public ConsentResponse deny(Long userId, Long consentId) {
        Consent consent = this.findOwned(userId, consentId);
        this.requirePending(consent);
        consent.setStatus(Consent.ConsentStatus.DENIED);
        return ConsentResponse.from((Consent)this.consentRepository.save(consent));
    }

    @Transactional
    public ConsentResponse revoke(Long userId, Long consentId) {
        Consent consent = this.findOwned(userId, consentId);
        if (consent.getStatus() != Consent.ConsentStatus.GRANTED) {
            throw new IllegalArgumentException("Only a granted consent can be revoked");
        }
        consent.setStatus(Consent.ConsentStatus.REVOKED);
        consent.setRevokedAt(LocalDateTime.now());
        return ConsentResponse.from((Consent)this.consentRepository.save(consent));
    }

    @Transactional(readOnly=true)
    public List<ConsentResponse> listAll() {
        return this.consentRepository.findAll().stream().map(ConsentResponse::from).toList();
    }

    private Consent findOwned(Long userId, Long consentId) {
        return this.consentRepository.findByIdAndUserId(consentId, userId).orElseThrow(() -> new ResourceNotFoundException("Consent not found"));
    }

    private void requirePending(Consent consent) {
        if (consent.getStatus() != Consent.ConsentStatus.PENDING) {
            throw new IllegalArgumentException("Only pending consent requests can be changed");
        }
    }

    @Generated
    public ConsentService(ConsentRepository consentRepository, UserRepository userRepository, ThirdPartyApplicationRepository applicationRepository) {
        this.consentRepository = consentRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }
}

