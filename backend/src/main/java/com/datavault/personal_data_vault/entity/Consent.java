package com.datavault.personal_data_vault.entity;

import com.datavault.personal_data_vault.entity.PersonalData;
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
@Table(name="consents")
public class Consent {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="application_id", nullable=false)
    private ThirdPartyApplication application;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false)
    private PersonalData.DataType dataType;
    @Column(nullable=false)
    private String purpose;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false)
    private AllowedOperation allowedOperation;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false)
    private ConsentStatus status;
    private LocalDateTime startTime;
    private LocalDateTime expirationTime;
    @Column(nullable=false)
    private int requestedDurationDays;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime revokedAt;

    @Generated
    public static ConsentBuilder builder() {
        return new ConsentBuilder();
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
    public PersonalData.DataType getDataType() {
        return this.dataType;
    }

    @Generated
    public String getPurpose() {
        return this.purpose;
    }

    @Generated
    public AllowedOperation getAllowedOperation() {
        return this.allowedOperation;
    }

    @Generated
    public ConsentStatus getStatus() {
        return this.status;
    }

    @Generated
    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    @Generated
    public LocalDateTime getExpirationTime() {
        return this.expirationTime;
    }

    @Generated
    public int getRequestedDurationDays() {
        return this.requestedDurationDays;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public LocalDateTime getRevokedAt() {
        return this.revokedAt;
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
    public void setDataType(PersonalData.DataType dataType) {
        this.dataType = dataType;
    }

    @Generated
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Generated
    public void setAllowedOperation(AllowedOperation allowedOperation) {
        this.allowedOperation = allowedOperation;
    }

    @Generated
    public void setStatus(ConsentStatus status) {
        this.status = status;
    }

    @Generated
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Generated
    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Generated
    public void setRequestedDurationDays(int requestedDurationDays) {
        this.requestedDurationDays = requestedDurationDays;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setRevokedAt(LocalDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }

    @Generated
    public Consent() {
    }

    @Generated
    public Consent(Long id, User user, ThirdPartyApplication application, PersonalData.DataType dataType, String purpose, AllowedOperation allowedOperation, ConsentStatus status, LocalDateTime startTime, LocalDateTime expirationTime, int requestedDurationDays, LocalDateTime createdAt, LocalDateTime revokedAt) {
        this.id = id;
        this.user = user;
        this.application = application;
        this.dataType = dataType;
        this.purpose = purpose;
        this.allowedOperation = allowedOperation;
        this.status = status;
        this.startTime = startTime;
        this.expirationTime = expirationTime;
        this.requestedDurationDays = requestedDurationDays;
        this.createdAt = createdAt;
        this.revokedAt = revokedAt;
    }

    @Generated
    public static class ConsentBuilder {
        @Generated
        private Long id;
        @Generated
        private User user;
        @Generated
        private ThirdPartyApplication application;
        @Generated
        private PersonalData.DataType dataType;
        @Generated
        private String purpose;
        @Generated
        private AllowedOperation allowedOperation;
        @Generated
        private ConsentStatus status;
        @Generated
        private LocalDateTime startTime;
        @Generated
        private LocalDateTime expirationTime;
        @Generated
        private int requestedDurationDays;
        @Generated
        private LocalDateTime createdAt;
        @Generated
        private LocalDateTime revokedAt;

        @Generated
        ConsentBuilder() {
        }

        @Generated
        public ConsentBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public ConsentBuilder user(User user) {
            this.user = user;
            return this;
        }

        @Generated
        public ConsentBuilder application(ThirdPartyApplication application) {
            this.application = application;
            return this;
        }

        @Generated
        public ConsentBuilder dataType(PersonalData.DataType dataType) {
            this.dataType = dataType;
            return this;
        }

        @Generated
        public ConsentBuilder purpose(String purpose) {
            this.purpose = purpose;
            return this;
        }

        @Generated
        public ConsentBuilder allowedOperation(AllowedOperation allowedOperation) {
            this.allowedOperation = allowedOperation;
            return this;
        }

        @Generated
        public ConsentBuilder status(ConsentStatus status) {
            this.status = status;
            return this;
        }

        @Generated
        public ConsentBuilder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        @Generated
        public ConsentBuilder expirationTime(LocalDateTime expirationTime) {
            this.expirationTime = expirationTime;
            return this;
        }

        @Generated
        public ConsentBuilder requestedDurationDays(int requestedDurationDays) {
            this.requestedDurationDays = requestedDurationDays;
            return this;
        }

        @Generated
        public ConsentBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Generated
        public ConsentBuilder revokedAt(LocalDateTime revokedAt) {
            this.revokedAt = revokedAt;
            return this;
        }

        @Generated
        public Consent build() {
            return new Consent(this.id, this.user, this.application, this.dataType, this.purpose, this.allowedOperation, this.status, this.startTime, this.expirationTime, this.requestedDurationDays, this.createdAt, this.revokedAt);
        }

        @Generated
        public String toString() {
            return "Consent.ConsentBuilder(id=" + this.id + ", user=" + String.valueOf(this.user) + ", application=" + String.valueOf(this.application) + ", dataType=" + String.valueOf((Object)this.dataType) + ", purpose=" + this.purpose + ", allowedOperation=" + String.valueOf((Object)this.allowedOperation) + ", status=" + String.valueOf((Object)this.status) + ", startTime=" + String.valueOf(this.startTime) + ", expirationTime=" + String.valueOf(this.expirationTime) + ", requestedDurationDays=" + this.requestedDurationDays + ", createdAt=" + String.valueOf(this.createdAt) + ", revokedAt=" + String.valueOf(this.revokedAt) + ")";
        }
    }

    public static enum AllowedOperation {
        READ,
        WRITE,
        READ_WRITE;

    }

    public static enum ConsentStatus {
        PENDING,
        GRANTED,
        DENIED,
        REVOKED,
        EXPIRED;

    }
}

