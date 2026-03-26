package fit.hutech.LePhuocToan_3296.controller;

import fit.hutech.LePhuocToan_3296.entity.Orders;
import fit.hutech.LePhuocToan_3296.entity.User;
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
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String myOrders(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName());
        List<Orders> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Orders order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "orders/detail";
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            orderService.cancelOrder(id);
            redirectAttributes.addFlashAttribute("success", "Đã hủy đơn hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/orders/" + id;
    }
}