package com.datavault.personal_data_vault.repository;

import com.datavault.personal_data_vault.entity.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {
    List<PersonalData> findByUserId(Long userId);
}
