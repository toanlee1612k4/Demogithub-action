package fit.hutech.LePhuocToan_3296.dto;

import lombok.Data;

@Data
public class CheckoutRequest {
    private String shippingAddress;
    private String phoneNumber;
    private String paymentMethod; // COD, BANKING, MOMO
    private String couponCode;
    private String note;
}