package com.datavault.personal_data_vault.dto.request;

import com.datavault.personal_data_vault.entity.PersonalData;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Generated;

public class PersonalDataRequest {
    @NotNull
    private PersonalData.DataType dataType;
    @NotBlank
    @Size(max=10000)
    private @NotBlank @Size(max=10000) String value;
    @Size(max=500)
    private @Size(max=500) String description;

    @Generated
    public PersonalDataRequest() {
    }

    @Generated
    public PersonalData.DataType getDataType() {
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
    public void setDataType(PersonalData.DataType dataType) {
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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PersonalDataRequest)) {
            return false;
        }
        PersonalDataRequest other = (PersonalDataRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        PersonalData.DataType this$dataType = this.getDataType();
        PersonalData.DataType other$dataType = other.getDataType();
        if (this$dataType == null ? other$dataType != null : !((Object)((Object)this$dataType)).equals((Object)other$dataType)) {
            return false;
        }
        String this$value = this.getValue();
        String other$value = other.getValue();
        if (this$value == null ? other$value != null : !this$value.equals(other$value)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        return !(this$description == null ? other$description != null : !this$description.equals(other$description));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof PersonalDataRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        PersonalData.DataType $dataType = this.getDataType();
        result = result * 59 + ($dataType == null ? 43 : ((Object)((Object)$dataType)).hashCode());
        String $value = this.getValue();
        result = result * 59 + ($value == null ? 43 : $value.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "PersonalDataRequest(dataType=" + String.valueOf((Object)this.getDataType()) + ", value=" + this.getValue() + ", description=" + this.getDescription() + ")";
    }
}

