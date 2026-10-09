package com.datavault.personal_data_vault.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Generated;

public class AppRegistrationRequest {
    @NotBlank
    @Size(min=2, max=100)
    private @NotBlank @Size(min=2, max=100) String applicationName;
    @Size(max=2000)
    private @Size(max=2000) String description;
    @NotBlank
    @Size(max=2048)
    private @NotBlank @Size(max=2048) String redirectUri;

    @Generated
    public AppRegistrationRequest() {
    }

    @Generated
    public String getApplicationName() {
        return this.applicationName;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getRedirectUri() {
        return this.redirectUri;
    }

    @Generated
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AppRegistrationRequest)) {
            return false;
        }
        AppRegistrationRequest other = (AppRegistrationRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$applicationName = this.getApplicationName();
        String other$applicationName = other.getApplicationName();
        if (this$applicationName == null ? other$applicationName != null : !this$applicationName.equals(other$applicationName)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        String this$redirectUri = this.getRedirectUri();
        String other$redirectUri = other.getRedirectUri();
        return !(this$redirectUri == null ? other$redirectUri != null : !this$redirectUri.equals(other$redirectUri));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AppRegistrationRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $applicationName = this.getApplicationName();
        result = result * 59 + ($applicationName == null ? 43 : $applicationName.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        String $redirectUri = this.getRedirectUri();
        result = result * 59 + ($redirectUri == null ? 43 : $redirectUri.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AppRegistrationRequest(applicationName=" + this.getApplicationName() + ", description=" + this.getDescription() + ", redirectUri=" + this.getRedirectUri() + ")";
    }
}

