package fit.hutech.LePhuocToan_3296.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"user", "orderDetails", "orderCoupons"})
@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @CreationTimestamp
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    private String status = "PENDING"; // PENDING, CONFIRMED, PROCESSING, SHIPPING, COMPLETED, CANCELLED

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    private String note;
    
    @Column(name = "payment_status")
    private String paymentStatus = "UNPAID"; // UNPAID, PAID, REFUNDED, PARTIAL
    
    @Column(name = "payment_method")
    private String paymentMethod = "COD"; // COD, BANKING, MOMO, VNPAY, ZALOPAY
    
    @Column(name = "shipping_fee")
    private Double shippingFee = 0.0;
    
    @Column(name = "discount_amount")
    private Double discountAmount = 0.0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderCoupon> orderCoupons;
}