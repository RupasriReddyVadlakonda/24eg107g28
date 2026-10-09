package com.datavault.personal_data_vault.repository;

import com.datavault.personal_data_vault.entity.PersonalData;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalDataRepository
extends JpaRepository<PersonalData, Long> {
    public List<PersonalData> findByUserId(Long var1);

    public Optional<PersonalData> findByIdAndUserId(Long var1, Long var2);

    public Optional<PersonalData> findByUserIdAndDataType(Long var1, PersonalData.DataType var2);

    public boolean existsByUserIdAndDataTypeAndIdNot(Long userId, PersonalData.DataType dataType, Long id);
}
