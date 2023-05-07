package com.pandora.backend.service.serviceImpl;

import com.pandora.backend.converter.cart.CartResponseConverter;
import com.pandora.backend.error.exception.InvalidArgumentException;
import com.pandora.backend.error.exception.ResourceNotFoundException;
import com.pandora.backend.model.dto.CartItemDTO;
import com.pandora.backend.model.entity.Book;
import com.pandora.backend.model.entity.Cart;
import com.pandora.backend.model.entity.CartItem;
import com.pandora.backend.model.entity.User;
import com.pandora.backend.model.request.cart.ConfirmCartRequest;
import com.pandora.backend.model.response.cart.CartResponse;
import com.pandora.backend.repository.CartRepository;
import com.pandora.backend.service.BookService;
import com.pandora.backend.service.CartService;
import com.pandora.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final BookService bookService;
    private final UserService userService;
    private final CartResponseConverter cartResponseConverter;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           BookService bookService,
                           UserService userService,
                           CartResponseConverter cartResponseConverter) {
        this.cartRepository = cartRepository;
        this.bookService = bookService;
        this.userService = userService;
        this.cartResponseConverter = cartResponseConverter;
    }

    @Override
    public CartResponse addToCart(Long bookId, Integer amount) {
        User user = userService.getUser();
        Cart cart = user.getCart();

        if (Objects.nonNull(cart) && Objects.nonNull(cart.getCartItemList()) && !cart.getCartItemList().isEmpty()) {
            Optional<CartItem> cartItem = cart.getCartItemList()
                    .stream()
                    .filter(ci -> ci.getBook().getId().equals(bookId)).findFirst();
            if (cartItem.isPresent()) {
                if (cartItem.get().getBook().getUnitsInStock() < (cartItem.get().getAmount() + amount)) {
                    throw new InvalidArgumentException("Book does not have desired stock.");
                }
                cartItem.get().setAmount(cartItem.get().getAmount() + amount);
                Cart updatedCart = calculatePrice(cart);
                cart = cartRepository.save(updatedCart);
                return cartResponseConverter.apply(cart);
            }
        }

        if (Objects.isNull(cart)) {
            cart = createCart(user);
        }

        Book book = bookService.findBookById(bookId);

        if (book.getUnitsInStock() < amount) {
            throw new InvalidArgumentException("Book does not have desired stock.");
        }

        CartItem cartItem = new CartItem();
        cartItem.setAmount(amount);
        cartItem.setBook(book);
        cartItem.setCart(cart);

        if (Objects.isNull(cart.getCartItemList())) {
            cart.setCartItemList(new ArrayList<>());
        }
        cart.getCartItemList().add(cartItem);
        cart = calculatePrice(cart);

        cart = cartRepository.save(cart);
        return cartResponseConverter.apply(cart);

    }

    @Override
    public CartResponse incrementCartItem(Long cartItemId, Integer amount) {

        User user = userService.getUser();
        Cart cart = user.getCart();
        if (Objects.isNull(cart) || Objects.isNull(cart.getCartItemList()) || cart.getCartItemList().isEmpty()) {
            throw new ResourceNotFoundException("Empty cart");
        }

        CartItem cartItem = cart.getCartItemList()
                .stream()
                .filter(cItem -> cItem.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found"));

        if (cartItem.getBook().getUnitsInStock() < (cartItem.getAmount() + amount)) {
            throw new InvalidArgumentException("Book does not have desired stock.");
        }

        cartItem.setAmount(cartItem.getAmount() + amount);
        cart = calculatePrice(cart);
        cart = cartRepository.save(cart);
        return cartResponseConverter.apply(cart);
    }

    @Override
    public CartResponse decrementCartItem(Long cartItemId, Integer amount) {

        User user = userService.getUser();
        Cart cart = user.getCart();
        if (Objects.isNull(cart) || Objects.isNull(cart.getCartItemList()) || cart.getCartItemList().isEmpty()) {
            throw new ResourceNotFoundException("Cart is Empty");
        }

        CartItem cartItem = cart.getCartItemList()
                .stream()
                .filter(cItem -> cItem.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found"));

        if (cartItem.getAmount() <= amount) {
            List<CartItem> cartItems = cart.getCartItemList();
            cartItems.remove(cartItem);
            if (Objects.isNull(cart.getCartItemList()) || cart.getCartItemList().isEmpty()) {
                user.setCart(null);
                userService.saveUser(user);
                return null;
            }
            cart.setCartItemList(cartItems);
            cart = calculatePrice(cart);
            cart = cartRepository.save(cart);
            return cartResponseConverter.apply(cart);
        }

        cartItem.setAmount(cartItem.getAmount() - amount);
        cart = calculatePrice(cart);
        cart = cartRepository.save(cart);
        return cartResponseConverter.apply(cart);
    }

    @Override
    public CartResponse fetchCart() {
        Cart cart = userService.getUser().getCart();
        if (cart == null) {
            return null;
        }
        return cartResponseConverter.apply(cart);
    }

    @Override
    public CartResponse removeFromCart(Long id) {
        User user = userService.getUser();
        Cart cart = user.getCart();

        if (Objects.isNull(cart) || Objects.isNull(cart.getCartItemList()) || cart.getCartItemList().isEmpty()) {
            throw new ResourceNotFoundException("Cart or CartItem not found");
        }

        List<CartItem> cartItems = cart.getCartItemList();
        Optional<CartItem> cartItem = cart.getCartItemList().stream()
                .filter(cItem -> cItem.getId().equals(id)).findFirst();
        if (cartItem.isEmpty()) {
            throw new ResourceNotFoundException("CartItem not found");
        }

        cartItems.remove(cartItem.get());
        if (Objects.isNull(cart.getCartItemList()) || cart.getCartItemList().isEmpty()) {
            user.setCart(null);
            userService.saveUser(user);
            return null;
        }

        cart.setCartItemList(cartItems);
        cart = calculatePrice(cart);
        cart = cartRepository.save(cart);
        return cartResponseConverter.apply(cart);
    }

    @Override
    public boolean confirmCart(ConfirmCartRequest confirmCartRequest) {

        Cart newCart = userService.getUser().getCart();
        if (Objects.isNull(newCart)) {
            return false;
        }
        List<CartItem> newCartItems = newCart.getCartItemList();
        List<CartItemDTO> cartItemsList = confirmCartRequest.getCartItems();
        if (newCartItems.size() != cartItemsList.size()) {
            return false;
        }

        for (int i = 0; i < newCartItems.size(); i++) {
            if (!newCartItems.get(i).getId().equals(cartItemsList.get(i).getId()) &&
                    !newCartItems.get(i).getAmount().equals(cartItemsList.get(i).getAmount()) &&
                    !newCartItems.get(i).getBook().getId().equals(cartItemsList.get(i).getId())) {
                return false;
            }
        }

        if (newCart.getTotalPrice().equals(confirmCartRequest.getTotalPrice()) &&
        newCart.getTotalCartPrice().equals(confirmCartRequest.getTotalCartPrice()) &&
        newCart.getTotalCartPrice().equals(confirmCartRequest.getTotalCartPrice())) {
            if (Objects.nonNull(newCart.getDiscount()) && Objects.nonNull(confirmCartRequest.getDiscount())) {
                return newCart.getDiscount().getDiscountPercent()
                        .equals(confirmCartRequest.getDiscount().getDiscountPercent());
            }
            return Objects.isNull(newCart.getDiscount()) && Objects.isNull(confirmCartRequest.getDiscount());
        }

        return false;
    }

    @Override
    public Cart getCart() {
        return userService.getUser().getCart();
    }

    @Override
    public void saveCart(Cart cart) {
        if (Objects.isNull(cart)) {
            throw new InvalidArgumentException("Cart is null");
        }
        cartRepository.save(cart);
    }

    @Override
    public void emptyCart() {
        User user = userService.getUser();
        user.setCart(null);
        userService.saveUser(user);
    }

    @Override
    public Cart calculatePrice(Cart cart) {
        cart.setTotalCartPrice(0F);
        cart.setTotalCargoPrice(12.99f);
        cart.setTotalPrice(0F);

        cart.getCartItemList().forEach(cartItem -> {
            cart.setTotalCartPrice(cart.getTotalCartPrice() +
                    (cartItem.getBook().getUnitPrice()) * cartItem.getAmount());
            cart.setTotalCargoPrice(12.99f);
            cart.setTotalPrice(
                    cart.getTotalPrice() + (cartItem.getBook().getUnitPrice() +
                            cart.getTotalCargoPrice()) * cartItem.getAmount());
        });

        if (Objects.nonNull(cart.getDiscount())) {
            cart.setTotalPrice(cart.getTotalPrice() - ((cart.getTotalPrice() *
                    cart.getDiscount().getDiscountPercent()) / 100));
        }

        cart.setTotalPrice(cart.getTotalPrice());
        cart.setTotalCargoPrice(cart.getTotalCargoPrice());
        return cart;
    }

    private Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cart;
    }
}
