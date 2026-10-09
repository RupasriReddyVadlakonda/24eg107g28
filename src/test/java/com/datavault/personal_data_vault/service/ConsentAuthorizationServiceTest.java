package com.datavault.personal_data_vault.service;

import com.datavault.personal_data_vault.entity.Consent;
import com.datavault.personal_data_vault.entity.PersonalData;
import com.datavault.personal_data_vault.entity.ThirdPartyApplication;
import com.datavault.personal_data_vault.entity.User;
import com.datavault.personal_data_vault.exception.ConsentAccessDeniedException;
import com.datavault.personal_data_vault.repository.ConsentRepository;
import com.datavault.personal_data_vault.repository.ThirdPartyApplicationRepository;
import com.datavault.personal_data_vault.service.ConsentAuthorizationService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(value={MockitoExtension.class})
class ConsentAuthorizationServiceTest {
    @Mock
    private ConsentRepository consentRepository;
    @Mock
    private ThirdPartyApplicationRepository applicationRepository;
    private ConsentAuthorizationService service;
    private final Long userId = 5L;
    private final Long appId = 8L;

    ConsentAuthorizationServiceTest() {
    }

    @BeforeEach
    void setUp() {
        this.service = new ConsentAuthorizationService(this.consentRepository, this.applicationRepository);
        Mockito.when(this.applicationRepository.findById(this.appId)).thenReturn(Optional.of(ThirdPartyApplication.builder().id(this.appId).active(true).build()));
    }

    @Test
    void allowsOnlyTheGrantedTypePurposeOperationAppAndUser() {
        Consent grant = this.grant(PersonalData.DataType.EMAIL, "OrderConfirmation", Consent.AllowedOperation.READ, LocalDateTime.now().plusDays(7L));
        Mockito.when((Object)this.consentRepository.findByUserIdAndApplicationIdAndDataType(this.userId, this.appId, PersonalData.DataType.EMAIL)).thenReturn(List.of(grant));
        Assertions.assertSame((Object)grant, (Object)this.service.authorize(this.userId, this.appId, PersonalData.DataType.EMAIL, "OrderConfirmation", Consent.AllowedOperation.READ));
        Assertions.assertFalse((boolean)this.service.isAccessAllowed(this.userId, this.appId, "PHONE", "OrderConfirmation", "READ"));
        Assertions.assertFalse((boolean)this.service.isAccessAllowed(this.userId, this.appId, "EMAIL", "Marketing", "READ"));
        Assertions.assertFalse((boolean)this.service.isAccessAllowed(this.userId, this.appId, "EMAIL", "OrderConfirmation", "WRITE"));
        Assertions.assertFalse((boolean)this.service.isAccessAllowed(Long.valueOf(6L), this.appId, "EMAIL", "OrderConfirmation", "READ"));
        Assertions.assertFalse((boolean)this.service.isAccessAllowed(this.userId, Long.valueOf(9L), "EMAIL", "OrderConfirmation", "READ"));
    }

    @Test
    void deniesExpiredConsentAndMarksItExpired() {
        Consent grant = this.grant(PersonalData.DataType.EMAIL, "OrderConfirmation", Consent.AllowedOperation.READ, LocalDateTime.now().minusSeconds(1L));
        Mockito.when((Object)this.consentRepository.findByUserIdAndApplicationIdAndDataType(this.userId, this.appId, PersonalData.DataType.EMAIL)).thenReturn(List.of(grant));
        ConsentAccessDeniedException exception = (ConsentAccessDeniedException)Assertions.assertThrows(ConsentAccessDeniedException.class, () -> this.service.authorize(this.userId, this.appId, PersonalData.DataType.EMAIL, "OrderConfirmation", Consent.AllowedOperation.READ));
        Assertions.assertEquals((Object)"CONSENT_EXPIRED", (Object)exception.getErrorCode());
        Assertions.assertEquals((Object)Consent.ConsentStatus.EXPIRED, (Object)grant.getStatus());
    }

    @Test
    void readWriteGrantAllowsBothIndividualOperations() {
        Consent grant = this.grant(PersonalData.DataType.EMAIL, "OrderConfirmation", Consent.AllowedOperation.READ_WRITE, LocalDateTime.now().plusDays(1L));
        Mockito.when((Object)this.consentRepository.findByUserIdAndApplicationIdAndDataType(this.userId, this.appId, PersonalData.DataType.EMAIL)).thenReturn(List.of(grant));
        Assertions.assertTrue((boolean)this.service.isAccessAllowed(this.userId, this.appId, "EMAIL", "OrderConfirmation", "READ"));
        Assertions.assertTrue((boolean)this.service.isAccessAllowed(this.userId, this.appId, "EMAIL", "OrderConfirmation", "WRITE"));
        Assertions.assertTrue((boolean)this.service.isAccessAllowed(this.userId, this.appId, "EMAIL", "OrderConfirmation", "READ_WRITE"));
    }

    private Consent grant(PersonalData.DataType type, String purpose, Consent.AllowedOperation operation, LocalDateTime expires) {
        return Consent.builder().user(User.builder().id(this.userId).build()).application(ThirdPartyApplication.builder().id(this.appId).active(true).build()).dataType(type).purpose(purpose).allowedOperation(operation).status(Consent.ConsentStatus.GRANTED).startTime(LocalDateTime.now().minusMinutes(1L)).expirationTime(expires).requestedDurationDays(7).build();
    }
}
