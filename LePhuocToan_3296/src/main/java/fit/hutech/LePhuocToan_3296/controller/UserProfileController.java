package fit.hutech.LePhuocToan_3296.controller;

import fit.hutech.LePhuocToan_3296.entity.User;
import fit.hutech.LePhuocToan_3296.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserService userService;

    @GetMapping
    public String profile(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("user", user);
        return "profile/index";
    }

    @PostMapping("/update")
    public String updateProfile(
            @ModelAttribute User user,
            Authentication auth,
            RedirectAttributes redirectAttributes) {
        
        User currentUser = userService.findByUsername(auth.getName());
        currentUser.setFullname(user.getFullname());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhoneNumber(user.getPhoneNumber());
        
        userService.updateProfile(currentUser);
        redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
        
        return "redirect:/profile";
    }

    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "profile/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Authentication auth,
            RedirectAttributes redirectAttributes) {
        
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", 
                "Mật khẩu mới không khớp!");
            return "redirect:/profile/change-password";
        }
        
        try {
            User user = userService.findByUsername(auth.getName());
            userService.changePassword(user, oldPassword, newPassword);
            redirectAttributes.addFlashAttribute("success", 
                "Đổi mật khẩu thành công!");
            return "redirect:/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/profile/change-password";
        }
    }
}