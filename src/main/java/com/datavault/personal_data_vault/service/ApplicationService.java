package com.datavault.personal_data_vault.service;

import com.datavault.personal_data_vault.dto.request.AppRegistrationRequest;
import com.datavault.personal_data_vault.dto.request.AppUpdateRequest;
import com.datavault.personal_data_vault.dto.response.AppRegistrationResponse;
import com.datavault.personal_data_vault.dto.response.AppResponse;
import com.datavault.personal_data_vault.dto.response.AppTokenResponse;
import com.datavault.personal_data_vault.entity.ThirdPartyApplication;
import com.datavault.personal_data_vault.entity.User;
import com.datavault.personal_data_vault.exception.DuplicateResourceException;
import com.datavault.personal_data_vault.exception.ResourceNotFoundException;
import com.datavault.personal_data_vault.repository.ThirdPartyApplicationRepository;
import com.datavault.personal_data_vault.repository.UserRepository;
import com.datavault.personal_data_vault.security.JwtService;
import java.net.URI;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationService {
    private final ThirdPartyApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SecureRandom secureRandom = new SecureRandom();
    @Value(value="${app.jwt.access-token-expiration}")
    private long tokenLifetimeMillis;

    @Transactional
    public AppRegistrationResponse register(Long ownerId, AppRegistrationRequest request) {
        if (this.applicationRepository.existsByApplicationName(request.getApplicationName().trim())) {
            throw new DuplicateResourceException("An application with that name already exists");
        }
        this.validateRedirectUri(request.getRedirectUri());
        User owner = (User)this.userRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String clientId = "pda_" + this.randomSecret(18);
        String clientSecret = this.randomSecret(48);
        ThirdPartyApplication app = (ThirdPartyApplication)this.applicationRepository.save(ThirdPartyApplication.builder().applicationName(request.getApplicationName().trim()).clientId(clientId).clientSecretHash(this.passwordEncoder.encode((CharSequence)clientSecret)).description(request.getDescription()).redirectUri(request.getRedirectUri().trim()).createdBy(owner).active(true).build());
        return AppRegistrationResponse.builder().id(app.getId()).applicationName(app.getApplicationName()).clientId(clientId).clientSecret(clientSecret).redirectUri(app.getRedirectUri()).message("Store this client secret securely; it will not be shown again.").build();
    }

    @Transactional(readOnly=true)
    public List<AppResponse> list(Long ownerId) {
        return this.applicationRepository.findByCreatedById(ownerId).stream().map(AppResponse::from).toList();
    }

    @Transactional(readOnly=true)
    public AppResponse get(Long ownerId, Long appId) {
        return AppResponse.from(this.findOwned(ownerId, appId));
    }

    @Transactional
    public AppResponse update(Long ownerId, Long appId, AppUpdateRequest request) {
        ThirdPartyApplication app = this.findOwned(ownerId, appId);
        if (this.applicationRepository.existsByApplicationNameAndIdNot(request.getApplicationName().trim(), appId)) {
            throw new DuplicateResourceException("An application with that name already exists");
        }
        this.validateRedirectUri(request.getRedirectUri());
        app.setApplicationName(request.getApplicationName().trim());
        app.setDescription(request.getDescription());
        app.setRedirectUri(request.getRedirectUri().trim());
        return AppResponse.from((ThirdPartyApplication)this.applicationRepository.save(app));
    }

    @Transactional
    public void delete(Long ownerId, Long appId) {
        ThirdPartyApplication app = this.findOwned(ownerId, appId);
        app.setActive(false);
        this.applicationRepository.save(app);
    }

    @Transactional(readOnly=true)
    public AppTokenResponse token(String clientId, String clientSecret) {
        ThirdPartyApplication app = this.applicationRepository.findByClientId(clientId).orElseThrow(() -> new BadCredentialsException("Invalid client credentials"));
        if (!app.isActive() || !this.passwordEncoder.matches((CharSequence)clientSecret, app.getClientSecretHash())) {
            throw new BadCredentialsException("Invalid client credentials");
        }
        return AppTokenResponse.builder().accessToken(this.jwtService.generateApplicationToken(app)).tokenType("Bearer").expiresIn(this.tokenLifetimeMillis / 1000L).applicationId(app.getId()).build();
    }

    private ThirdPartyApplication findOwned(Long ownerId, Long appId) {
        return this.applicationRepository.findByIdAndCreatedById(appId, ownerId).orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }

    private String randomSecret(int bytes) {
        byte[] value = new byte[bytes];
        this.secureRandom.nextBytes(value);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }

    private void validateRedirectUri(String rawUri) {
        try {
            URI uri = URI.create(rawUri);
            if (!uri.isAbsolute() || uri.getHost() == null || !"https".equalsIgnoreCase(uri.getScheme()) && !"http".equalsIgnoreCase(uri.getScheme())) {
                throw new IllegalArgumentException();
            }
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Redirect URI must be an absolute HTTP or HTTPS URI");
        }
    }

    @Generated
    public ApplicationService(ThirdPartyApplicationRepository applicationRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
}

