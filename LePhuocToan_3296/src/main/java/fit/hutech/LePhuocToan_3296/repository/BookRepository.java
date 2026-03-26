package fit.hutech.LePhuocToan_3296.repository;

import fit.hutech.LePhuocToan_3296.entity.Book;
import fit.hutech.LePhuocToan_3296.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Tìm kiếm thông minh (F02)
    @Query("SELECT b FROM Book b WHERE b.isDeleted = false " +
           "AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Book> searchBooks(@Param("keyword") String keyword, Pageable pageable);
    
    // Lọc theo danh mục (F03)
    Page<Book> findByCategoryAndIsDeletedFalse(Category category, Pageable pageable);
    
    // Lọc theo khoảng giá (F03)
    Page<Book> findByPriceBetweenAndIsDeletedFalse(Double minPrice, Double maxPrice, Pageable pageable);
    
    // Lọc theo danh mục và giá
    Page<Book> findByCategoryAndPriceBetweenAndIsDeletedFalse(
        Category category, Double minPrice, Double maxPrice, Pageable pageable);
    
    // Sản phẩm mới nhất (F01)
    List<Book> findTop10ByIsDeletedFalseOrderByCreatedAtDesc();
    
    // Sản phẩm nổi bật (có thể dựa vào số lượng bán)
    @Query("SELECT b FROM Book b WHERE b.isDeleted = false ORDER BY b.quantity DESC")
    List<Book> findFeaturedBooks(Pageable pageable);
    
    // Kiểm tra tồn kho
    @Query("SELECT b.quantity FROM Book b WHERE b.id = :bookId AND b.isDeleted = false")
    Integer getAvailableStock(@Param("bookId") Long bookId);
    
    // Tất cả sách chưa bị xóa
    Page<Book> findByIsDeletedFalse(Pageable pageable);
    
    // Admin: Tất cả sách (kể cả đã xóa)
    Page<Book> findAll(Pageable pageable);
}