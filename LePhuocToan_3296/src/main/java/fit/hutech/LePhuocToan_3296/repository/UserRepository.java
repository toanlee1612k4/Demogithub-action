package fit.hutech.LePhuocToan_3296.repository;

import fit.hutech.LePhuocToan_3296.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    // Admin: Quản lý người dùng (F15)
    Page<User> findByIsActiveTrue(Pageable pageable);
    
    Page<User> findByIsActiveFalse(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.role = 'USER' OR u.role = 'ADMIN'")
    Page<User> findAllCustomers(Pageable pageable);
}