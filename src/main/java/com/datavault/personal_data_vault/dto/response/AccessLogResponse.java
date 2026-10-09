package com.datavault.personal_data_vault.dto.response;

import com.datavault.personal_data_vault.entity.AccessLog;
import java.time.LocalDateTime;
import lombok.Generated;

public class AccessLogResponse {
    private Long id;
    private Long userId;
    private Long applicationId;
    private String applicationName;
    private String dataType;
    private String operation;
    private String purpose;
    private LocalDateTime timestamp;
    private String ipAddress;
    private boolean success;
    private String failureReason;

    public static AccessLogResponse from(AccessLog log) {
        return AccessLogResponse.builder().id(log.getId()).userId(log.getUser().getId()).applicationId(log.getApplication() != null ? log.getApplication().getId() : null).applicationName(log.getApplication() != null ? log.getApplication().getApplicationName() : null).dataType(log.getDataType() != null ? log.getDataType().name() : null).operation(log.getOperation()).purpose(log.getPurpose()).timestamp(log.getTimestamp()).ipAddress(log.getIpAddress()).success(log.isSuccess()).failureReason(log.getFailureReason()).build();
    }

    @Generated
    public static AccessLogResponseBuilder builder() {
        return new AccessLogResponseBuilder();
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
    public String getApplicationName() {
        return this.applicationName;
    }

    @Generated
    public String getDataType() {
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
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    @Generated
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Generated
    public void setDataType(String dataType) {
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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AccessLogResponse)) {
            return false;
        }
        AccessLogResponse other = (AccessLogResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isSuccess() != other.isSuccess()) {
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
        String this$applicationName = this.getApplicationName();
        String other$applicationName = other.getApplicationName();
        if (this$applicationName == null ? other$applicationName != null : !this$applicationName.equals(other$applicationName)) {
            return false;
        }
        String this$dataType = this.getDataType();
        String other$dataType = other.getDataType();
        if (this$dataType == null ? other$dataType != null : !this$dataType.equals(other$dataType)) {
            return false;
        }
        String this$operation = this.getOperation();
        String other$operation = other.getOperation();
        if (this$operation == null ? other$operation != null : !this$operation.equals(other$operation)) {
            return false;
        }
        String this$purpose = this.getPurpose();
        String other$purpose = other.getPurpose();
        if (this$purpose == null ? other$purpose != null : !this$purpose.equals(other$purpose)) {
            return false;
        }
        LocalDateTime this$timestamp = this.getTimestamp();
        LocalDateTime other$timestamp = other.getTimestamp();
        if (this$timestamp == null ? other$timestamp != null : !((Object)this$timestamp).equals(other$timestamp)) {
            return false;
        }
        String this$ipAddress = this.getIpAddress();
        String other$ipAddress = other.getIpAddress();
        if (this$ipAddress == null ? other$ipAddress != null : !this$ipAddress.equals(other$ipAddress)) {
            return false;
        }
        String this$failureReason = this.getFailureReason();
        String other$failureReason = other.getFailureReason();
        return !(this$failureReason == null ? other$failureReason != null : !this$failureReason.equals(other$failureReason));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AccessLogResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isSuccess() ? 79 : 97);
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Long $applicationId = this.getApplicationId();
        result = result * 59 + ($applicationId == null ? 43 : ((Object)$applicationId).hashCode());
        String $applicationName = this.getApplicationName();
        result = result * 59 + ($applicationName == null ? 43 : $applicationName.hashCode());
        String $dataType = this.getDataType();
        result = result * 59 + ($dataType == null ? 43 : $dataType.hashCode());
        String $operation = this.getOperation();
        result = result * 59 + ($operation == null ? 43 : $operation.hashCode());
        String $purpose = this.getPurpose();
        result = result * 59 + ($purpose == null ? 43 : $purpose.hashCode());
        LocalDateTime $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : ((Object)$timestamp).hashCode());
        String $ipAddress = this.getIpAddress();
        result = result * 59 + ($ipAddress == null ? 43 : $ipAddress.hashCode());
        String $failureReason = this.getFailureReason();
        result = result * 59 + ($failureReason == null ? 43 : $failureReason.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AccessLogResponse(id=" + this.getId() + ", userId=" + this.getUserId() + ", applicationId=" + this.getApplicationId() + ", applicationName=" + this.getApplicationName() + ", dataType=" + this.getDataType() + ", operation=" + this.getOperation() + ", purpose=" + this.getPurpose() + ", timestamp=" + String.valueOf(this.getTimestamp()) + ", ipAddress=" + this.getIpAddress() + ", success=" + this.isSuccess() + ", failureReason=" + this.getFailureReason() + ")";
    }

    @Generated
    public AccessLogResponse() {
    }

    @Generated
    public AccessLogResponse(Long id, Long userId, Long applicationId, String applicationName, String dataType, String operation, String purpose, LocalDateTime timestamp, String ipAddress, boolean success, String failureReason) {
        this.id = id;
        this.userId = userId;
        this.applicationId = applicationId;
        this.applicationName = applicationName;
        this.dataType = dataType;
        this.operation = operation;
        this.purpose = purpose;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
        this.success = success;
        this.failureReason = failureReason;
    }

    @Generated
    public static class AccessLogResponseBuilder {
        @Generated
        private Long id;
        @Generated
        private Long userId;
        @Generated
        private Long applicationId;
        @Generated
        private String applicationName;
        @Generated
        private String dataType;
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
        AccessLogResponseBuilder() {
        }

        @Generated
        public AccessLogResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public AccessLogResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public AccessLogResponseBuilder applicationId(Long applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        @Generated
        public AccessLogResponseBuilder applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        @Generated
        public AccessLogResponseBuilder dataType(String dataType) {
            this.dataType = dataType;
            return this;
        }

        @Generated
        public AccessLogResponseBuilder operation(String operation) {
            this.operation = operation;
            return this;
        }

        @Generated
        public AccessLogResponseBuilder purpose(String purpose) {
            this.purpose = purpose;
            return this;
        }

        @Generated
        public AccessLogResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Generated
        public AccessLogResponseBuilder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        @Generated
        public AccessLogResponseBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        @Generated
        public AccessLogResponseBuilder failureReason(String failureReason) {
            this.failureReason = failureReason;
            return this;
        }

        @Generated
        public AccessLogResponse build() {
            return new AccessLogResponse(this.id, this.userId, this.applicationId, this.applicationName, this.dataType, this.operation, this.purpose, this.timestamp, this.ipAddress, this.success, this.failureReason);
        }

        @Generated
        public String toString() {
            return "AccessLogResponse.AccessLogResponseBuilder(id=" + this.id + ", userId=" + this.userId + ", applicationId=" + this.applicationId + ", applicationName=" + this.applicationName + ", dataType=" + this.dataType + ", operation=" + this.operation + ", purpose=" + this.purpose + ", timestamp=" + String.valueOf(this.timestamp) + ", ipAddress=" + this.ipAddress + ", success=" + this.success + ", failureReason=" + this.failureReason + ")";
        }
    }
}

