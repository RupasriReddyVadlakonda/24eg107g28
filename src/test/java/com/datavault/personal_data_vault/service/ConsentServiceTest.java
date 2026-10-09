package com.datavault.personal_data_vault.service;

import com.datavault.personal_data_vault.dto.request.ConsentRequest;
import com.datavault.personal_data_vault.dto.response.ConsentResponse;
import com.datavault.personal_data_vault.entity.Consent;
import com.datavault.personal_data_vault.entity.PersonalData;
import com.datavault.personal_data_vault.entity.ThirdPartyApplication;
import com.datavault.personal_data_vault.entity.User;
import com.datavault.personal_data_vault.exception.ResourceNotFoundException;
import com.datavault.personal_data_vault.repository.ConsentRepository;
import com.datavault.personal_data_vault.repository.ThirdPartyApplicationRepository;
import com.datavault.personal_data_vault.repository.UserRepository;
import com.datavault.personal_data_vault.service.ConsentService;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(value={MockitoExtension.class})
class ConsentServiceTest {
    @Mock
    private ConsentRepository consentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ThirdPartyApplicationRepository applicationRepository;

    ConsentServiceTest() {
    }

    @Test
    void createsPendingRequestThenSupportsGrantDenyAndRevoke() {
        User user = User.builder().id(Long.valueOf(1L)).email("user@example.test").build();
        ThirdPartyApplication app = ThirdPartyApplication.builder().id(Long.valueOf(2L)).applicationName("Shop").active(true).build();
        ConsentService service = new ConsentService(this.consentRepository, this.userRepository, this.applicationRepository);
        Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(this.applicationRepository.findById(2L)).thenReturn(Optional.of(app));
        Mockito.when(this.consentRepository.save(ArgumentMatchers.any(Consent.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ConsentRequest request = new ConsentRequest();
        request.setApplicationId(Long.valueOf(2L));
        request.setDataType(PersonalData.DataType.EMAIL);
        request.setPurpose("Order Confirmation");
        request.setOperation(Consent.AllowedOperation.READ);
        request.setRequestedDurationDays(Integer.valueOf(7));
        ConsentResponse pending = service.request(Long.valueOf(1L), null, request);
        Assertions.assertEquals((Object)"PENDING", (Object)pending.getStatus());
        Assertions.assertEquals((int)7, (Integer)pending.getRequestedDurationDays());
        Consent granted = this.consent(pending.getId(), user, app, Consent.ConsentStatus.PENDING);
        Mockito.when((Object)this.consentRepository.findByIdAndUserId(Long.valueOf(10L), Long.valueOf(1L))).thenReturn(Optional.of(granted));
        Assertions.assertEquals((Object)"GRANTED", (Object)service.grant(Long.valueOf(1L), Long.valueOf(10L)).getStatus());
        Assertions.assertNotNull((Object)granted.getExpirationTime());
        Consent revoked = this.consent(11L, user, app, Consent.ConsentStatus.GRANTED);
        Mockito.when((Object)this.consentRepository.findByIdAndUserId(Long.valueOf(11L), Long.valueOf(1L))).thenReturn(Optional.of(revoked));
        Assertions.assertEquals((Object)"REVOKED", (Object)service.revoke(Long.valueOf(1L), Long.valueOf(11L)).getStatus());
        Assertions.assertNotNull((Object)revoked.getRevokedAt());
        Consent denied = this.consent(12L, user, app, Consent.ConsentStatus.PENDING);
        Mockito.when((Object)this.consentRepository.findByIdAndUserId(Long.valueOf(12L), Long.valueOf(1L))).thenReturn(Optional.of(denied));
        Assertions.assertEquals((Object)"DENIED", (Object)service.deny(Long.valueOf(1L), Long.valueOf(12L)).getStatus());
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.grant(Long.valueOf(1L), Long.valueOf(12L)));
    }

    @Test
    void doesNotExposeAnotherUsersConsent() {
        ConsentService service = new ConsentService(this.consentRepository, this.userRepository, this.applicationRepository);
        Mockito.when((Object)this.consentRepository.findByIdAndUserId(Long.valueOf(99L), Long.valueOf(1L))).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.get(Long.valueOf(1L), Long.valueOf(99L)));
    }

    private Consent consent(Long id, User user, ThirdPartyApplication app, Consent.ConsentStatus status) {
        return Consent.builder().id(id).user(user).application(app).dataType(PersonalData.DataType.EMAIL).purpose("Order Confirmation").allowedOperation(Consent.AllowedOperation.READ).status(status).requestedDurationDays(7).build();
    }
}
