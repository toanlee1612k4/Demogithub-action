package fit.hutech.LePhuocToan_3296.controller;

import fit.hutech.LePhuocToan_3296.dto.CheckoutRequest;
import fit.hutech.LePhuocToan_3296.entity.Address;
import fit.hutech.LePhuocToan_3296.entity.Orders;
import fit.hutech.LePhuocToan_3296.entity.User;
import fit.hutech.LePhuocToan_3296.service.AddressService;
import fit.hutech.LePhuocToan_3296.service.CartService;
import fit.hutech.LePhuocToan_3296.service.OrderService;
import fit.hutech.LePhuocToan_3296.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;
    private final AddressService addressService;

    @GetMapping
    public String checkoutPage(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName());
        List<Address> addresses = addressService.getAddressesByUser(user);
        Double total = cartService.calculateTotal(user);
        
        model.addAttribute("cartItems", cartService.getCartByUser(user));
        model.addAttribute("addresses", addresses);
        model.addAttribute("total", total);
        model.addAttribute("request", new CheckoutRequest());
        
        return "checkout/index";
    }

    @PostMapping("/process")
    public String processCheckout(
            @ModelAttribute CheckoutRequest request,
            Authentication auth,
            RedirectAttributes redirectAttributes) {
        
        try {
            User user = userService.findByUsername(auth.getName());
            Orders order = orderService.createOrder(user, request);
            
            redirectAttributes.addFlashAttribute("success", 
                "Đặt hàng thành công! Mã đơn: " + order.getId());
            
            return "redirect:/orders/" + order.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }

    @PostMapping("/apply-coupon")
    @ResponseBody
    public Double applyCoupon(
            @RequestParam String couponCode,
            Authentication auth) {
        
        User user = userService.findByUsername(auth.getName());
        Double total = cartService.calculateTotal(user);
        return total;
    }
}