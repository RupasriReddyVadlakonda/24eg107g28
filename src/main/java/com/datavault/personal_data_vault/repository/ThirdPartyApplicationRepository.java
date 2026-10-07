package com.datavault.personal_data_vault.repository;

import com.datavault.personal_data_vault.entity.ThirdPartyApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ThirdPartyApplicationRepository extends JpaRepository<ThirdPartyApplication, Long> {
    Optional<ThirdPartyApplication> findByClientId(String clientId);
}
