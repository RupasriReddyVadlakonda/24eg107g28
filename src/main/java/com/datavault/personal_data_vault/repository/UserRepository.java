package com.datavault.personal_data_vault.repository;

import com.datavault.personal_data_vault.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String var1);

    public boolean existsByEmail(String var1);
}

