package fit.hutech.LePhuocToan_3296.repository;

import fit.hutech.LePhuocToan_3296.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    // Tim coupon theo code (F07)
    Optional<Coupon> findByCode(String code);
    
    // Coupon dang hoat dong
    Page<Coupon> findByIsActiveTrue(Pageable pageable);
    
    // Kiem tra coupon hop le
    @Query("SELECT c FROM Coupon c WHERE c.code = :code " +
           "AND c.isActive = true " +
           "AND c.startDate <= :now " +
           "AND c.endDate >= :now " +
           "AND c.usedCount < c.usageLimit")
    Optional<Coupon> findValidCoupon(@Param("code") String code, @Param("now") LocalDateTime now);
}