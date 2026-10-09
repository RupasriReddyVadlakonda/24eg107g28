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

@Entity
@Table(name="access_logs")
public class AccessLog {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="application_id")
    private ThirdPartyApplication application;
    @Enumerated(value=EnumType.STRING)
    private PersonalData.DataType dataType;
    private String operation;
    private String purpose;
    @Column(nullable=false)
    private LocalDateTime timestamp;
    private String ipAddress;
    @Column(nullable=false)
    private boolean success;
    private String failureReason;

    @Generated
    public static AccessLogBuilder builder() {
        return new AccessLogBuilder();
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
    public String getOperation() {
        return this.operation;
    }

    @Generated
    public String getPurpose() {
        return this.purpose;
    }

    @Generated
    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    @Generated
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Generated
    public boolean isSuccess() {
        return this.success;
    }

    @Generated
    public String getFailureReason() {
        return this.failureReason;
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
    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Generated
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Generated
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Generated
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Generated
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Generated
    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    @Generated
    public AccessLog() {
    }

    @Generated
    public AccessLog(Long id, User user, ThirdPartyApplication application, PersonalData.DataType dataType, String operation, String purpose, LocalDateTime timestamp, String ipAddress, boolean success, String failureReason) {
        this.id = id;
        this.user = user;
        this.application = application;
        this.dataType = dataType;
        this.operation = operation;
        this.purpose = purpose;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
        this.success = success;
        this.failureReason = failureReason;
    }

    @Generated
    public static class AccessLogBuilder {
        @Generated
        private Long id;
        @Generated
        private User user;
        @Generated
        private ThirdPartyApplication application;
        @Generated
        private PersonalData.DataType dataType;
        @Generated
        private String operation;
        @Generated
        private String purpose;
        @Generated
        private LocalDateTime timestamp;
        @Generated
        private String ipAddress;
        @Generated
        private boolean success;
        @Generated
        private String failureReason;

        @Generated
        AccessLogBuilder() {
        }

        @Generated
        public AccessLogBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public AccessLogBuilder user(User user) {
            this.user = user;
            return this;
        }

        @Generated
        public AccessLogBuilder application(ThirdPartyApplication application) {
            this.application = application;
            return this;
        }

        @Generated
        public AccessLogBuilder dataType(PersonalData.DataType dataType) {
            this.dataType = dataType;
            return this;
        }

        @Generated
        public AccessLogBuilder operation(String operation) {
            this.operation = operation;
            return this;
        }

        @Generated
        public AccessLogBuilder purpose(String purpose) {
            this.purpose = purpose;
            return this;
        }

        @Generated
        public AccessLogBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Generated
        public AccessLogBuilder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        @Generated
        public AccessLogBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        @Generated
        public AccessLogBuilder failureReason(String failureReason) {
            this.failureReason = failureReason;
            return this;
        }

        @Generated
        public AccessLog build() {
            return new AccessLog(this.id, this.user, this.application, this.dataType, this.operation, this.purpose, this.timestamp, this.ipAddress, this.success, this.failureReason);
        }

        @Generated
        public String toString() {
            return "AccessLog.AccessLogBuilder(id=" + this.id + ", user=" + String.valueOf(this.user) + ", application=" + String.valueOf(this.application) + ", dataType=" + String.valueOf((Object)this.dataType) + ", operation=" + this.operation + ", purpose=" + this.purpose + ", timestamp=" + String.valueOf(this.timestamp) + ", ipAddress=" + this.ipAddress + ", success=" + this.success + ", failureReason=" + this.failureReason + ")";
        }
    }
}

