package fit.hutech.LePhuocToan_3296.controller.admin;

import fit.hutech.LePhuocToan_3296.entity.Role;
import fit.hutech.LePhuocToan_3296.entity.User;
import fit.hutech.LePhuocToan_3296.repository.RoleRepository;
import fit.hutech.LePhuocToan_3296.repository.UserRepository;
import fit.hutech.LePhuocToan_3296.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String listUsers(
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        
        model.addAttribute("userPage", 
            userService.getAllUsers(PageRequest.of(page, 20)));
        return "admin/users/list";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin/users/form";
    }

    @PostMapping("/add")
    public String addUser(
            @ModelAttribute User user,
            @RequestParam(name = "roleIds", required = false) Set<Long> roleIds,
            RedirectAttributes redirectAttributes) {
        try {
            if (userRepository.existsByUsername(user.getUsername())) {
                redirectAttributes.addFlashAttribute("error", "Ten dang nhap da ton tai!");
                return "redirect:/admin/users/add";
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email da ton tai!");
                return "redirect:/admin/users/add";
            }

            // Set roles
            Set<Role> roles = new HashSet<>();
            if (roleIds != null) {
                for (Long roleId : roleIds) {
                    roleRepository.findById(roleId).ifPresent(roles::add);
                }
            }
            if (roles.isEmpty()) {
                roleRepository.findByName("ROLE_USER").ifPresent(roles::add);
            }
            user.setRoles(roles);
            user.setIsActive(true);
            user.setProvider("LOCAL");

            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("success", "Them nguoi dung thanh cong!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Loi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin/users/form";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(
            @PathVariable Long id,
            @ModelAttribute User user,
            @RequestParam(name = "roleIds", required = false) Set<Long> roleIds,
            @RequestParam(name = "newPassword", required = false) String newPassword,
            RedirectAttributes redirectAttributes) {
        try {
            User existing = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            existing.setFullname(user.getFullname());
            existing.setEmail(user.getEmail());
            existing.setPhoneNumber(user.getPhoneNumber());

            // Update password only if provided
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                existing.setPassword(passwordEncoder.encode(newPassword));
            }

            // Update roles
            if (roleIds != null && !roleIds.isEmpty()) {
                Set<Role> roles = new HashSet<>();
                for (Long roleId : roleIds) {
                    roleRepository.findById(roleId).ifPresent(roles::add);
                }
                existing.setRoles(roles);
            }

            userRepository.save(existing);
            redirectAttributes.addFlashAttribute("success", "Cap nhat nguoi dung thanh cong!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Loi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleUserStatus(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        userService.toggleUserStatus(id);
        redirectAttributes.addFlashAttribute("success", 
            "Da thay doi trang thai nguoi dung!");
        return "redirect:/admin/users";
    }
}