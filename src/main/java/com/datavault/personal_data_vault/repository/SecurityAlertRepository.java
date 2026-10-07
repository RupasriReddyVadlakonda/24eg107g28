package com.datavault.personal_data_vault.repository;

import com.datavault.personal_data_vault.entity.SecurityAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SecurityAlertRepository extends JpaRepository<SecurityAlert, Long> {
    List<SecurityAlert> findByUserIdOrderByCreatedAtDesc(Long userId);
}
