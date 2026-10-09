package com.datavault.personal_data_vault.repository;

import com.datavault.personal_data_vault.entity.AccessLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogRepository
extends JpaRepository<AccessLog, Long> {
    @EntityGraph(attributePaths={"user", "application"})
    public Page<AccessLog> findByUserId(Long var1, Pageable var2);

    @EntityGraph(attributePaths={"user", "application"})
    public Page<AccessLog> findAll(Pageable var1);
}

