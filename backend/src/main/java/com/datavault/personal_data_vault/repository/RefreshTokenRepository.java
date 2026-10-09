package com.datavault.personal_data_vault.repository;

import com.datavault.personal_data_vault.entity.RefreshToken;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository
extends JpaRepository<RefreshToken, Long> {
    public Optional<RefreshToken> findByTokenHash(String var1);

    @Modifying
    @Query(value="UPDATE RefreshToken r SET r.revoked = true WHERE r.id = :id AND r.revoked = false AND r.expiresAt > :now")
    public int revokeIfActive(@Param(value="id") Long var1, @Param(value="now") LocalDateTime var2);

    @Modifying
    @Query(value="UPDATE RefreshToken r SET r.revoked = true WHERE r.user.id = :userId")
    public void revokeAllByUserId(@Param(value="userId") Long var1);
}

