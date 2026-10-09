package com.datavault.personal_data_vault.service;

import com.datavault.personal_data_vault.dto.request.PersonalDataRequest;
import com.datavault.personal_data_vault.dto.request.PersonalDataPatchRequest;
import com.datavault.personal_data_vault.dto.response.PersonalDataResponse;
import com.datavault.personal_data_vault.encryption.EncryptionService;
import com.datavault.personal_data_vault.entity.PersonalData;
import com.datavault.personal_data_vault.entity.User;
import com.datavault.personal_data_vault.exception.DuplicateResourceException;
import com.datavault.personal_data_vault.exception.ResourceNotFoundException;
import com.datavault.personal_data_vault.repository.PersonalDataRepository;
import com.datavault.personal_data_vault.repository.UserRepository;
import java.util.List;
import lombok.Generated;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonalDataService {
    private final PersonalDataRepository personalDataRepository;
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;

    @Transactional
    public PersonalDataResponse create(Long userId, PersonalDataRequest request) {
        User owner = this.findUser(userId);
        if (this.personalDataRepository.findByUserIdAndDataType(userId, request.getDataType()).isPresent()) {
            throw new DuplicateResourceException("A record of this data type already exists; update it instead");
        }
        PersonalData data = PersonalData.builder().user(owner).dataType(request.getDataType()).encryptedValue(this.encryptionService.encrypt(request.getValue())).description(request.getDescription()).build();
        return this.toResponse((PersonalData)this.personalDataRepository.save(data));
    }

    @Transactional(readOnly=true)
    public List<PersonalDataResponse> list(Long userId) {
        return this.personalDataRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly=true)
    public PersonalDataResponse get(Long userId, Long dataId) {
        return this.personalDataRepository.findByIdAndUserId(dataId, userId).map(this::toResponse).orElseThrow(() -> new ResourceNotFoundException("Personal data not found"));
    }

    @Transactional
    public PersonalDataResponse update(Long userId, Long dataId, PersonalDataRequest request) {
        PersonalData data = this.personalDataRepository.findByIdAndUserId(dataId, userId).orElseThrow(() -> new ResourceNotFoundException("Personal data not found"));
        data.setDataType(request.getDataType());
        data.setEncryptedValue(this.encryptionService.encrypt(request.getValue()));
        data.setDescription(request.getDescription());
        return this.toResponse((PersonalData)this.personalDataRepository.save(data));
    }

    @Transactional
    public PersonalDataResponse patch(Long userId, Long dataId, PersonalDataPatchRequest request) {
        PersonalData data = this.personalDataRepository.findByIdAndUserId(dataId, userId).orElseThrow(() -> new ResourceNotFoundException("Personal data not found"));
        if (request.getDataType() != null) {
            if (this.personalDataRepository.existsByUserIdAndDataTypeAndIdNot(userId, request.getDataType(), dataId)) {
                throw new DuplicateResourceException("A record of this data type already exists");
            }
            data.setDataType(request.getDataType());
        }
        if (request.getValue() != null) {
            data.setEncryptedValue(this.encryptionService.encrypt(request.getValue()));
        }
        if (request.isDescriptionProvided()) {
            data.setDescription(request.getDescription());
        }
        return this.toResponse((PersonalData)this.personalDataRepository.save(data));
    }

    @Transactional
    public void delete(Long userId, Long dataId) {
        PersonalData data = this.personalDataRepository.findByIdAndUserId(dataId, userId).orElseThrow(() -> new ResourceNotFoundException("Personal data not found"));
        this.personalDataRepository.delete(data);
    }

    @Transactional
    public void writeAuthorized(User user, PersonalData.DataType type, String value) {
        PersonalData data = this.personalDataRepository.findByUserIdAndDataType(user.getId(), type).orElseGet(() -> PersonalData.builder().user(user).dataType(type).build());
        data.setEncryptedValue(this.encryptionService.encrypt(value));
        this.personalDataRepository.save(data);
    }

    @Transactional(readOnly=true)
    public PersonalDataResponse getAuthorized(Long userId, PersonalData.DataType type) {
        PersonalData data = this.personalDataRepository.findByUserIdAndDataType(userId, type).orElseThrow(() -> new ResourceNotFoundException("The user has no stored data of this type"));
        return this.toResponse(data);
    }

    @Transactional
    public PersonalDataResponse updateAuthorized(Long userId, PersonalData.DataType type, String value) {
        PersonalData data = this.personalDataRepository.findByUserIdAndDataType(userId, type).orElseThrow(() -> new ResourceNotFoundException("The user has no stored data of this type"));
        data.setEncryptedValue(this.encryptionService.encrypt(value));
        return this.toResponse((PersonalData)this.personalDataRepository.save(data));
    }

    private User findUser(Long userId) {
        return (User)this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private PersonalDataResponse toResponse(PersonalData data) {
        return PersonalDataResponse.from(data, this.encryptionService.decrypt(data.getEncryptedValue()));
    }

    @Generated
    public PersonalDataService(PersonalDataRepository personalDataRepository, UserRepository userRepository, EncryptionService encryptionService) {
        this.personalDataRepository = personalDataRepository;
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
    }
}
