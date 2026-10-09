package com.datavault.personal_data_vault.repository;

import com.datavault.personal_data_vault.entity.ThirdPartyApplication;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdPartyApplicationRepository
extends JpaRepository<ThirdPartyApplication, Long> {
    public Optional<ThirdPartyApplication> findByClientId(String var1);

    public Optional<ThirdPartyApplication> findByIdAndCreatedById(Long var1, Long var2);

    public List<ThirdPartyApplication> findByCreatedById(Long var1);

    public boolean existsByClientId(String var1);

    public boolean existsByApplicationName(String var1);

    public boolean existsByApplicationNameAndIdNot(String var1, Long var2);
}

