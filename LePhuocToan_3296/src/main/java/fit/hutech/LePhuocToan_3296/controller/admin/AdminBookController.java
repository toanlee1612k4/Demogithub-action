package fit.hutech.LePhuocToan_3296.controller.admin;

import fit.hutech.LePhuocToan_3296.entity.Book;
import fit.hutech.LePhuocToan_3296.service.BookService;
import fit.hutech.LePhuocToan_3296.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBookController {
    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        
        model.addAttribute("bookPage", 
            bookService.getAllBooks(PageRequest.of(page, 20)));
        return "admin/books/list";
    }

    @GetMapping("/create")
    public String createBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/books/form";
    }

    @PostMapping("/create")
    public String createBook(
            @ModelAttribute Book book,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            RedirectAttributes redirectAttributes) {
        
        try {
            if (categoryId != null) {
                book.setCategory(categoryService.getCategoryById(categoryId));
            }
            bookService.saveBook(book);
            redirectAttributes.addFlashAttribute("success", "Them sach thanh cong!");
        } catch (Exception e) {
            log.error("Error creating book: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Loi khi them sach: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/books/form";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(
            @PathVariable Long id,
            @ModelAttribute Book book,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            RedirectAttributes redirectAttributes) {
        
        try {
            Book existing = bookService.getBookById(id);
            existing.setTitle(book.getTitle());
            existing.setAuthor(book.getAuthor());
            existing.setDescription(book.getDescription());
            existing.setPrice(book.getPrice());
            existing.setQuantity(book.getQuantity());
            existing.setImage(book.getImage());
            if (categoryId != null) {
                existing.setCategory(categoryService.getCategoryById(categoryId));
            }
            bookService.saveBook(existing);
            redirectAttributes.addFlashAttribute("success", "Cap nhat thanh cong!");
        } catch (Exception e) {
            log.error("Error updating book id={}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Loi khi cap nhat sach: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            bookService.softDeleteBook(id);
            redirectAttributes.addFlashAttribute("success", "Da xoa sach!");
        } catch (Exception e) {
            log.error("Error deleting book id={}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Loi khi xoa sach: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }
}