package fit.hutech.LePhuocToan_3296.service;

import fit.hutech.LePhuocToan_3296.entity.SystemLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISystemLogService {
    void log(String action, String entityName, Long entityId, 
             String oldValue, String newValue, String modifiedBy);
    Page<SystemLog> getLogsByEntity(String entityName, Long entityId, Pageable pageable);
    Page<SystemLog> getLogsByAction(String action, Pageable pageable);
    Page<SystemLog> getAllLogs(Pageable pageable);
}