package fit.hutech.LePhuocToan_3296.service;

import fit.hutech.LePhuocToan_3296.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBookService {
    List<Book> getAllBooks();
    Page<Book> getAllBooks(Pageable pageable);
    Book getBookById(Long id);
    Book saveBook(Book book);
    void deleteBook(Long id);
    void softDeleteBook(Long id);
    
    Page<Book> searchBooks(String keyword, Pageable pageable);
    Page<Book> filterByCategory(Long categoryId, Pageable pageable);
    Page<Book> filterByPrice(Double minPrice, Double maxPrice, Pageable pageable);
    
    List<Book> getNewBooks();
    List<Book> getFeaturedBooks();
    List<Book> getBestSellingBooks();
    
    boolean checkStock(Long bookId, Integer quantity);
    void updateStock(Long bookId, Integer quantity);
}