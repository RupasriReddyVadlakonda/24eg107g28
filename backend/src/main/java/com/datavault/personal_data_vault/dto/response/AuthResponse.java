package com.datavault.personal_data_vault.dto.response;

import lombok.Generated;

public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String email;
    private String role;

    @Generated
    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    @Generated
    public String getAccessToken() {
        return this.accessToken;
    }

    @Generated
    public String getRefreshToken() {
        return this.refreshToken;
    }

    @Generated
    public Long getUserId() {
        return this.userId;
    }

    @Generated
    public String getEmail() {
        return this.email;
    }

    @Generated
    public String getRole() {
        return this.role;
    }

    @Generated
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Generated
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Generated
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setEmail(String email) {
        this.email = email;
    }

    @Generated
    public void setRole(String role) {
        this.role = role;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AuthResponse)) {
            return false;
        }
        AuthResponse other = (AuthResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        String this$accessToken = this.getAccessToken();
        String other$accessToken = other.getAccessToken();
        if (this$accessToken == null ? other$accessToken != null : !this$accessToken.equals(other$accessToken)) {
            return false;
        }
        String this$refreshToken = this.getRefreshToken();
        String other$refreshToken = other.getRefreshToken();
        if (this$refreshToken == null ? other$refreshToken != null : !this$refreshToken.equals(other$refreshToken)) {
            return false;
        }
        String this$email = this.getEmail();
        String other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) {
            return false;
        }
        String this$role = this.getRole();
        String other$role = other.getRole();
        return !(this$role == null ? other$role != null : !this$role.equals(other$role));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AuthResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        String $accessToken = this.getAccessToken();
        result = result * 59 + ($accessToken == null ? 43 : $accessToken.hashCode());
        String $refreshToken = this.getRefreshToken();
        result = result * 59 + ($refreshToken == null ? 43 : $refreshToken.hashCode());
        String $email = this.getEmail();
        result = result * 59 + ($email == null ? 43 : $email.hashCode());
        String $role = this.getRole();
        result = result * 59 + ($role == null ? 43 : $role.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AuthResponse(accessToken=[REDACTED], refreshToken=[REDACTED], userId=" + this.getUserId() + ", email=[REDACTED], role=" + this.getRole() + ")";
    }

    @Generated
    public AuthResponse() {
    }

    @Generated
    public AuthResponse(String accessToken, String refreshToken, Long userId, String email, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    @Generated
    public static class AuthResponseBuilder {
        @Generated
        private String accessToken;
        @Generated
        private String refreshToken;
        @Generated
        private Long userId;
        @Generated
        private String email;
        @Generated
        private String role;

        @Generated
        AuthResponseBuilder() {
        }

        @Generated
        public AuthResponseBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        @Generated
        public AuthResponseBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        @Generated
        public AuthResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public AuthResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        @Generated
        public AuthResponseBuilder role(String role) {
            this.role = role;
            return this;
        }

        @Generated
        public AuthResponse build() {
            return new AuthResponse(this.accessToken, this.refreshToken, this.userId, this.email, this.role);
        }

        @Generated
        public String toString() {
            return "AuthResponse.AuthResponseBuilder(accessToken=[REDACTED], refreshToken=[REDACTED], userId=" + this.userId + ", email=[REDACTED], role=" + this.role + ")";
        }
    }
}

