package com.datavault.personal_data_vault.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Generated;

public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;

    @Generated
    public RefreshTokenRequest() {
    }

    @Generated
    public String getRefreshToken() {
        return this.refreshToken;
    }

    @Generated
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RefreshTokenRequest)) {
            return false;
        }
        RefreshTokenRequest other = (RefreshTokenRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$refreshToken = this.getRefreshToken();
        String other$refreshToken = other.getRefreshToken();
        return !(this$refreshToken == null ? other$refreshToken != null : !this$refreshToken.equals(other$refreshToken));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof RefreshTokenRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $refreshToken = this.getRefreshToken();
        result = result * 59 + ($refreshToken == null ? 43 : $refreshToken.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "RefreshTokenRequest(refreshToken=" + this.getRefreshToken() + ")";
    }
}

