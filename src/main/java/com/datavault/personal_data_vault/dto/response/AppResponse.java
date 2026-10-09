package com.datavault.personal_data_vault.dto.response;

import com.datavault.personal_data_vault.entity.ThirdPartyApplication;
import java.time.LocalDateTime;
import lombok.Generated;

public class AppResponse {
    private Long id;
    private String applicationName;
    private String clientId;
    private String description;
    private String redirectUri;
    private boolean active;
    private LocalDateTime createdAt;

    public static AppResponse from(ThirdPartyApplication app) {
        return AppResponse.builder().id(app.getId()).applicationName(app.getApplicationName()).clientId(app.getClientId()).description(app.getDescription()).redirectUri(app.getRedirectUri()).active(app.isActive()).createdAt(app.getCreatedAt()).build();
    }

    @Generated
    public static AppResponseBuilder builder() {
        return new AppResponseBuilder();
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
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getRedirectUri() {
        return this.redirectUri;
    }

    @Generated
    public boolean isActive() {
        return this.active;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
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
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    @Generated
    public void setActive(boolean active) {
        this.active = active;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AppResponse)) {
            return false;
        }
        AppResponse other = (AppResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isActive() != other.isActive()) {
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
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        String this$redirectUri = this.getRedirectUri();
        String other$redirectUri = other.getRedirectUri();
        if (this$redirectUri == null ? other$redirectUri != null : !this$redirectUri.equals(other$redirectUri)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        return !(this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AppResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isActive() ? 79 : 97);
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $applicationName = this.getApplicationName();
        result = result * 59 + ($applicationName == null ? 43 : $applicationName.hashCode());
        String $clientId = this.getClientId();
        result = result * 59 + ($clientId == null ? 43 : $clientId.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        String $redirectUri = this.getRedirectUri();
        result = result * 59 + ($redirectUri == null ? 43 : $redirectUri.hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AppResponse(id=" + this.getId() + ", applicationName=" + this.getApplicationName() + ", clientId=" + this.getClientId() + ", description=" + this.getDescription() + ", redirectUri=" + this.getRedirectUri() + ", active=" + this.isActive() + ", createdAt=" + String.valueOf(this.getCreatedAt()) + ")";
    }

    @Generated
    public AppResponse() {
    }

    @Generated
    public AppResponse(Long id, String applicationName, String clientId, String description, String redirectUri, boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.applicationName = applicationName;
        this.clientId = clientId;
        this.description = description;
        this.redirectUri = redirectUri;
        this.active = active;
        this.createdAt = createdAt;
    }

    @Generated
    public static class AppResponseBuilder {
        @Generated
        private Long id;
        @Generated
        private String applicationName;
        @Generated
        private String clientId;
        @Generated
        private String description;
        @Generated
        private String redirectUri;
        @Generated
        private boolean active;
        @Generated
        private LocalDateTime createdAt;

        @Generated
        AppResponseBuilder() {
        }

        @Generated
        public AppResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public AppResponseBuilder applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        @Generated
        public AppResponseBuilder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        @Generated
        public AppResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Generated
        public AppResponseBuilder redirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        @Generated
        public AppResponseBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        @Generated
        public AppResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Generated
        public AppResponse build() {
            return new AppResponse(this.id, this.applicationName, this.clientId, this.description, this.redirectUri, this.active, this.createdAt);
        }

        @Generated
        public String toString() {
            return "AppResponse.AppResponseBuilder(id=" + this.id + ", applicationName=" + this.applicationName + ", clientId=" + this.clientId + ", description=" + this.description + ", redirectUri=" + this.redirectUri + ", active=" + this.active + ", createdAt=" + String.valueOf(this.createdAt) + ")";
        }
    }
}

