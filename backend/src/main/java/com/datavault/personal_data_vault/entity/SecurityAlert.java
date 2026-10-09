package com.datavault.personal_data_vault.entity;

import com.datavault.personal_data_vault.entity.ThirdPartyApplication;
import com.datavault.personal_data_vault.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name="security_alerts")
public class SecurityAlert {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="application_id")
    private ThirdPartyApplication application;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false)
    private AlertType alertType;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false)
    private Severity severity;
    @Column(nullable=false, columnDefinition="TEXT")
    private String description;
    @CreationTimestamp
    private LocalDateTime timestamp;
    @Column(nullable=false)
    private boolean resolved;

    @Generated
    private static boolean $default$resolved() {
        return false;
    }

    @Generated
    public static SecurityAlertBuilder builder() {
        return new SecurityAlertBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public User getUser() {
        return this.user;
    }

    @Generated
    public ThirdPartyApplication getApplication() {
        return this.application;
    }

    @Generated
    public AlertType getAlertType() {
        return this.alertType;
    }

    @Generated
    public Severity getSeverity() {
        return this.severity;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    @Generated
    public boolean isResolved() {
        return this.resolved;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setUser(User user) {
        this.user = user;
    }

    @Generated
    public void setApplication(ThirdPartyApplication application) {
        this.application = application;
    }

    @Generated
    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    @Generated
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Generated
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    @Generated
    public SecurityAlert() {
        this.resolved = SecurityAlert.$default$resolved();
    }

    @Generated
    public SecurityAlert(Long id, User user, ThirdPartyApplication application, AlertType alertType, Severity severity, String description, LocalDateTime timestamp, boolean resolved) {
        this.id = id;
        this.user = user;
        this.application = application;
        this.alertType = alertType;
        this.severity = severity;
        this.description = description;
        this.timestamp = timestamp;
        this.resolved = resolved;
    }

    @Generated
    public static class SecurityAlertBuilder {
        @Generated
        private Long id;
        @Generated
        private User user;
        @Generated
        private ThirdPartyApplication application;
        @Generated
        private AlertType alertType;
        @Generated
        private Severity severity;
        @Generated
        private String description;
        @Generated
        private LocalDateTime timestamp;
        @Generated
        private boolean resolved$set;
        @Generated
        private boolean resolved$value;

        @Generated
        SecurityAlertBuilder() {
        }

        @Generated
        public SecurityAlertBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public SecurityAlertBuilder user(User user) {
            this.user = user;
            return this;
        }

        @Generated
        public SecurityAlertBuilder application(ThirdPartyApplication application) {
            this.application = application;
            return this;
        }

        @Generated
        public SecurityAlertBuilder alertType(AlertType alertType) {
            this.alertType = alertType;
            return this;
        }

        @Generated
        public SecurityAlertBuilder severity(Severity severity) {
            this.severity = severity;
            return this;
        }

        @Generated
        public SecurityAlertBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Generated
        public SecurityAlertBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Generated
        public SecurityAlertBuilder resolved(boolean resolved) {
            this.resolved$value = resolved;
            this.resolved$set = true;
            return this;
        }

        @Generated
        public SecurityAlert build() {
            boolean resolved$value = this.resolved$value;
            if (!this.resolved$set) {
                resolved$value = SecurityAlert.$default$resolved();
            }
            return new SecurityAlert(this.id, this.user, this.application, this.alertType, this.severity, this.description, this.timestamp, resolved$value);
        }

        @Generated
        public String toString() {
            return "SecurityAlert.SecurityAlertBuilder(id=" + this.id + ", user=" + String.valueOf(this.user) + ", application=" + String.valueOf(this.application) + ", alertType=" + String.valueOf((Object)this.alertType) + ", severity=" + String.valueOf((Object)this.severity) + ", description=" + this.description + ", timestamp=" + String.valueOf(this.timestamp) + ", resolved$value=" + this.resolved$value + ")";
        }
    }

    public static enum AlertType {
        EXCESSIVE_UNAUTHORIZED_ACCESS,
        EXPIRED_CONSENT_ACCESS_ATTEMPT,
        UNUSUAL_DATA_TYPE_ACCESS,
        RATE_LIMIT_EXCEEDED,
        REPEATED_UNAUTHORIZED_ACCESS;

    }

    public static enum Severity {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL;

    }
}

