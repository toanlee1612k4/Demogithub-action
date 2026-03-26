package fit.hutech.LePhuocToan_3296.service;

import fit.hutech.LePhuocToan_3296.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICouponService {
    Page<Coupon> getAllCoupons(Pageable pageable);
    Coupon getCouponById(Long id);
    Coupon getCouponByCode(String code);
    Coupon saveCoupon(Coupon coupon);
    void deleteCoupon(Long id);
    boolean isValidCoupon(String code);
    Double calculateDiscount(String code, Double orderTotal);
}