package com.datavault.personal_data_vault.dto.response;

import com.datavault.personal_data_vault.entity.Consent;
import java.time.LocalDateTime;
import lombok.Generated;

public class ConsentResponse {
    private Long id;
    private Long userId;
    private Long applicationId;
    private String applicationName;
    private String dataType;
    private String purpose;
    private String allowedOperation;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime expirationTime;
    private Integer requestedDurationDays;
    private LocalDateTime createdAt;
    private LocalDateTime revokedAt;

    public static ConsentResponse from(Consent consent) {
        boolean expired = consent.getStatus() == Consent.ConsentStatus.GRANTED && consent.getExpirationTime() != null && !consent.getExpirationTime().isAfter(LocalDateTime.now());
        return ConsentResponse.builder().id(consent.getId()).userId(consent.getUser().getId()).applicationId(consent.getApplication().getId()).applicationName(consent.getApplication().getApplicationName()).dataType(consent.getDataType().name()).purpose(consent.getPurpose()).allowedOperation(consent.getAllowedOperation().name()).status(expired ? Consent.ConsentStatus.EXPIRED.name() : consent.getStatus().name()).startTime(consent.getStartTime()).expirationTime(consent.getExpirationTime()).requestedDurationDays(consent.getRequestedDurationDays()).createdAt(consent.getCreatedAt()).revokedAt(consent.getRevokedAt()).build();
    }

    @Generated
    public static ConsentResponseBuilder builder() {
        return new ConsentResponseBuilder();
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
    public String getPurpose() {
        return this.purpose;
    }

    @Generated
    public String getAllowedOperation() {
        return this.allowedOperation;
    }

    @Generated
    public String getStatus() {
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
    public Integer getRequestedDurationDays() {
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
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Generated
    public void setAllowedOperation(String allowedOperation) {
        this.allowedOperation = allowedOperation;
    }

    @Generated
    public void setStatus(String status) {
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
    public void setRequestedDurationDays(Integer requestedDurationDays) {
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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ConsentResponse)) {
            return false;
        }
        ConsentResponse other = (ConsentResponse)o;
        if (!other.canEqual(this)) {
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
        Integer this$requestedDurationDays = this.getRequestedDurationDays();
        Integer other$requestedDurationDays = other.getRequestedDurationDays();
        if (this$requestedDurationDays == null ? other$requestedDurationDays != null : !((Object)this$requestedDurationDays).equals(other$requestedDurationDays)) {
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
        String this$purpose = this.getPurpose();
        String other$purpose = other.getPurpose();
        if (this$purpose == null ? other$purpose != null : !this$purpose.equals(other$purpose)) {
            return false;
        }
        String this$allowedOperation = this.getAllowedOperation();
        String other$allowedOperation = other.getAllowedOperation();
        if (this$allowedOperation == null ? other$allowedOperation != null : !this$allowedOperation.equals(other$allowedOperation)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        LocalDateTime this$startTime = this.getStartTime();
        LocalDateTime other$startTime = other.getStartTime();
        if (this$startTime == null ? other$startTime != null : !((Object)this$startTime).equals(other$startTime)) {
            return false;
        }
        LocalDateTime this$expirationTime = this.getExpirationTime();
        LocalDateTime other$expirationTime = other.getExpirationTime();
        if (this$expirationTime == null ? other$expirationTime != null : !((Object)this$expirationTime).equals(other$expirationTime)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt)) {
            return false;
        }
        LocalDateTime this$revokedAt = this.getRevokedAt();
        LocalDateTime other$revokedAt = other.getRevokedAt();
        return !(this$revokedAt == null ? other$revokedAt != null : !((Object)this$revokedAt).equals(other$revokedAt));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ConsentResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Long $applicationId = this.getApplicationId();
        result = result * 59 + ($applicationId == null ? 43 : ((Object)$applicationId).hashCode());
        Integer $requestedDurationDays = this.getRequestedDurationDays();
        result = result * 59 + ($requestedDurationDays == null ? 43 : ((Object)$requestedDurationDays).hashCode());
        String $applicationName = this.getApplicationName();
        result = result * 59 + ($applicationName == null ? 43 : $applicationName.hashCode());
        String $dataType = this.getDataType();
        result = result * 59 + ($dataType == null ? 43 : $dataType.hashCode());
        String $purpose = this.getPurpose();
        result = result * 59 + ($purpose == null ? 43 : $purpose.hashCode());
        String $allowedOperation = this.getAllowedOperation();
        result = result * 59 + ($allowedOperation == null ? 43 : $allowedOperation.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        LocalDateTime $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : ((Object)$startTime).hashCode());
        LocalDateTime $expirationTime = this.getExpirationTime();
        result = result * 59 + ($expirationTime == null ? 43 : ((Object)$expirationTime).hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        LocalDateTime $revokedAt = this.getRevokedAt();
        result = result * 59 + ($revokedAt == null ? 43 : ((Object)$revokedAt).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ConsentResponse(id=" + this.getId() + ", userId=" + this.getUserId() + ", applicationId=" + this.getApplicationId() + ", applicationName=" + this.getApplicationName() + ", dataType=" + this.getDataType() + ", purpose=" + this.getPurpose() + ", allowedOperation=" + this.getAllowedOperation() + ", status=" + this.getStatus() + ", startTime=" + String.valueOf(this.getStartTime()) + ", expirationTime=" + String.valueOf(this.getExpirationTime()) + ", requestedDurationDays=" + this.getRequestedDurationDays() + ", createdAt=" + String.valueOf(this.getCreatedAt()) + ", revokedAt=" + String.valueOf(this.getRevokedAt()) + ")";
    }

    @Generated
    public ConsentResponse() {
    }

    @Generated
    public ConsentResponse(Long id, Long userId, Long applicationId, String applicationName, String dataType, String purpose, String allowedOperation, String status, LocalDateTime startTime, LocalDateTime expirationTime, Integer requestedDurationDays, LocalDateTime createdAt, LocalDateTime revokedAt) {
        this.id = id;
        this.userId = userId;
        this.applicationId = applicationId;
        this.applicationName = applicationName;
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
    public static class ConsentResponseBuilder {
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
        private String purpose;
        @Generated
        private String allowedOperation;
        @Generated
        private String status;
        @Generated
        private LocalDateTime startTime;
        @Generated
        private LocalDateTime expirationTime;
        @Generated
        private Integer requestedDurationDays;
        @Generated
        private LocalDateTime createdAt;
        @Generated
        private LocalDateTime revokedAt;

        @Generated
        ConsentResponseBuilder() {
        }

        @Generated
        public ConsentResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public ConsentResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public ConsentResponseBuilder applicationId(Long applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        @Generated
        public ConsentResponseBuilder applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        @Generated
        public ConsentResponseBuilder dataType(String dataType) {
            this.dataType = dataType;
            return this;
        }

        @Generated
        public ConsentResponseBuilder purpose(String purpose) {
            this.purpose = purpose;
            return this;
        }

        @Generated
        public ConsentResponseBuilder allowedOperation(String allowedOperation) {
            this.allowedOperation = allowedOperation;
            return this;
        }

        @Generated
        public ConsentResponseBuilder status(String status) {
            this.status = status;
            return this;
        }

        @Generated
        public ConsentResponseBuilder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        @Generated
        public ConsentResponseBuilder expirationTime(LocalDateTime expirationTime) {
            this.expirationTime = expirationTime;
            return this;
        }

        @Generated
        public ConsentResponseBuilder requestedDurationDays(Integer requestedDurationDays) {
            this.requestedDurationDays = requestedDurationDays;
            return this;
        }

        @Generated
        public ConsentResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Generated
        public ConsentResponseBuilder revokedAt(LocalDateTime revokedAt) {
            this.revokedAt = revokedAt;
            return this;
        }

        @Generated
        public ConsentResponse build() {
            return new ConsentResponse(this.id, this.userId, this.applicationId, this.applicationName, this.dataType, this.purpose, this.allowedOperation, this.status, this.startTime, this.expirationTime, this.requestedDurationDays, this.createdAt, this.revokedAt);
        }

        @Generated
        public String toString() {
            return "ConsentResponse.ConsentResponseBuilder(id=" + this.id + ", userId=" + this.userId + ", applicationId=" + this.applicationId + ", applicationName=" + this.applicationName + ", dataType=" + this.dataType + ", purpose=" + this.purpose + ", allowedOperation=" + this.allowedOperation + ", status=" + this.status + ", startTime=" + String.valueOf(this.startTime) + ", expirationTime=" + String.valueOf(this.expirationTime) + ", requestedDurationDays=" + this.requestedDurationDays + ", createdAt=" + String.valueOf(this.createdAt) + ", revokedAt=" + String.valueOf(this.revokedAt) + ")";
        }
    }
}

