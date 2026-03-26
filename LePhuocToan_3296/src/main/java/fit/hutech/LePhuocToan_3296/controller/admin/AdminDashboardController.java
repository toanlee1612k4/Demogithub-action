package fit.hutech.LePhuocToan_3296.controller.admin;

import fit.hutech.LePhuocToan_3296.entity.Book;
import fit.hutech.LePhuocToan_3296.repository.BookRepository;
import fit.hutech.LePhuocToan_3296.repository.OrdersRepository;
import fit.hutech.LePhuocToan_3296.repository.UserRepository;
import fit.hutech.LePhuocToan_3296.service.OrderService;
import fit.hutech.LePhuocToan_3296.service.BookService;
import fit.hutech.LePhuocToan_3296.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {
    private final OrderService orderService;
    private final BookService bookService;
    private final UserService userService;
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @GetMapping
    public String dashboard(Model model) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        
        // ✅ Stat cards với nhiều mốc thời gian
        Double todayRevenue = orderService.calculateRevenue(startOfDay, now);
        Double weekRevenue = orderService.calculateRevenue(startOfWeek, now);
        Double monthRevenue = orderService.calculateRevenue(startOfMonth, now);
        
        // ✅ Debug: Tổng doanh thu TẤT CẢ đơn COMPLETED (không giới hạn thời gian)
        Double allTimeRevenue = ordersRepository.calculateTotalRevenue();
        Long completedOrdersCount = ordersRepository.countCompletedOrders();
        
        System.out.println("=== DEBUG REVENUE ===");
        System.out.println("Today Revenue: " + todayRevenue + " (from " + startOfDay + " to " + now + ")");
        System.out.println("Week Revenue: " + weekRevenue + " (from " + startOfWeek + " to " + now + ")");
        System.out.println("Month Revenue: " + monthRevenue + " (from " + startOfMonth + " to " + now + ")");
        System.out.println("ALL TIME Revenue: " + allTimeRevenue);
        System.out.println("Completed Orders: " + completedOrdersCount);
        System.out.println("====================");
        
        Long newOrdersCount = ordersRepository.countByStatus("PENDING");
        Long totalOrders = ordersRepository.count();
        long totalCustomers = userRepository.count();
        
        List<Book> lowStockBooks = bookRepository.findFeaturedBooks(PageRequest.of(0, 20))
                .stream().filter(b -> b.getQuantity() != null && b.getQuantity() < 10)
                .collect(Collectors.toList());
        lowStockBooks.sort(Comparator.comparingInt(b -> b.getQuantity() != null ? b.getQuantity() : 0));
        
        // Revenue stats
        model.addAttribute("todayRevenue", todayRevenue != null ? todayRevenue : 0.0);
        model.addAttribute("weekRevenue", weekRevenue != null ? weekRevenue : 0.0);
        model.addAttribute("monthRevenue", monthRevenue != null ? monthRevenue : 0.0);
        model.addAttribute("totalRevenue", monthRevenue != null ? monthRevenue : 0.0); // Hiển thị doanh thu tháng
        model.addAttribute("allTimeRevenue", allTimeRevenue != null ? allTimeRevenue : 0.0);
        model.addAttribute("completedOrdersCount", completedOrdersCount != null ? completedOrdersCount : 0);
        
        // Order stats
        model.addAttribute("newOrdersCount", newOrdersCount != null ? newOrdersCount : 0);
        model.addAttribute("totalOrders", totalOrders != null ? totalOrders : 0);
        
        // Customer & Stock
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("lowStockCount", lowStockBooks.size());
        model.addAttribute("lowStockBooks", lowStockBooks.size() > 5 ? lowStockBooks.subList(0, 5) : lowStockBooks);
        
        // Top selling books  
        model.addAttribute("topSellingBooks", bookService.getBestSellingBooks());
        
        // Recent orders
        model.addAttribute("recentOrders", ordersRepository.findTop10ByOrderByOrderDateDesc());
        
        // ✅ Chart data (revenue for last 7 days) - Cải thiện
        List<Map<String, Object>> chartData = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
        double maxRevenue = 0;
        double totalWeekRevenue = 0;
        
        System.out.println("=== CHART DATA DEBUG ===");
        for (int i = 6; i >= 0; i--) {
            LocalDateTime dayStart = now.minusDays(i).withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime dayEnd = dayStart.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            Double dayRevenue = orderService.calculateRevenue(dayStart, dayEnd);
            double revenueValue = dayRevenue != null ? dayRevenue : 0.0;
            
            String dateStr = dayStart.format(fmt);
            System.out.println("Ngày " + dateStr + ": " + revenueValue + "đ");
            
            Map<String, Object> point = new HashMap<>();
            point.put("date", dateStr);
            point.put("amount", revenueValue);
            chartData.add(point);
            
            totalWeekRevenue += revenueValue;
            if (revenueValue > maxRevenue) {
                maxRevenue = revenueValue;
            }
        }
        
        System.out.println("Tổng tuần: " + totalWeekRevenue + "đ");
        System.out.println("Max: " + maxRevenue + "đ");
        System.out.println("========================");
        
        model.addAttribute("revenueChartData", chartData);
        model.addAttribute("maxRevenue", maxRevenue);
        
        return "admin/dashboard";
    }

    @GetMapping("/stats/revenue")
    @ResponseBody
    public Map<String, Object> getRevenueStats() {
        Map<String, Object> stats = new HashMap<>();
        
        LocalDateTime last7Days = LocalDateTime.now().minusDays(7);
        
        stats.put("last7Days", orderService.calculateRevenue(last7Days, LocalDateTime.now()));
        
        return stats;
    }
}