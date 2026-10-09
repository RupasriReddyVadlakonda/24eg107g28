package com.datavault.personal_data_vault.dto.response;

import com.datavault.personal_data_vault.entity.User;
import java.time.LocalDateTime;
import lombok.Generated;

public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;
    private boolean enabled;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        return UserResponse.builder().id(user.getId()).fullName(user.getFullName()).email(user.getEmail()).phoneNumber(user.getPhoneNumber()).role(user.getRole().name()).enabled(user.isEnabled()).createdAt(user.getCreatedAt()).build();
    }

    @Generated
    public static UserResponseBuilder builder() {
        return new UserResponseBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getFullName() {
        return this.fullName;
    }

    @Generated
    public String getEmail() {
        return this.email;
    }

    @Generated
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    @Generated
    public String getRole() {
        return this.role;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
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
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Generated
    public void setEmail(String email) {
        this.email = email;
    }

    @Generated
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Generated
    public void setRole(String role) {
        this.role = role;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
        if (!(o instanceof UserResponse)) {
            return false;
        }
        UserResponse other = (UserResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isEnabled() != other.isEnabled()) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$fullName = this.getFullName();
        String other$fullName = other.getFullName();
        if (this$fullName == null ? other$fullName != null : !this$fullName.equals(other$fullName)) {
            return false;
        }
        String this$email = this.getEmail();
        String other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) {
            return false;
        }
        String this$phoneNumber = this.getPhoneNumber();
        String other$phoneNumber = other.getPhoneNumber();
        if (this$phoneNumber == null ? other$phoneNumber != null : !this$phoneNumber.equals(other$phoneNumber)) {
            return false;
        }
        String this$role = this.getRole();
        String other$role = other.getRole();
        if (this$role == null ? other$role != null : !this$role.equals(other$role)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        return !(this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof UserResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isEnabled() ? 79 : 97);
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $fullName = this.getFullName();
        result = result * 59 + ($fullName == null ? 43 : $fullName.hashCode());
        String $email = this.getEmail();
        result = result * 59 + ($email == null ? 43 : $email.hashCode());
        String $phoneNumber = this.getPhoneNumber();
        result = result * 59 + ($phoneNumber == null ? 43 : $phoneNumber.hashCode());
        String $role = this.getRole();
        result = result * 59 + ($role == null ? 43 : $role.hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "UserResponse(id=" + this.getId() + ", fullName=" + this.getFullName() + ", email=" + this.getEmail() + ", phoneNumber=" + this.getPhoneNumber() + ", role=" + this.getRole() + ", enabled=" + this.isEnabled() + ", createdAt=" + String.valueOf(this.getCreatedAt()) + ")";
    }

    @Generated
    public UserResponse() {
    }

    @Generated
    public UserResponse(Long id, String fullName, String email, String phoneNumber, String role, boolean enabled, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.enabled = enabled;
        this.createdAt = createdAt;
    }

    @Generated
    public static class UserResponseBuilder {
        @Generated
        private Long id;
        @Generated
        private String fullName;
        @Generated
        private String email;
        @Generated
        private String phoneNumber;
        @Generated
        private String role;
        @Generated
        private boolean enabled;
        @Generated
        private LocalDateTime createdAt;

        @Generated
        UserResponseBuilder() {
        }

        @Generated
        public UserResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public UserResponseBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        @Generated
        public UserResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        @Generated
        public UserResponseBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        @Generated
        public UserResponseBuilder role(String role) {
            this.role = role;
            return this;
        }

        @Generated
        public UserResponseBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        @Generated
        public UserResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Generated
        public UserResponse build() {
            return new UserResponse(this.id, this.fullName, this.email, this.phoneNumber, this.role, this.enabled, this.createdAt);
        }

        @Generated
        public String toString() {
            return "UserResponse.UserResponseBuilder(id=" + this.id + ", fullName=" + this.fullName + ", email=" + this.email + ", phoneNumber=" + this.phoneNumber + ", role=" + this.role + ", enabled=" + this.enabled + ", createdAt=" + String.valueOf(this.createdAt) + ")";
        }
    }
}

