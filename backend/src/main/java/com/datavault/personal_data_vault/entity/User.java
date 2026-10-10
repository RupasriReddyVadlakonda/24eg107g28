package com.datavault.personal_data_vault.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Generated;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String fullName;
    @Column(nullable=false, unique=true)
    private String email;
    @Column(nullable=false)
    private String passwordHash;
    private String phoneNumber;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false)
    private Role role;
    @Column(nullable=false)
    private boolean enabled;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Generated
    private static boolean $default$enabled() {
        return true;
    }

    @Generated
    public static UserBuilder builder() {
        return new UserBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getFullName() {
        return this.fullName;
    }

    @Generated
    public String getEmail() {
        return this.email;
    }

    @Generated
    public String getPasswordHash() {
        return this.passwordHash;
    }

    @Generated
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    @Generated
    public Role getRole() {
        return this.role;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Generated
    public void setEmail(String email) {
        this.email = email;
    }

    @Generated
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Generated
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Generated
    public void setRole(Role role) {
        this.role = role;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Generated
    public User() {
        this.enabled = User.$default$enabled();
    }

    @Generated
    public User(Long id, String fullName, String email, String passwordHash, String phoneNumber, Role role, boolean enabled, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Generated
    public static class UserBuilder {
        @Generated
        private Long id;
        @Generated
        private String fullName;
        @Generated
        private String email;
        @Generated
        private String passwordHash;
        @Generated
        private String phoneNumber;
        @Generated
        private Role role;
        @Generated
        private boolean enabled$set;
        @Generated
        private boolean enabled$value;
        @Generated
        private LocalDateTime createdAt;
        @Generated
        private LocalDateTime updatedAt;

        @Generated
        UserBuilder() {
        }

        @Generated
        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public UserBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        @Generated
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        @Generated
        public UserBuilder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        @Generated
        public UserBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        @Generated
        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        @Generated
        public UserBuilder enabled(boolean enabled) {
            this.enabled$value = enabled;
            this.enabled$set = true;
            return this;
        }

        @Generated
        public UserBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Generated
        public UserBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @Generated
        public User build() {
            boolean enabled$value = this.enabled$value;
            if (!this.enabled$set) {
                enabled$value = User.$default$enabled();
            }
            return new User(this.id, this.fullName, this.email, this.passwordHash, this.phoneNumber, this.role, enabled$value, this.createdAt, this.updatedAt);
        }

        @Generated
        public String toString() {
            return "User.UserBuilder(id=" + this.id + ", fullName=[REDACTED], email=[REDACTED], passwordHash=[REDACTED], phoneNumber=[REDACTED], role=" + String.valueOf((Object)this.role) + ", enabled$value=" + this.enabled$value + ", createdAt=" + String.valueOf(this.createdAt) + ", updatedAt=" + String.valueOf(this.updatedAt) + ")";
        }
    }

    public static enum Role {
        USER,
        ADMIN;

    }
}

