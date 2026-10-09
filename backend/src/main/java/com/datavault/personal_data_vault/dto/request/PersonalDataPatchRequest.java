package com.datavault.personal_data_vault.dto.request;

import com.datavault.personal_data_vault.entity.PersonalData;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;

public class PersonalDataPatchRequest {
    private PersonalData.DataType dataType;
    @Size(max = 10000)
    private String value;
    @Size(max = 500)
    private String description;
    private boolean dataTypeProvided;
    private boolean valueProvided;
    private boolean descriptionProvided;

    public PersonalData.DataType getDataType() {
        return dataType;
    }

    public void setDataType(PersonalData.DataType dataType) {
        this.dataType = dataType;
        this.dataTypeProvided = true;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.valueProvided = true;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDescriptionProvided() {
        return descriptionProvided;
    }

    public void setDescription(String description) {
        this.description = description;
        this.descriptionProvided = true;
    }

    @AssertTrue(message = "At least one field must be provided")
    public boolean isPatchPresent() {
        return dataTypeProvided || valueProvided || descriptionProvided;
    }

    @AssertTrue(message = "Data type must not be null when provided")
    public boolean isDataTypeValid() {
        return !dataTypeProvided || dataType != null;
    }

    @AssertTrue(message = "Value must not be blank when provided")
    public boolean isValueValid() {
        return !valueProvided || value != null && !value.isBlank();
    }
}
