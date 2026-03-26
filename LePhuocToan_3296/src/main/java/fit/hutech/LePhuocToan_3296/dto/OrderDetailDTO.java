package fit.hutech.LePhuocToan_3296.dto;

import lombok.Data;

@Data
public class OrderDetailDTO {
    private String bookTitle;
    private Integer quantity;
    private Double price;
    private Double subtotal;
}