package com.datavault.personal_data_vault.repository;

import com.datavault.personal_data_vault.entity.Consent;
import com.datavault.personal_data_vault.entity.PersonalData;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConsentRepository
extends JpaRepository<Consent, Long> {
    public List<Consent> findByUserId(Long var1);

    public List<Consent> findByApplicationId(Long var1);

    public List<Consent> findByUserIdAndApplicationIdAndDataType(Long var1, Long var2, PersonalData.DataType var3);

    public Optional<Consent> findByIdAndUserId(Long var1, Long var2);

    @Query(value="SELECT c FROM Consent c WHERE c.user.id = :userId AND c.application.id = :appId AND c.dataType = :dataType AND c.purpose = :purpose AND c.status = :status AND (c.expirationTime IS NULL OR c.expirationTime > :now)")
    public Optional<Consent> findActiveConsent(@Param(value="userId") Long var1, @Param(value="appId") Long var2, @Param(value="dataType") PersonalData.DataType var3, @Param(value="purpose") String var4, @Param(value="status") Consent.ConsentStatus var5, @Param(value="now") LocalDateTime var6);

    @Query(value="SELECT c FROM Consent c WHERE c.status = 'GRANTED' AND c.expirationTime < :now")
    public List<Consent> findExpiredConsents(@Param(value="now") LocalDateTime var1);
}

