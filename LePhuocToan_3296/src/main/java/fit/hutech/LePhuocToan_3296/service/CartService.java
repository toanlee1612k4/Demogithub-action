package fit.hutech.LePhuocToan_3296.service;

import fit.hutech.LePhuocToan_3296.entity.Book;
import fit.hutech.LePhuocToan_3296.entity.Cart;
import fit.hutech.LePhuocToan_3296.entity.User;
import fit.hutech.LePhuocToan_3296.repository.BookRepository;
import fit.hutech.LePhuocToan_3296.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;

    @Override
    public List<Cart> getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    @Override
    public void addToCart(User user, Long bookId, Integer quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        Optional<Cart> existingCart = cartRepository.findByUserAndBook(user, book);
        
        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            cartRepository.save(cart);
        } else {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setBook(book);
            newCart.setQuantity(quantity);
            cartRepository.save(newCart);
        }
    }

    @Override
    public void updateCartItem(Long cartId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        if (cart.getBook().getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        cart.setQuantity(quantity);
        cartRepository.save(cart);
    }

    @Override
    public void removeCartItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    @Override
    public void clearCart(User user) {
        cartRepository.deleteByUser(user);
    }

    @Override
    public Double calculateTotal(User user) {
        List<Cart> carts = getCartByUser(user);
        return carts.stream()
                .mapToDouble(cart -> cart.getBook().getPrice() * cart.getQuantity())
                .sum();
    }

    @Override
    public Integer getCartCount(User user) {
        return cartRepository.countByUser(user).intValue();
    }
}