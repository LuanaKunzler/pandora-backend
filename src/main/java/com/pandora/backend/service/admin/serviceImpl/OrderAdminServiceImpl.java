package com.pandora.backend.service.admin.serviceImpl;

import com.pandora.backend.error.exception.InvalidArgumentException;
import com.pandora.backend.model.entity.Order;
import com.pandora.backend.model.entity.OrderDetail;
import com.pandora.backend.model.entity.Role;
import com.pandora.backend.model.entity.User;
import com.pandora.backend.model.request.admin.user.UserUpdateRequest;
import com.pandora.backend.model.request.order.UpdateOrderRequest;
import com.pandora.backend.repository.admin.OrderAdminRepository;
import com.pandora.backend.service.admin.OrderAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class OrderAdminServiceImpl implements OrderAdminService {

    private final OrderAdminRepository repository;

    public OrderAdminServiceImpl(OrderAdminRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Order> getOrders(Pageable pageable) {
        Sort.Order dateOrder = Sort.Order.desc("date");
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(dateOrder));
        return repository.findAll(pageable);
    }

    @Override
    public Order getOrderById(Long id) {
        if (Objects.isNull(id)) {
            throw new InvalidArgumentException("Null id");
        }

        return repository.findById(id).orElse(null);
    }

    @Override
    public int getBooksSoldByMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        Iterable<Order> orders = repository.findAll();

        int booksSoldByMonth = 0;
        for (Order order : orders) {
            LocalDate orderDate = order.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            YearMonth orderYearMonth = YearMonth.of(orderDate.getYear(), orderDate.getMonth());

            if (orderYearMonth.equals(yearMonth)) {
                List<OrderDetail> orderDetails = order.getOrderDetailList();
                for (OrderDetail orderDetail : orderDetails) {
                    booksSoldByMonth += orderDetail.getAmount();
                }
            }
        }

        return booksSoldByMonth;
    }

    @Override
    public List<Order> getRecentOrders() {
        return repository.findTop8ByOrderByDateDesc();
    }

    @Override
    public BigDecimal getTotalRevenueByMonth(int year, int month) {
        YearMonth targetMonth = YearMonth.of(year, month);
        Iterable<Order> orders = repository.findAll();

        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (Order order : orders) {
            YearMonth orderMonth = YearMonth.of(
                    order.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear(),
                    order.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth()
            );

            if (orderMonth.equals(targetMonth)) {
                BigDecimal orderTotal = BigDecimal.valueOf(order.getTotalPrice());
                totalRevenue = totalRevenue.add(orderTotal);
            }
        }

        return totalRevenue.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getTotalSalesAmount() {
        Iterable<Order> orders = repository.findAll();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Order order : orders) {
            totalAmount = totalAmount.add(BigDecimal.valueOf(order.getTotalPrice()));
        }

        return totalAmount;
    }

    @Override
    public Order updateOrder(Long id, UpdateOrderRequest updateOrderRequest) {
        Order existingOrder = repository.findById(id).orElse(null);
        if (existingOrder == null) {
            throw new IllegalArgumentException("Order not found");
        }

        existingOrder.setShipped(updateOrderRequest.getShipped());
        existingOrder.setCargoFirm(updateOrderRequest.getCargoFirm());
        existingOrder.setTrackingNumber(updateOrderRequest.getTrackingNumber());

        return repository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        if (Objects.isNull(id)) {
            throw new InvalidArgumentException("Null id");
        }

        repository.deleteById(id);
    }
}
