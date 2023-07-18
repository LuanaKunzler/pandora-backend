package com.pandora.backend.controller.adminController;

import com.pandora.backend.model.entity.Discount;
import com.pandora.backend.model.entity.Order;
import com.pandora.backend.model.entity.User;
import com.pandora.backend.model.request.admin.user.UserUpdateRequest;
import com.pandora.backend.model.request.order.UpdateOrderRequest;
import com.pandora.backend.service.admin.OrderAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
public class OrderAdminController extends ApiAdminController{

    private final OrderAdminService service;

    @Autowired
    public OrderAdminController(OrderAdminService service) {
        this.service = service;
    }

    @GetMapping(value = "/orders")
    public ResponseEntity<Page<Order>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date,desc") String sort) {

        try {
            Sort.Order[] orders = Arrays.stream(sort.split(","))
                    .map(field -> {
                        String[] parts = field.split(":");
                        String fieldName = parts[0];
                        Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("asc")
                                ? Sort.Direction.ASC
                                : Sort.Direction.DESC;
                        return new Sort.Order(direction, fieldName);
                    })
                    .toArray(Sort.Order[]::new);

            Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
            Page<Order> ordersPage = service.getOrders(pageable);
            return new ResponseEntity<>(ordersPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/order/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        try {
            var order = service.getOrderById(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/order/sold/{year}/{month}")
    @ResponseStatus(HttpStatus.OK)
    public int getBooksSoldByMonth(@PathVariable int year, @PathVariable int month) {
        return service.getBooksSoldByMonth(year, month);
    }

    @GetMapping("/order/recent")
    public List<Order> getRecentOrders() {
        return service.getRecentOrders();
    }

    @GetMapping("/order/revenue/{year}/{month}")
    public BigDecimal getTotalRevenueByMonth(@PathVariable int year, @PathVariable int month) {
        return service.getTotalRevenueByMonth(year, month);
    }

    @GetMapping("/order/total-sales-amount")
    public ResponseEntity<BigDecimal> getTotalSalesAmount() {
        BigDecimal totalAmount = service.getTotalSalesAmount();
        return ResponseEntity.ok(totalAmount);
    }

    @PutMapping("/order/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody UpdateOrderRequest updateRequest) {
        Order updatedOrder = service.updateOrder(id, updateRequest);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable Long id) {
        try {
            service.deleteOrder(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
