package fit.hutech.LePhuocToan_3296.service;

import fit.hutech.LePhuocToan_3296.entity.Coupon;
import fit.hutech.LePhuocToan_3296.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService implements ICouponService {
    private final CouponRepository couponRepository;

    @Override
    public Page<Coupon> getAllCoupons(Pageable pageable) {
        return couponRepository.findAll(pageable);
    }

    @Override
    public Coupon getCouponById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    @Override
    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    @Override
    public Coupon saveCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    @Override
    public boolean isValidCoupon(String code) {
        return couponRepository.findValidCoupon(code, LocalDateTime.now()).isPresent();
    }

    @Override
    public Double calculateDiscount(String code, Double orderTotal) {
        Coupon coupon = couponRepository.findValidCoupon(code, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Invalid or expired coupon"));

        if (orderTotal < coupon.getMinOrderValue()) {
            throw new RuntimeException("Order total does not meet minimum requirement");
        }

        Double discount = 0.0;
        if (coupon.getDiscountType() == Coupon.DiscountType.PERCENT) {
            discount = orderTotal * coupon.getDiscountValue() / 100;
            if (coupon.getMaxDiscount() != null && discount > coupon.getMaxDiscount()) {
                discount = coupon.getMaxDiscount();
            }
        } else {
            discount = coupon.getDiscountValue();
        }

        return discount;
    }
}