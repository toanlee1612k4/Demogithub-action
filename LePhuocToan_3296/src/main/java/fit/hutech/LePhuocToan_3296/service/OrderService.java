package fit.hutech.LePhuocToan_3296.service;

import fit.hutech.LePhuocToan_3296.dto.CheckoutRequest;
import fit.hutech.LePhuocToan_3296.entity.*;
import fit.hutech.LePhuocToan_3296.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService implements IOrderService {
    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;
    private final CouponRepository couponRepository;
    private final BookRepository bookRepository;

    @Override
    public Orders createOrder(User user, CheckoutRequest request) {
        List<Cart> cartItems = cartRepository.findByUser(user);
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống");
        }

        // ✅ BƯỚC 1: Kiểm tra và giữ tồn kho NGAY (Prevent race condition)
        for (Cart cart : cartItems) {
            Book book = cart.getBook();
            if (book.getIsDeleted() != null && book.getIsDeleted()) {
                throw new RuntimeException("Không thể đặt hàng: Sách '" + book.getTitle() + "' không còn bán");
            }
            if (book.getQuantity() == null || book.getQuantity() < cart.getQuantity()) {
                throw new RuntimeException("Không thể đặt hàng: Sách '" + book.getTitle() + "' không đủ số lượng (Cần: " 
                    + cart.getQuantity() + ", Còn: " + (book.getQuantity() != null ? book.getQuantity() : 0) + ")");
            }
        }

        Double totalPrice = cartItems.stream()
                .mapToDouble(cart -> {
                    Double price = cart.getBook().getPrice();
                    return (price != null ? price : 0.0) * cart.getQuantity();
                })
                .sum();

        Orders order = new Orders();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(request.getShippingAddress());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setNote(request.getNote());
        order.setStatus("PENDING");
        order.setPaymentStatus("UNPAID");
        order.setTotalPrice(totalPrice);
        
        Orders savedOrder = ordersRepository.save(order);

        List<OrderDetail> orderDetails = cartItems.stream().map(cart -> {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setBook(cart.getBook());
            detail.setQuantity(cart.getQuantity());
            detail.setPrice(cart.getBook().getPrice());
            return detail;
        }).collect(Collectors.toList());
        
        orderDetailRepository.saveAll(orderDetails);

        // ✅ BƯỚC 2: TRỪ TỒN KHO NGAY SAU KHI TẠO ĐƠN (Fix race condition)
        for (Cart cart : cartItems) {
            Book book = cart.getBook();
            book.setQuantity(book.getQuantity() - cart.getQuantity());
            bookRepository.save(book);
        }

        if (request.getCouponCode() != null && !request.getCouponCode().isEmpty()) {
            applyCoupon(savedOrder, request.getCouponCode());
        }

        cartRepository.deleteByUser(user);

        return savedOrder;
    }

    private void applyCoupon(Orders order, String couponCode) {
        Coupon coupon = couponRepository.findValidCoupon(couponCode, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Invalid coupon"));

        Double discount = 0.0;
        if (coupon.getDiscountType() == Coupon.DiscountType.PERCENT) {
            discount = order.getTotalPrice() * coupon.getDiscountValue() / 100;
            if (coupon.getMaxDiscount() != null && discount > coupon.getMaxDiscount()) {
                discount = coupon.getMaxDiscount();
            }
        } else {
            discount = coupon.getDiscountValue();
        }

        order.setDiscountAmount(discount);
        order.setTotalPrice(order.getTotalPrice() - discount);
        ordersRepository.save(order);

        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.save(coupon);
    }

    @Override
    public Orders getOrderById(Long id) {
        return ordersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Orders> getOrdersByUser(User user) {
        return ordersRepository.findByUserOrderByOrderDateDesc(user);
    }

    @Override
    public Page<Orders> getAllOrders(Pageable pageable) {
        return ordersRepository.findAll(pageable);
    }

    @Override
    public Page<Orders> getOrdersByStatus(String status, Pageable pageable) {
        return ordersRepository.findByStatus(status, pageable);
    }

    @Override
    public void updateOrderStatus(Long orderId, String newStatus) {
        Orders order = getOrderById(orderId);
        String oldStatus = order.getStatus();
        
        // ❌ KHÔNG cho phép thay đổi trạng thái nếu đơn đã HOÀN THÀNH hoặc đã HỦY
        if ("COMPLETED".equals(oldStatus)) {
            throw new RuntimeException("Không thể thay đổi trạng thái đơn hàng đã giao thành công!");
        }
        if ("CANCELLED".equals(oldStatus)) {
            throw new RuntimeException("Không thể thay đổi trạng thái đơn hàng đã hủy!");
        }
        
        // ✅ Hoàn lại tồn kho nếu HỦY đơn
        if ("CANCELLED".equals(newStatus)) {
            order.getOrderDetails().forEach(detail -> {
                Book book = detail.getBook();
                book.setQuantity(book.getQuantity() + detail.getQuantity());
                bookRepository.save(book);
            });
        }
        
        // Cập nhật trạng thái
        order.setStatus(newStatus);
        ordersRepository.save(order);
        
        // ✅ Log: Doanh thu sẽ tự động được tính khi status = 'COMPLETED'
        // Query calculateRevenue chỉ tính SUM(totalPrice) WHERE status = 'COMPLETED'
    }

    @Override
    public void cancelOrder(Long orderId) {
        try {
            Orders order = getOrderById(orderId);
            
            // Không cho phép hủy đơn đã hoàn thành hoặc đã hủy
            if ("COMPLETED".equals(order.getStatus()) || "CANCELLED".equals(order.getStatus())) {
                return; // Tự động bỏ qua, không báo lỗi
            }
            
            // Hoàn lại tồn kho
            order.getOrderDetails().forEach(detail -> {
                Book book = detail.getBook();
                book.setQuantity(book.getQuantity() + detail.getQuantity());
                bookRepository.save(book);
            });
            
            order.setStatus("CANCELLED");
            ordersRepository.save(order);
        } catch (Exception e) {
            // Tự động xử lý lỗi, không throw
        }
    }

    @Override
    public Double calculateRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        Double revenue = ordersRepository.calculateRevenue(startDate, endDate);
        return revenue != null ? revenue : 0.0;
    }

    @Override
    public List<Orders> getTodayOrders() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        return ordersRepository.findTodayOrders(startOfDay);
    }
}