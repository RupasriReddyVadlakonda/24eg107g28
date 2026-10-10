package com.datavault.personal_data_vault.dto.response;

import lombok.Generated;

public class AppTokenResponse {
    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private Long applicationId;

    @Generated
    public static AppTokenResponseBuilder builder() {
        return new AppTokenResponseBuilder();
    }

    @Generated
    public String getAccessToken() {
        return this.accessToken;
    }

    @Generated
    public String getTokenType() {
        return this.tokenType;
    }

    @Generated
    public long getExpiresIn() {
        return this.expiresIn;
    }

    @Generated
    public Long getApplicationId() {
        return this.applicationId;
    }

    @Generated
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Generated
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Generated
    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Generated
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AppTokenResponse)) {
            return false;
        }
        AppTokenResponse other = (AppTokenResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getExpiresIn() != other.getExpiresIn()) {
            return false;
        }
        Long this$applicationId = this.getApplicationId();
        Long other$applicationId = other.getApplicationId();
        if (this$applicationId == null ? other$applicationId != null : !((Object)this$applicationId).equals(other$applicationId)) {
            return false;
        }
        String this$accessToken = this.getAccessToken();
        String other$accessToken = other.getAccessToken();
        if (this$accessToken == null ? other$accessToken != null : !this$accessToken.equals(other$accessToken)) {
            return false;
        }
        String this$tokenType = this.getTokenType();
        String other$tokenType = other.getTokenType();
        return !(this$tokenType == null ? other$tokenType != null : !this$tokenType.equals(other$tokenType));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AppTokenResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $expiresIn = this.getExpiresIn();
        result = result * 59 + (int)($expiresIn >>> 32 ^ $expiresIn);
        Long $applicationId = this.getApplicationId();
        result = result * 59 + ($applicationId == null ? 43 : ((Object)$applicationId).hashCode());
        String $accessToken = this.getAccessToken();
        result = result * 59 + ($accessToken == null ? 43 : $accessToken.hashCode());
        String $tokenType = this.getTokenType();
        result = result * 59 + ($tokenType == null ? 43 : $tokenType.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AppTokenResponse(accessToken=[REDACTED], tokenType=" + this.getTokenType() + ", expiresIn=" + this.getExpiresIn() + ", applicationId=" + this.getApplicationId() + ")";
    }

    @Generated
    public AppTokenResponse() {
    }

    @Generated
    public AppTokenResponse(String accessToken, String tokenType, long expiresIn, Long applicationId) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.applicationId = applicationId;
    }

    @Generated
    public static class AppTokenResponseBuilder {
        @Generated
        private String accessToken;
        @Generated
        private String tokenType;
        @Generated
        private long expiresIn;
        @Generated
        private Long applicationId;

        @Generated
        AppTokenResponseBuilder() {
        }

        @Generated
        public AppTokenResponseBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        @Generated
        public AppTokenResponseBuilder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        @Generated
        public AppTokenResponseBuilder expiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        @Generated
        public AppTokenResponseBuilder applicationId(Long applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        @Generated
        public AppTokenResponse build() {
            return new AppTokenResponse(this.accessToken, this.tokenType, this.expiresIn, this.applicationId);
        }

        @Generated
        public String toString() {
            return "AppTokenResponse.AppTokenResponseBuilder(accessToken=[REDACTED], tokenType=" + this.tokenType + ", expiresIn=" + this.expiresIn + ", applicationId=" + this.applicationId + ")";
        }
    }
}

