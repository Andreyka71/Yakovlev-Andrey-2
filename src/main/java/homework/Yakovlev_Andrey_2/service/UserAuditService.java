package homework.Yakovlev_Andrey_2.service;

import homework.Yakovlev_Andrey_2.entity.UserAudit;
import homework.Yakovlev_Andrey_2.repository.UserAuditRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAuditService {

    private final UserAuditRepository userAuditRepository;

    public void createAuditRecord(UUID userId, String actionType, String details) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        UserAudit.UserKey key = new UserAudit.UserKey();
        key.setUserId(userId);
        key.setActionTime(Instant.now());

        UserAudit audit = new UserAudit();
        audit.setKey(key);
        audit.setActionType(actionType);
        audit.setDetails(details);

        userAuditRepository.save(audit);
    }

    public List<UserAudit> getAuditByUserId(UUID userId) {
        return userAuditRepository.findByUserId(userId);
    }
}