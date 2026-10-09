package com.datavault.personal_data_vault.entity;

import com.datavault.personal_data_vault.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Generated;

@Entity
@Table(name="refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="token", nullable=false, unique=true, length=64)
    private String tokenHash;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    @Column(nullable=false)
    private LocalDateTime expiresAt;
    @Column(nullable=false)
    private boolean revoked;

    @Generated
    private static boolean $default$revoked() {
        return false;
    }

    @Generated
    public static RefreshTokenBuilder builder() {
        return new RefreshTokenBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getTokenHash() {
        return this.tokenHash;
    }

    @Generated
    public User getUser() {
        return this.user;
    }

    @Generated
    public LocalDateTime getExpiresAt() {
        return this.expiresAt;
    }

    @Generated
    public boolean isRevoked() {
        return this.revoked;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    @Generated
    public void setUser(User user) {
        this.user = user;
    }

    @Generated
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Generated
    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    @Generated
    public RefreshToken() {
        this.revoked = RefreshToken.$default$revoked();
    }

    @Generated
    public RefreshToken(Long id, String tokenHash, User user, LocalDateTime expiresAt, boolean revoked) {
        this.id = id;
        this.tokenHash = tokenHash;
        this.user = user;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
    }

    @Generated
    public static class RefreshTokenBuilder {
        @Generated
        private Long id;
        @Generated
        private String tokenHash;
        @Generated
        private User user;
        @Generated
        private LocalDateTime expiresAt;
        @Generated
        private boolean revoked$set;
        @Generated
        private boolean revoked$value;

        @Generated
        RefreshTokenBuilder() {
        }

        @Generated
        public RefreshTokenBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public RefreshTokenBuilder tokenHash(String tokenHash) {
            this.tokenHash = tokenHash;
            return this;
        }

        @Generated
        public RefreshTokenBuilder user(User user) {
            this.user = user;
            return this;
        }

        @Generated
        public RefreshTokenBuilder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        @Generated
        public RefreshTokenBuilder revoked(boolean revoked) {
            this.revoked$value = revoked;
            this.revoked$set = true;
            return this;
        }

        @Generated
        public RefreshToken build() {
            boolean revoked$value = this.revoked$value;
            if (!this.revoked$set) {
                revoked$value = RefreshToken.$default$revoked();
            }
            return new RefreshToken(this.id, this.tokenHash, this.user, this.expiresAt, revoked$value);
        }

        @Generated
        public String toString() {
            return "RefreshToken.RefreshTokenBuilder(id=" + this.id + ", tokenHash=" + this.tokenHash + ", user=" + String.valueOf(this.user) + ", expiresAt=" + String.valueOf(this.expiresAt) + ", revoked$value=" + this.revoked$value + ")";
        }
    }
}

