package com.datavault.personal_data_vault.repository;

import com.datavault.personal_data_vault.entity.SecurityAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityAlertRepository
extends JpaRepository<SecurityAlert, Long> {
    public Page<SecurityAlert> findByUserId(Long var1, Pageable var2);

    public Page<SecurityAlert> findAll(Pageable var1);
}

