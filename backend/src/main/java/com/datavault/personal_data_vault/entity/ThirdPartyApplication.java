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
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="third_party_applications")
public class ThirdPartyApplication {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true)
    private String applicationName;
    @Column(nullable=false, unique=true)
    private String clientId;
    @Column(nullable=false)
    private String clientSecretHash;
    @Column(columnDefinition="TEXT")
    private String description;
    @Column(length=2048)
    private String redirectUri;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="created_by_user_id")
    private User createdBy;
    @Column(nullable=false)
    private boolean active;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Generated
    private static boolean $default$active() {
        return true;
    }

    @Generated
    public static ThirdPartyApplicationBuilder builder() {
        return new ThirdPartyApplicationBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getApplicationName() {
        return this.applicationName;
    }

    @Generated
    public String getClientId() {
        return this.clientId;
    }

    @Generated
    public String getClientSecretHash() {
        return this.clientSecretHash;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getRedirectUri() {
        return this.redirectUri;
    }

    @Generated
    public User getCreatedBy() {
        return this.createdBy;
    }

    @Generated
    public boolean isActive() {
        return this.active;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Generated
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Generated
    public void setClientSecretHash(String clientSecretHash) {
        this.clientSecretHash = clientSecretHash;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    @Generated
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Generated
    public void setActive(boolean active) {
        this.active = active;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public ThirdPartyApplication() {
        this.active = ThirdPartyApplication.$default$active();
    }

    @Generated
    public ThirdPartyApplication(Long id, String applicationName, String clientId, String clientSecretHash, String description, String redirectUri, User createdBy, boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.applicationName = applicationName;
        this.clientId = clientId;
        this.clientSecretHash = clientSecretHash;
        this.description = description;
        this.redirectUri = redirectUri;
        this.createdBy = createdBy;
        this.active = active;
        this.createdAt = createdAt;
    }

    @Generated
    public static class ThirdPartyApplicationBuilder {
        @Generated
        private Long id;
        @Generated
        private String applicationName;
        @Generated
        private String clientId;
        @Generated
        private String clientSecretHash;
        @Generated
        private String description;
        @Generated
        private String redirectUri;
        @Generated
        private User createdBy;
        @Generated
        private boolean active$set;
        @Generated
        private boolean active$value;
        @Generated
        private LocalDateTime createdAt;

        @Generated
        ThirdPartyApplicationBuilder() {
        }

        @Generated
        public ThirdPartyApplicationBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public ThirdPartyApplicationBuilder applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        @Generated
        public ThirdPartyApplicationBuilder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        @Generated
        public ThirdPartyApplicationBuilder clientSecretHash(String clientSecretHash) {
            this.clientSecretHash = clientSecretHash;
            return this;
        }

        @Generated
        public ThirdPartyApplicationBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Generated
        public ThirdPartyApplicationBuilder redirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        @Generated
        public ThirdPartyApplicationBuilder createdBy(User createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        @Generated
        public ThirdPartyApplicationBuilder active(boolean active) {
            this.active$value = active;
            this.active$set = true;
            return this;
        }

        @Generated
        public ThirdPartyApplicationBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Generated
        public ThirdPartyApplication build() {
            boolean active$value = this.active$value;
            if (!this.active$set) {
                active$value = ThirdPartyApplication.$default$active();
            }
            return new ThirdPartyApplication(this.id, this.applicationName, this.clientId, this.clientSecretHash, this.description, this.redirectUri, this.createdBy, active$value, this.createdAt);
        }

        @Generated
        public String toString() {
            return "ThirdPartyApplication.ThirdPartyApplicationBuilder(id=" + this.id + ", applicationName=" + this.applicationName + ", clientId=" + this.clientId + ", clientSecretHash=[REDACTED], description=[REDACTED], redirectUri=[REDACTED], createdBy=" + String.valueOf(this.createdBy) + ", active$value=" + this.active$value + ", createdAt=" + String.valueOf(this.createdAt) + ")";
        }
    }
}

