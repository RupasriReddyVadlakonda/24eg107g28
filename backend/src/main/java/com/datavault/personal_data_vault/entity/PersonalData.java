package com.datavault.personal_data_vault.entity;

import com.datavault.personal_data_vault.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Generated;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="personal_data", uniqueConstraints={@UniqueConstraint(name="uk_personal_data_user_type", columnNames={"user_id", "data_type"})})
public class PersonalData {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false)
    private DataType dataType;
    @Column(nullable=false, columnDefinition="TEXT")
    private String encryptedValue;
    @Column(length=500)
    private String description;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Generated
    public static PersonalDataBuilder builder() {
        return new PersonalDataBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public User getUser() {
        return this.user;
    }

    @Generated
    public DataType getDataType() {
        return this.dataType;
    }

    @Generated
    public String getEncryptedValue() {
        return this.encryptedValue;
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
    public void setUser(User user) {
        this.user = user;
    }

    @Generated
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    @Generated
    public void setEncryptedValue(String encryptedValue) {
        this.encryptedValue = encryptedValue;
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
    public PersonalData() {
    }

    @Generated
    public PersonalData(Long id, User user, DataType dataType, String encryptedValue, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.dataType = dataType;
        this.encryptedValue = encryptedValue;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Generated
    public static class PersonalDataBuilder {
        @Generated
        private Long id;
        @Generated
        private User user;
        @Generated
        private DataType dataType;
        @Generated
        private String encryptedValue;
        @Generated
        private String description;
        @Generated
        private LocalDateTime createdAt;
        @Generated
        private LocalDateTime updatedAt;

        @Generated
        PersonalDataBuilder() {
        }

        @Generated
        public PersonalDataBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public PersonalDataBuilder user(User user) {
            this.user = user;
            return this;
        }

        @Generated
        public PersonalDataBuilder dataType(DataType dataType) {
            this.dataType = dataType;
            return this;
        }

        @Generated
        public PersonalDataBuilder encryptedValue(String encryptedValue) {
            this.encryptedValue = encryptedValue;
            return this;
        }

        @Generated
        public PersonalDataBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Generated
        public PersonalDataBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Generated
        public PersonalDataBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @Generated
        public PersonalData build() {
            return new PersonalData(this.id, this.user, this.dataType, this.encryptedValue, this.description, this.createdAt, this.updatedAt);
        }

        @Generated
        public String toString() {
            return "PersonalData.PersonalDataBuilder(id=" + this.id + ", user=[REDACTED], dataType=" + String.valueOf((Object)this.dataType) + ", encryptedValue=[REDACTED], description=[REDACTED], createdAt=" + String.valueOf(this.createdAt) + ", updatedAt=" + String.valueOf(this.updatedAt) + ")";
        }
    }

    public static enum DataType {
        NAME,
        EMAIL,
        PHONE,
        ADDRESS,
        DATE_OF_BIRTH,
        NATIONAL_ID,
        PASSPORT,
        FINANCIAL_INFORMATION,
        HEALTH_INFORMATION,
        CUSTOM;

    }
}

