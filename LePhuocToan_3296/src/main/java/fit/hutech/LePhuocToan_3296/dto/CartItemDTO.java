package fit.hutech.LePhuocToan_3296.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long bookId;
    private String title;
    private Double price;
    private Integer quantity;
    private String image;
    private Double subtotal;
}