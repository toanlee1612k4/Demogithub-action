package fit.hutech.LePhuocToan_3296.controller;

import fit.hutech.LePhuocToan_3296.entity.Cart;
import fit.hutech.LePhuocToan_3296.entity.User;
import fit.hutech.LePhuocToan_3296.service.CartService;
import fit.hutech.LePhuocToan_3296.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public String viewCart(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName());
        List<Cart> cartItems = cartService.getCartByUser(user);
        Double total = cartService.calculateTotal(user);
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart/index";
    }

    @PostMapping("/add")
    public String addToCart(
            @RequestParam Long bookId,
            @RequestParam(defaultValue = "1") Integer quantity,
            Authentication auth,
            RedirectAttributes redirectAttributes) {
        
        try {
            User user = userService.findByUsername(auth.getName());
            cartService.addToCart(user, bookId, quantity);
            redirectAttributes.addFlashAttribute("success", "Thêm vào giỏ hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/cart";
    }

    @PostMapping("/update/{cartId}")
    public String updateCart(
            @PathVariable Long cartId,
            @RequestParam Integer quantity,
            RedirectAttributes redirectAttributes) {
        
        try {
            cartService.updateCartItem(cartId, quantity);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/cart";
    }

    @PostMapping("/remove/{cartId}")
    public String removeFromCart(
            @PathVariable Long cartId,
            RedirectAttributes redirectAttributes) {
        
        cartService.removeCartItem(cartId);
        redirectAttributes.addFlashAttribute("success", "Đã xóa sản phẩm!");
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        cartService.clearCart(user);
        return "redirect:/cart";
    }

    @GetMapping("/count")
    @ResponseBody
    public Integer getCartCount(Authentication auth) {
        if (auth == null) return 0;
        User user = userService.findByUsername(auth.getName());
        return cartService.getCartCount(user);
    }
}