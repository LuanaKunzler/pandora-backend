package com.pandora.backend.converter.cart;

import com.pandora.backend.model.dto.CartItemDTO;
import com.pandora.backend.model.dto.DiscountDTO;
import com.pandora.backend.model.entity.Cart;
import com.pandora.backend.model.response.cart.CartResponse;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CartResponseConverter implements Function<Cart, CartResponse> {
    @Override
    public CartResponse apply(Cart cart) {
        CartResponse cartResponse = new CartResponse();

        cartResponse.setCartItems(cart.getCartItemList()
                .stream()
                .map(cartItem -> CartItemDTO
                        .builder()
                        .id(cartItem.getId())
                        .bookUrl(cartItem.getBook().getBookUrl())
                        .imageUrl(cartItem.getBook().getImageUrl())
                        .title(cartItem.getBook().getTitle())
                        .price(cartItem.getBook().getUnitPrice())
                        .amount(cartItem.getAmount())
                        .unitsInStock(cartItem.getBook().getUnitsInStock())
                        .build())
                .collect(Collectors.toList()));

        if (Objects.nonNull(cart.getDiscount())) {
            cartResponse.setDiscount(DiscountDTO
                    .builder()
                    .discountPercent(cart.getDiscount().getDiscountPercent())
                    .status(cart.getDiscount().getStatus())
                    .build()
            );
        }

        cartResponse.setTotalCartPrice(cart.getTotalCartPrice());
        cartResponse.setTotalCargoPrice(cart.getTotalCargoPrice());
        cartResponse.setTotalPrice(cart.getTotalPrice());
        return cartResponse;
    }
}
