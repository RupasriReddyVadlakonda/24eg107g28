package com.datavault.personal_data_vault.dto.request;

import com.datavault.personal_data_vault.entity.Consent;
import com.datavault.personal_data_vault.entity.PersonalData;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Generated;

public class ConsentRequest {
    @NotNull
    private Long applicationId;
    @Positive
    private Long userId;
    @NotNull
    private PersonalData.DataType dataType;
    @NotBlank
    @Size(max=255)
    private @NotBlank @Size(max=255) String purpose;
    @NotNull
    private Consent.AllowedOperation operation;
    @NotNull
    @Min(value=1L)
    @Max(value=365L)
    private @NotNull @Min(value=1L) @Max(value=365L) Integer requestedDurationDays;

    @Generated
    public ConsentRequest() {
    }

    @Generated
    public Long getApplicationId() {
        return this.applicationId;
    }

    @Generated
    public Long getUserId() {
        return this.userId;
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
    public Consent.AllowedOperation getOperation() {
        return this.operation;
    }

    @Generated
    public Integer getRequestedDurationDays() {
        return this.requestedDurationDays;
    }

    @Generated
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    @Generated
    public void setUserId(Long userId) {
        this.userId = userId;
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
    public void setOperation(Consent.AllowedOperation operation) {
        this.operation = operation;
    }

    @Generated
    public void setRequestedDurationDays(Integer requestedDurationDays) {
        this.requestedDurationDays = requestedDurationDays;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ConsentRequest)) {
            return false;
        }
        ConsentRequest other = (ConsentRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$applicationId = this.getApplicationId();
        Long other$applicationId = other.getApplicationId();
        if (this$applicationId == null ? other$applicationId != null : !((Object)this$applicationId).equals(other$applicationId)) {
            return false;
        }
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$requestedDurationDays = this.getRequestedDurationDays();
        Integer other$requestedDurationDays = other.getRequestedDurationDays();
        if (this$requestedDurationDays == null ? other$requestedDurationDays != null : !((Object)this$requestedDurationDays).equals(other$requestedDurationDays)) {
            return false;
        }
        PersonalData.DataType this$dataType = this.getDataType();
        PersonalData.DataType other$dataType = other.getDataType();
        if (this$dataType == null ? other$dataType != null : !((Object)((Object)this$dataType)).equals((Object)other$dataType)) {
            return false;
        }
        String this$purpose = this.getPurpose();
        String other$purpose = other.getPurpose();
        if (this$purpose == null ? other$purpose != null : !this$purpose.equals(other$purpose)) {
            return false;
        }
        Consent.AllowedOperation this$operation = this.getOperation();
        Consent.AllowedOperation other$operation = other.getOperation();
        return !(this$operation == null ? other$operation != null : !((Object)((Object)this$operation)).equals((Object)other$operation));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ConsentRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $applicationId = this.getApplicationId();
        result = result * 59 + ($applicationId == null ? 43 : ((Object)$applicationId).hashCode());
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $requestedDurationDays = this.getRequestedDurationDays();
        result = result * 59 + ($requestedDurationDays == null ? 43 : ((Object)$requestedDurationDays).hashCode());
        PersonalData.DataType $dataType = this.getDataType();
        result = result * 59 + ($dataType == null ? 43 : ((Object)((Object)$dataType)).hashCode());
        String $purpose = this.getPurpose();
        result = result * 59 + ($purpose == null ? 43 : $purpose.hashCode());
        Consent.AllowedOperation $operation = this.getOperation();
        result = result * 59 + ($operation == null ? 43 : ((Object)((Object)$operation)).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ConsentRequest(applicationId=" + this.getApplicationId() + ", userId=" + this.getUserId() + ", dataType=" + String.valueOf((Object)this.getDataType()) + ", purpose=" + this.getPurpose() + ", operation=" + String.valueOf((Object)this.getOperation()) + ", requestedDurationDays=" + this.getRequestedDurationDays() + ")";
    }
}

