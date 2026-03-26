package fit.hutech.LePhuocToan_3296.repository;

import fit.hutech.LePhuocToan_3296.entity.Book;
import fit.hutech.LePhuocToan_3296.entity.Cart;
import fit.hutech.LePhuocToan_3296.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // Giỏ hàng của user (F05)
    List<Cart> findByUser(User user);
    
    // Kiểm tra sản phẩm đã có trong giỏ chưa
    Optional<Cart> findByUserAndBook(User user, Book book);
    
    // Xóa toàn bộ giỏ hàng sau khi thanh toán
    void deleteByUser(User user);
    
    // Đếm số lượng sản phẩm trong giỏ
    Long countByUser(User user);
}