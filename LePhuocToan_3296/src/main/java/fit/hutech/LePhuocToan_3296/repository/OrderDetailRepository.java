package fit.hutech.LePhuocToan_3296.repository;

import fit.hutech.LePhuocToan_3296.entity.OrderDetail;
import fit.hutech.LePhuocToan_3296.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrder(Orders order);
    
    // Top sách bán chạy (F01, F19)
    @Query("SELECT od.book.id, od.book.title, SUM(od.quantity) as totalSold " +
           "FROM OrderDetail od " +
           "JOIN od.order o " +
           "WHERE o.status = 'COMPLETED' " +
           "GROUP BY od.book.id, od.book.title " +
           "ORDER BY totalSold DESC")
    List<Object[]> findBestSellingBooks(@Param("limit") int limit);
}