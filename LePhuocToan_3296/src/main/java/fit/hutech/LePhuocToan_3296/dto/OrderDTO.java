package fit.hutech.LePhuocToan_3296.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Date orderDate;
    private Double totalPrice;
    private String status;
    private String paymentStatus;
    private String shippingAddress;
    private List<OrderDetailDTO> orderDetails;
}