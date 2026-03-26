package fit.hutech.LePhuocToan_3296.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(
            ResourceNotFoundException ex, 
            Model model) {
        
        log.error("Resource not found: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(InsufficientStockException.class)
    public String handleInsufficientStock(
            InsufficientStockException ex, 
            Model model) {
        
        log.warn("Insufficient stock: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error/stock-error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(
            Exception ex, 
            HttpServletRequest request,
            Model model) {
        
        log.error("Unexpected error at {}: {}", 
                  request.getRequestURI(), ex.getMessage(), ex);
        
        model.addAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại!");
        model.addAttribute("url", request.getRequestURI());
        return "error/500";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(Model model) {
        model.addAttribute("error", "Trang không tồn tại");
        return "error/404";
    }
}