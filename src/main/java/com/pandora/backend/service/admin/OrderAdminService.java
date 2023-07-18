package com.pandora.backend.service.admin;

import com.pandora.backend.model.entity.Order;
import com.pandora.backend.model.request.order.UpdateOrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface OrderAdminService {
    Page<Order> getOrders(Pageable pageable);

    Order getOrderById(Long id);

    int getBooksSoldByMonth(int year, int month);

    List<Order> getRecentOrders();

    BigDecimal getTotalRevenueByMonth(int year, int month);

    BigDecimal getTotalSalesAmount();

    Order updateOrder(Long id, UpdateOrderRequest updateOrderRequest);

    void deleteOrder(Long id);
}
