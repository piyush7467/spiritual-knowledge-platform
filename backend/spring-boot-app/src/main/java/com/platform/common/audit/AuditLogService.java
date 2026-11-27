package com.platform.common.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void record(Long actorUserId, String actorRole, String actionCode,
                       String entityType, UUID entityPublicId, Map<String, Object> details) {
        AuditLogEntity log = new AuditLogEntity();
        log.setActorUserId(actorUserId);
        log.setActorRole(actorRole);
        log.setActionCode(actionCode);
        log.setEntityType(entityType);
        log.setEntityPublicId(entityPublicId);
        log.setDetailsJson(details);
        auditLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public Page<AuditLogEntity> listRecent(Pageable pageable) {
        return auditLogRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}

