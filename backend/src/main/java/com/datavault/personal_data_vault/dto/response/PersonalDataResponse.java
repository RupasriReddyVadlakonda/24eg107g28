package com.datavault.personal_data_vault.dto.response;

import com.datavault.personal_data_vault.entity.PersonalData;
import java.time.LocalDateTime;
import lombok.Generated;

public class PersonalDataResponse {
    private Long id;
    private String dataType;
    private String value;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PersonalDataResponse from(PersonalData data, String decryptedValue) {
        return PersonalDataResponse.builder().id(data.getId()).dataType(data.getDataType().name()).value(decryptedValue).description(data.getDescription()).createdAt(data.getCreatedAt()).updatedAt(data.getUpdatedAt()).build();
    }

    @Generated
    public static PersonalDataResponseBuilder builder() {
        return new PersonalDataResponseBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getDataType() {
        return this.dataType;
    }

    @Generated
    public String getValue() {
        return this.value;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Generated
    public void setValue(String value) {
        this.value = value;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PersonalDataResponse)) {
            return false;
        }
        PersonalDataResponse other = (PersonalDataResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$dataType = this.getDataType();
        String other$dataType = other.getDataType();
        if (this$dataType == null ? other$dataType != null : !this$dataType.equals(other$dataType)) {
            return false;
        }
        String this$value = this.getValue();
        String other$value = other.getValue();
        if (this$value == null ? other$value != null : !this$value.equals(other$value)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt)) {
            return false;
        }
        LocalDateTime this$updatedAt = this.getUpdatedAt();
        LocalDateTime other$updatedAt = other.getUpdatedAt();
        return !(this$updatedAt == null ? other$updatedAt != null : !((Object)this$updatedAt).equals(other$updatedAt));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof PersonalDataResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $dataType = this.getDataType();
        result = result * 59 + ($dataType == null ? 43 : $dataType.hashCode());
        String $value = this.getValue();
        result = result * 59 + ($value == null ? 43 : $value.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        LocalDateTime $updatedAt = this.getUpdatedAt();
        result = result * 59 + ($updatedAt == null ? 43 : ((Object)$updatedAt).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "PersonalDataResponse(id=" + this.getId() + ", dataType=" + this.getDataType() + ", value=" + this.getValue() + ", description=" + this.getDescription() + ", createdAt=" + String.valueOf(this.getCreatedAt()) + ", updatedAt=" + String.valueOf(this.getUpdatedAt()) + ")";
    }

    @Generated
    public PersonalDataResponse() {
    }

    @Generated
    public PersonalDataResponse(Long id, String dataType, String value, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.dataType = dataType;
        this.value = value;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Generated
    public static class PersonalDataResponseBuilder {
        @Generated
        private Long id;
        @Generated
        private String dataType;
        @Generated
        private String value;
        @Generated
        private String description;
        @Generated
        private LocalDateTime createdAt;
        @Generated
        private LocalDateTime updatedAt;

        @Generated
        PersonalDataResponseBuilder() {
        }

        @Generated
        public PersonalDataResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public PersonalDataResponseBuilder dataType(String dataType) {
            this.dataType = dataType;
            return this;
        }

        @Generated
        public PersonalDataResponseBuilder value(String value) {
            this.value = value;
            return this;
        }

        @Generated
        public PersonalDataResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Generated
        public PersonalDataResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Generated
        public PersonalDataResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @Generated
        public PersonalDataResponse build() {
            return new PersonalDataResponse(this.id, this.dataType, this.value, this.description, this.createdAt, this.updatedAt);
        }

        @Generated
        public String toString() {
            return "PersonalDataResponse.PersonalDataResponseBuilder(id=" + this.id + ", dataType=" + this.dataType + ", value=" + this.value + ", description=" + this.description + ", createdAt=" + String.valueOf(this.createdAt) + ", updatedAt=" + String.valueOf(this.updatedAt) + ")";
        }
    }
}

