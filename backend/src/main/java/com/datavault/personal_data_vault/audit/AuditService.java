package com.datavault.personal_data_vault.audit;

import com.datavault.personal_data_vault.entity.AccessLog;
import com.datavault.personal_data_vault.entity.PersonalData;
import com.datavault.personal_data_vault.entity.ThirdPartyApplication;
import com.datavault.personal_data_vault.entity.User;
import com.datavault.personal_data_vault.repository.AccessLogRepository;
import java.time.LocalDateTime;
import lombok.Generated;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {
    private final AccessLogRepository accessLogRepository;

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void logAccess(User user, ThirdPartyApplication application, PersonalData.DataType dataType, String operation, String purpose, String ipAddress, boolean success, String failureReason) {
        this.accessLogRepository.save(AccessLog.builder().user(user).application(application).dataType(dataType).operation(operation).purpose(purpose).timestamp(LocalDateTime.now()).ipAddress(ipAddress).success(success).failureReason(failureReason).build());
    }

    @Generated
    public AuditService(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }
}

