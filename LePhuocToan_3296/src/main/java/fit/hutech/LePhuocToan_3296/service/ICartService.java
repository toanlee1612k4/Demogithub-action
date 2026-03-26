package fit.hutech.LePhuocToan_3296.service;

import fit.hutech.LePhuocToan_3296.entity.Cart;
import fit.hutech.LePhuocToan_3296.entity.User;

import java.util.List;

public interface ICartService {
    List<Cart> getCartByUser(User user);
    void addToCart(User user, Long bookId, Integer quantity);
    void updateCartItem(Long cartId, Integer quantity);
    void removeCartItem(Long cartId);
    void clearCart(User user);
    Double calculateTotal(User user);
    Integer getCartCount(User user);
}