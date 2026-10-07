package com.datavault.personal_data_vault.repository;

import com.datavault.personal_data_vault.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    List<AccessLog> findByUserIdOrderByAccessedAtDesc(Long userId);
}
