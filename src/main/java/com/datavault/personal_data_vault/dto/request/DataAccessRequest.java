package com.datavault.personal_data_vault.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Generated;

public class DataAccessRequest {
    @NotNull
    private Long userId;
    @NotBlank
    @Size(max=255)
    private @NotBlank @Size(max=255) String purpose;
    @NotBlank
    @Size(max=10000)
    private @NotBlank @Size(max=10000) String value;

    @Generated
    public DataAccessRequest() {
    }

    @Generated
    public Long getUserId() {
        return this.userId;
    }

    @Generated
    public String getPurpose() {
        return this.purpose;
    }

    @Generated
    public String getValue() {
        return this.value;
    }

    @Generated
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Generated
    public void setValue(String value) {
        this.value = value;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DataAccessRequest)) {
            return false;
        }
        DataAccessRequest other = (DataAccessRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        String this$purpose = this.getPurpose();
        String other$purpose = other.getPurpose();
        if (this$purpose == null ? other$purpose != null : !this$purpose.equals(other$purpose)) {
            return false;
        }
        String this$value = this.getValue();
        String other$value = other.getValue();
        return !(this$value == null ? other$value != null : !this$value.equals(other$value));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof DataAccessRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        String $purpose = this.getPurpose();
        result = result * 59 + ($purpose == null ? 43 : $purpose.hashCode());
        String $value = this.getValue();
        result = result * 59 + ($value == null ? 43 : $value.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "DataAccessRequest(userId=" + this.getUserId() + ", purpose=" + this.getPurpose() + ", value=" + this.getValue() + ")";
    }
}

