package fit.hutech.LePhuocToan_3296.service;

import fit.hutech.LePhuocToan_3296.dto.CheckoutRequest;
import fit.hutech.LePhuocToan_3296.entity.Orders;
import fit.hutech.LePhuocToan_3296.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService {
    Orders createOrder(User user, CheckoutRequest request);
    Orders getOrderById(Long id);
    List<Orders> getOrdersByUser(User user);
    Page<Orders> getAllOrders(Pageable pageable);
    Page<Orders> getOrdersByStatus(String status, Pageable pageable);
    
    void updateOrderStatus(Long orderId, String newStatus);
    void cancelOrder(Long orderId);
    
    Double calculateRevenue(LocalDateTime startDate, LocalDateTime endDate);
    List<Orders> getTodayOrders();
}