package com.pandora.backend.model.response.order;

import com.pandora.backend.model.dto.DiscountDTO;
import com.pandora.backend.model.dto.OrderDetailDTO;
import com.pandora.backend.model.entity.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {

    private Long id;

    private String shipName;

    private String shipAddress;

    private String billingAddress;

    private String city;

    private String state;

    private String zip;

    private String country;

    private String phone;

    private Float totalPrice;

    private Float totalCargoPrice;

    private Long date;

    private OrderStatus shipped;

    private String cargoFirm;

    private String trackingNumber;

    private DiscountDTO discount;

    private List<OrderDetailDTO> orderItems;

}
