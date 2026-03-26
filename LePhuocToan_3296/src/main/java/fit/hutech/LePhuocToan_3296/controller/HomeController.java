package fit.hutech.LePhuocToan_3296.controller;

import fit.hutech.LePhuocToan_3296.entity.Book;
import fit.hutech.LePhuocToan_3296.entity.Category;
import fit.hutech.LePhuocToan_3296.service.BookService;
import fit.hutech.LePhuocToan_3296.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("newBooks", bookService.getNewBooks());
        model.addAttribute("featuredBooks", bookService.getFeaturedBooks());
        model.addAttribute("bestSellers", bookService.getBestSellingBooks());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "home";
    }

    @GetMapping("/books")
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<Book> bookPage;

        if (keyword != null && !keyword.isEmpty()) {
            bookPage = bookService.searchBooks(keyword, pageable);
        } else if (categoryId != null && minPrice != null && maxPrice != null) {
            bookPage = bookService.filterByCategory(categoryId, pageable);
        } else if (categoryId != null) {
            bookPage = bookService.filterByCategory(categoryId, pageable);
        } else if (minPrice != null && maxPrice != null) {
            bookPage = bookService.filterByPrice(minPrice, maxPrice, pageable);
        } else {
            bookPage = bookService.getAllBooks(pageable);
        }

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        
        return "books/list";
    }

    @GetMapping("/books/{id}")
    public String bookDetail(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("relatedBooks", bookService.getFeaturedBooks());
        return "books/detail";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        
        Page<Book> results = bookService.searchBooks(
            keyword, 
            PageRequest.of(page, 12)
        );
        
        model.addAttribute("bookPage", results);
        model.addAttribute("keyword", keyword);
        return "books/search-results";
    }
}