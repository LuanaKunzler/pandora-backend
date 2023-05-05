package com.pandora.backend.model.response.cart;

import com.pandora.backend.model.dto.CartItemDTO;
import com.pandora.backend.model.dto.DiscountDTO;
import lombok.Data;

import java.util.List;

@Data
public class CartResponse {
    private List<CartItemDTO> cartItems;
    private DiscountDTO discount;
    private Float totalCartPrice;
    private Float totalCargoPrice;
    private Float totalPrice;
}
