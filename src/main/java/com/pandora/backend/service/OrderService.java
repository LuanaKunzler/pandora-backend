package com.pandora.backend.service;

import com.pandora.backend.model.request.order.PostOrderRequest;
import com.pandora.backend.model.response.order.OrderResponse;

import java.util.List;

public interface OrderService {
    Integer getAllOrdersCount();

    List<OrderResponse> getAllOrders(Integer page, Integer pageSize);

    OrderResponse postOrder(PostOrderRequest postOrderRequest);
}
