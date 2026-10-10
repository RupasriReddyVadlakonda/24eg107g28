package com.datavault.personal_data_vault.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Generated;

public class AppTokenRequest {
    @NotBlank
    private String clientId;
    @NotBlank
    private String clientSecret;

    @Generated
    public AppTokenRequest() {
    }

    @Generated
    public String getClientId() {
        return this.clientId;
    }

    @Generated
    public String getClientSecret() {
        return this.clientSecret;
    }

    @Generated
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Generated
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AppTokenRequest)) {
            return false;
        }
        AppTokenRequest other = (AppTokenRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$clientId = this.getClientId();
        String other$clientId = other.getClientId();
        if (this$clientId == null ? other$clientId != null : !this$clientId.equals(other$clientId)) {
            return false;
        }
        String this$clientSecret = this.getClientSecret();
        String other$clientSecret = other.getClientSecret();
        return !(this$clientSecret == null ? other$clientSecret != null : !this$clientSecret.equals(other$clientSecret));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AppTokenRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $clientId = this.getClientId();
        result = result * 59 + ($clientId == null ? 43 : $clientId.hashCode());
        String $clientSecret = this.getClientSecret();
        result = result * 59 + ($clientSecret == null ? 43 : $clientSecret.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AppTokenRequest(clientId=" + this.getClientId() + ", clientSecret=[REDACTED])";
    }
}

