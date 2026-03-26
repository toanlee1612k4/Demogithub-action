package fit.hutech.LePhuocToan_3296.service;

import fit.hutech.LePhuocToan_3296.entity.SystemLog;
import fit.hutech.LePhuocToan_3296.repository.SystemLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
@Transactional
public class SystemLogService implements ISystemLogService {
    private final SystemLogRepository systemLogRepository;

    @Override
    public void log(String action, String entityName, Long entityId, 
                    String oldValue, String newValue, String modifiedBy) {
        SystemLog log = new SystemLog();
        log.setAction(action);
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        log.setModifiedBy(modifiedBy);
        log.setModifiedAt(LocalDateTime.now());
        systemLogRepository.save(log);
    }

    @Override
    public Page<SystemLog> getLogsByEntity(String entityName, Long entityId, Pageable pageable) {
        return systemLogRepository.findByEntityNameAndEntityId(entityName, entityId, pageable);
    }

    @Override
    public Page<SystemLog> getLogsByAction(String action, Pageable pageable) {
        return systemLogRepository.findByAction(action, pageable);
    }

    @Override
    public Page<SystemLog> getAllLogs(Pageable pageable) {
        return systemLogRepository.findAll(pageable);
    }
}