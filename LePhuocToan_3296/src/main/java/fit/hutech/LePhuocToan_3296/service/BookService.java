package fit.hutech.LePhuocToan_3296.service;

import fit.hutech.LePhuocToan_3296.entity.Book;
import fit.hutech.LePhuocToan_3296.entity.Category;
import fit.hutech.LePhuocToan_3296.repository.BookRepository;
import fit.hutech.LePhuocToan_3296.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService implements IBookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findByIsDeletedFalse(pageable);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void softDeleteBook(Long id) {
        Book book = getBookById(id);
        book.setIsDeleted(true);
        bookRepository.save(book);
    }

    @Override
    public Page<Book> searchBooks(String keyword, Pageable pageable) {
        return bookRepository.searchBooks(keyword, pageable);
    }

    @Override
    public Page<Book> filterByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return bookRepository.findByCategoryAndIsDeletedFalse(category, pageable);
    }

    @Override
    public Page<Book> filterByPrice(Double minPrice, Double maxPrice, Pageable pageable) {
        return bookRepository.findByPriceBetweenAndIsDeletedFalse(minPrice, maxPrice, pageable);
    }

    @Override
    public List<Book> getNewBooks() {
        return bookRepository.findTop10ByIsDeletedFalseOrderByCreatedAtDesc();
    }

    @Override
    public List<Book> getFeaturedBooks() {
        return bookRepository.findFeaturedBooks(PageRequest.of(0, 10));
    }

    @Override
    public List<Book> getBestSellingBooks() {
        return bookRepository.findFeaturedBooks(PageRequest.of(0, 10));
    }

    @Override
    public boolean checkStock(Long bookId, Integer quantity) {
        Integer available = bookRepository.getAvailableStock(bookId);
        return available != null && available >= quantity;
    }

    @Override
    public void updateStock(Long bookId, Integer quantity) {
        Book book = getBookById(bookId);
        book.setQuantity(book.getQuantity() - quantity);
        bookRepository.save(book);
    }
}