package fit.hutech.LePhuocToan_3296.controller.admin;

import fit.hutech.LePhuocToan_3296.entity.Category;
import fit.hutech.LePhuocToan_3296.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("newCategory", new Category());
        return "admin/categories";
    }

    @PostMapping("/add")
    public String addCategory(
            @ModelAttribute("newCategory") Category category,
            RedirectAttributes redirectAttributes) {
        try {
            categoryService.saveCategory(category);
            redirectAttributes.addFlashAttribute("success", "Them danh muc thanh cong!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Ten danh muc da ton tai!");
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Xoa danh muc thanh cong!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Khong the xoa danh muc nay vi co sach lien quan!");
        }
        return "redirect:/admin/categories";
    }
}
