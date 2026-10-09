package com.datavault.personal_data_vault.dto.response;

import lombok.Generated;

public class AppRegistrationResponse {
    private Long id;
    private String applicationName;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String message;

    @Generated
    public static AppRegistrationResponseBuilder builder() {
        return new AppRegistrationResponseBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getApplicationName() {
        return this.applicationName;
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
    public String getRedirectUri() {
        return this.redirectUri;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
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
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    @Generated
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AppRegistrationResponse)) {
            return false;
        }
        AppRegistrationResponse other = (AppRegistrationResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$applicationName = this.getApplicationName();
        String other$applicationName = other.getApplicationName();
        if (this$applicationName == null ? other$applicationName != null : !this$applicationName.equals(other$applicationName)) {
            return false;
        }
        String this$clientId = this.getClientId();
        String other$clientId = other.getClientId();
        if (this$clientId == null ? other$clientId != null : !this$clientId.equals(other$clientId)) {
            return false;
        }
        String this$clientSecret = this.getClientSecret();
        String other$clientSecret = other.getClientSecret();
        if (this$clientSecret == null ? other$clientSecret != null : !this$clientSecret.equals(other$clientSecret)) {
            return false;
        }
        String this$redirectUri = this.getRedirectUri();
        String other$redirectUri = other.getRedirectUri();
        if (this$redirectUri == null ? other$redirectUri != null : !this$redirectUri.equals(other$redirectUri)) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        return !(this$message == null ? other$message != null : !this$message.equals(other$message));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AppRegistrationResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $applicationName = this.getApplicationName();
        result = result * 59 + ($applicationName == null ? 43 : $applicationName.hashCode());
        String $clientId = this.getClientId();
        result = result * 59 + ($clientId == null ? 43 : $clientId.hashCode());
        String $clientSecret = this.getClientSecret();
        result = result * 59 + ($clientSecret == null ? 43 : $clientSecret.hashCode());
        String $redirectUri = this.getRedirectUri();
        result = result * 59 + ($redirectUri == null ? 43 : $redirectUri.hashCode());
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AppRegistrationResponse(id=" + this.getId() + ", applicationName=" + this.getApplicationName() + ", clientId=" + this.getClientId() + ", clientSecret=" + this.getClientSecret() + ", redirectUri=" + this.getRedirectUri() + ", message=" + this.getMessage() + ")";
    }

    @Generated
    public AppRegistrationResponse() {
    }

    @Generated
    public AppRegistrationResponse(Long id, String applicationName, String clientId, String clientSecret, String redirectUri, String message) {
        this.id = id;
        this.applicationName = applicationName;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.message = message;
    }

    @Generated
    public static class AppRegistrationResponseBuilder {
        @Generated
        private Long id;
        @Generated
        private String applicationName;
        @Generated
        private String clientId;
        @Generated
        private String clientSecret;
        @Generated
        private String redirectUri;
        @Generated
        private String message;

        @Generated
        AppRegistrationResponseBuilder() {
        }

        @Generated
        public AppRegistrationResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public AppRegistrationResponseBuilder applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        @Generated
        public AppRegistrationResponseBuilder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        @Generated
        public AppRegistrationResponseBuilder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        @Generated
        public AppRegistrationResponseBuilder redirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        @Generated
        public AppRegistrationResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        @Generated
        public AppRegistrationResponse build() {
            return new AppRegistrationResponse(this.id, this.applicationName, this.clientId, this.clientSecret, this.redirectUri, this.message);
        }

        @Generated
        public String toString() {
            return "AppRegistrationResponse.AppRegistrationResponseBuilder(id=" + this.id + ", applicationName=" + this.applicationName + ", clientId=" + this.clientId + ", clientSecret=" + this.clientSecret + ", redirectUri=" + this.redirectUri + ", message=" + this.message + ")";
        }
    }
}

