package fit.hutech.LePhuocToan_3296.controller.admin;

import fit.hutech.LePhuocToan_3296.entity.Coupon;
import fit.hutech.LePhuocToan_3296.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/coupons")
@RequiredArgsConstructor
public class AdminCouponController {
    private final CouponService couponService;

    @GetMapping
    public String listCoupons(
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        model.addAttribute("couponPage", couponService.getAllCoupons(PageRequest.of(page, 20)));
        model.addAttribute("newCoupon", new Coupon());
        return "admin/coupons";
    }

    @PostMapping("/add")
    public String addCoupon(
            @ModelAttribute("newCoupon") Coupon coupon,
            RedirectAttributes redirectAttributes) {
        try {
            couponService.saveCoupon(coupon);
            redirectAttributes.addFlashAttribute("success", "Them ma giam gia thanh cong!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Loi: " + e.getMessage());
        }
        return "redirect:/admin/coupons";
    }

    @PostMapping("/toggle/{id}")
    public String toggleCoupon(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            Coupon coupon = couponService.getCouponById(id);
            coupon.setIsActive(!coupon.getIsActive());
            couponService.saveCoupon(coupon);
            redirectAttributes.addFlashAttribute("success", "Da cap nhat trang thai coupon!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Loi: " + e.getMessage());
        }
        return "redirect:/admin/coupons";
    }

    @PostMapping("/delete/{id}")
    public String deleteCoupon(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            couponService.deleteCoupon(id);
            redirectAttributes.addFlashAttribute("success", "Xoa coupon thanh cong!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Khong the xoa coupon nay!");
        }
        return "redirect:/admin/coupons";
    }
}
