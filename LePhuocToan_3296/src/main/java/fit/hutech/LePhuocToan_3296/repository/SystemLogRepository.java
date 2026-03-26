package fit.hutech.LePhuocToan_3296.repository;

import fit.hutech.LePhuocToan_3296.entity.SystemLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
    // Audit log theo entity (F13)
    Page<SystemLog> findByEntityNameAndEntityId(String entityName, Long entityId, Pageable pageable);
    
    Page<SystemLog> findByAction(String action, Pageable pageable);
    
    Page<SystemLog> findByModifiedBy(String modifiedBy, Pageable pageable);
}