package com.datavault.personal_data_vault.dto.response;

import com.datavault.personal_data_vault.entity.SecurityAlert;
import java.time.LocalDateTime;
import lombok.Generated;

public class SecurityAlertResponse {
    private Long id;
    private Long userId;
    private Long applicationId;
    private String alertType;
    private String severity;
    private String description;
    private LocalDateTime timestamp;
    private boolean resolved;

    public static SecurityAlertResponse from(SecurityAlert alert) {
        return SecurityAlertResponse.builder().id(alert.getId()).userId(alert.getUser() != null ? alert.getUser().getId() : null).applicationId(alert.getApplication() != null ? alert.getApplication().getId() : null).alertType(alert.getAlertType().name()).severity(alert.getSeverity().name()).description(alert.getDescription()).timestamp(alert.getTimestamp()).resolved(alert.isResolved()).build();
    }

    @Generated
    public static SecurityAlertResponseBuilder builder() {
        return new SecurityAlertResponseBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public Long getUserId() {
        return this.userId;
    }

    @Generated
    public Long getApplicationId() {
        return this.applicationId;
    }

    @Generated
    public String getAlertType() {
        return this.alertType;
    }

    @Generated
    public String getSeverity() {
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
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    @Generated
    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    @Generated
    public void setSeverity(String severity) {
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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SecurityAlertResponse)) {
            return false;
        }
        SecurityAlertResponse other = (SecurityAlertResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isResolved() != other.isResolved()) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Long this$applicationId = this.getApplicationId();
        Long other$applicationId = other.getApplicationId();
        if (this$applicationId == null ? other$applicationId != null : !((Object)this$applicationId).equals(other$applicationId)) {
            return false;
        }
        String this$alertType = this.getAlertType();
        String other$alertType = other.getAlertType();
        if (this$alertType == null ? other$alertType != null : !this$alertType.equals(other$alertType)) {
            return false;
        }
        String this$severity = this.getSeverity();
        String other$severity = other.getSeverity();
        if (this$severity == null ? other$severity != null : !this$severity.equals(other$severity)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        LocalDateTime this$timestamp = this.getTimestamp();
        LocalDateTime other$timestamp = other.getTimestamp();
        return !(this$timestamp == null ? other$timestamp != null : !((Object)this$timestamp).equals(other$timestamp));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof SecurityAlertResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isResolved() ? 79 : 97);
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Long $applicationId = this.getApplicationId();
        result = result * 59 + ($applicationId == null ? 43 : ((Object)$applicationId).hashCode());
        String $alertType = this.getAlertType();
        result = result * 59 + ($alertType == null ? 43 : $alertType.hashCode());
        String $severity = this.getSeverity();
        result = result * 59 + ($severity == null ? 43 : $severity.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        LocalDateTime $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : ((Object)$timestamp).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "SecurityAlertResponse(id=" + this.getId() + ", userId=" + this.getUserId() + ", applicationId=" + this.getApplicationId() + ", alertType=" + this.getAlertType() + ", severity=" + this.getSeverity() + ", description=" + this.getDescription() + ", timestamp=" + String.valueOf(this.getTimestamp()) + ", resolved=" + this.isResolved() + ")";
    }

    @Generated
    public SecurityAlertResponse() {
    }

    @Generated
    public SecurityAlertResponse(Long id, Long userId, Long applicationId, String alertType, String severity, String description, LocalDateTime timestamp, boolean resolved) {
        this.id = id;
        this.userId = userId;
        this.applicationId = applicationId;
        this.alertType = alertType;
        this.severity = severity;
        this.description = description;
        this.timestamp = timestamp;
        this.resolved = resolved;
    }

    @Generated
    public static class SecurityAlertResponseBuilder {
        @Generated
        private Long id;
        @Generated
        private Long userId;
        @Generated
        private Long applicationId;
        @Generated
        private String alertType;
        @Generated
        private String severity;
        @Generated
        private String description;
        @Generated
        private LocalDateTime timestamp;
        @Generated
        private boolean resolved;

        @Generated
        SecurityAlertResponseBuilder() {
        }

        @Generated
        public SecurityAlertResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public SecurityAlertResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public SecurityAlertResponseBuilder applicationId(Long applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        @Generated
        public SecurityAlertResponseBuilder alertType(String alertType) {
            this.alertType = alertType;
            return this;
        }

        @Generated
        public SecurityAlertResponseBuilder severity(String severity) {
            this.severity = severity;
            return this;
        }

        @Generated
        public SecurityAlertResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Generated
        public SecurityAlertResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Generated
        public SecurityAlertResponseBuilder resolved(boolean resolved) {
            this.resolved = resolved;
            return this;
        }

        @Generated
        public SecurityAlertResponse build() {
            return new SecurityAlertResponse(this.id, this.userId, this.applicationId, this.alertType, this.severity, this.description, this.timestamp, this.resolved);
        }

        @Generated
        public String toString() {
            return "SecurityAlertResponse.SecurityAlertResponseBuilder(id=" + this.id + ", userId=" + this.userId + ", applicationId=" + this.applicationId + ", alertType=" + this.alertType + ", severity=" + this.severity + ", description=" + this.description + ", timestamp=" + String.valueOf(this.timestamp) + ", resolved=" + this.resolved + ")";
        }
    }
}

