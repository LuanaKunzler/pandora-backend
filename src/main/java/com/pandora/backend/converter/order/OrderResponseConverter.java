package com.pandora.backend.converter.order;

import com.pandora.backend.model.dto.CategoryDTO;
import com.pandora.backend.model.dto.DiscountDTO;
import com.pandora.backend.model.dto.OrderDetailDTO;
import com.pandora.backend.model.entity.Order;
import com.pandora.backend.model.response.order.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OrderResponseConverter implements Function<Order, OrderResponse> {
    @Override
    public OrderResponse apply(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setShipName(order.getShipName());
        orderResponse.setShipAddress(order.getShipAddress());
        orderResponse.setBillingAddress(order.getBillingAddress());
        orderResponse.setCity(order.getCity());
        orderResponse.setState(order.getState());
        orderResponse.setZip(order.getZip());
        orderResponse.setCountry(order.getCountry());
        orderResponse.setPhone(order.getPhone());
        orderResponse.setTotalPrice(order.getTotalPrice());
        orderResponse.setTotalCargoPrice(order.getTotalCargoPrice());
        orderResponse.setDate(order.getDate().getTime());
        orderResponse.setShipped(order.getShipped());
        orderResponse.setCargoFirm(order.getCargoFirm());
        orderResponse.setTrackingNumber(order.getTrackingNumber());
        if (Objects.nonNull(order.getDiscount())) {
            orderResponse.setDiscount(DiscountDTO
                    .builder()
                    .discountPercent(order.getDiscount().getDiscountPercent())
                    .status(order.getDiscount().getStatus())
                    .build()
            );
        }

        orderResponse.setOrderItems(
                order.getOrderDetailList()
                        .stream()
                        .map(orderDetails -> OrderDetailDTO
                                .builder()
                                .imageUrl(orderDetails.getBook().getImageUrl())
                                .bookUrl(orderDetails.getBook().getBookUrl())
                                .title(orderDetails.getBook().getTitle())
                                .price(orderDetails.getBook().getUnitPrice())
                                .cargoPrice(orderDetails.getBook().getCargoPrice())
                                .amount(orderDetails.getAmount())
                                .category(CategoryDTO
                                        .builder()
                                        .name(orderDetails.getBook().getBookCategory().getName())
                                        .build())
                                .build()
                        ).collect(Collectors.toList())
        );

        return orderResponse;
    }
}
