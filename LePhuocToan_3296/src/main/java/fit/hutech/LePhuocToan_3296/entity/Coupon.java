package fit.hutech.LePhuocToan_3296.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String code;
    
    @Column(name = "discount_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType; // PERCENT, FIXED
    
    @Column(name = "discount_value", nullable = false)
    private Double discountValue;
    
    @Column(name = "min_order_value")
    private Double minOrderValue = 0.0;
    
    @Column(name = "max_discount")
    private Double maxDiscount;
    
    @Column(name = "usage_limit")
    private Integer usageLimit = 100;
    
    @Column(name = "used_count")
    private Integer usedCount = 0;
    
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @OneToMany(mappedBy = "coupon")
    private List<OrderCoupon> orderCoupons;
    
    public enum DiscountType {
        PERCENT, FIXED
    }
}