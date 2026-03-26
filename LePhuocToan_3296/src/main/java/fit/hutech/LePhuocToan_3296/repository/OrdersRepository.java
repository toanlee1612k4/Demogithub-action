package fit.hutech.LePhuocToan_3296.repository;

import fit.hutech.LePhuocToan_3296.entity.Orders;
import fit.hutech.LePhuocToan_3296.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    // Lịch sử đơn hàng của khách (F10)
    List<Orders> findByUserOrderByOrderDateDesc(User user);
    
    Page<Orders> findByUser(User user, Pageable pageable);
    
    // Admin: Quản lý đơn hàng (F14)
    Page<Orders> findByStatus(String status, Pageable pageable);
    
    List<Orders> findByStatusOrderByOrderDateDesc(String status);
    
    // Dashboard: Đơn hàng mới
    List<Orders> findTop10ByOrderByOrderDateDesc();
    
    // Thống kê doanh thu (F19) - chỉ tính đơn đã hoàn thành
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Orders o WHERE o.status = 'COMPLETED' " +
           "AND o.orderDate BETWEEN :startDate AND :endDate")
    Double calculateRevenue(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // ✅ Debug: Tổng doanh thu TẤT CẢ đơn COMPLETED (không giới hạn thời gian)
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Orders o WHERE o.status = 'COMPLETED'")
    Double calculateTotalRevenue();
    
    // ✅ Debug: Đếm số đơn COMPLETED
    @Query("SELECT COUNT(o) FROM Orders o WHERE o.status = 'COMPLETED'")
    Long countCompletedOrders();
    
    @Query("SELECT COUNT(o) FROM Orders o WHERE o.status = :status")
    Long countByStatus(@Param("status") String status);
    
    // Đơn hàng trong ngày
    @Query("SELECT o FROM Orders o WHERE o.orderDate >= :startOfDay ORDER BY o.orderDate DESC")
    List<Orders> findTodayOrders(@Param("startOfDay") LocalDateTime startOfDay);
}