package com.pandora.backend.controller;

import com.pandora.backend.error.exception.InvalidArgumentException;
import com.pandora.backend.model.request.order.PostOrderRequest;
import com.pandora.backend.model.response.order.OrderResponse;
import com.pandora.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
public class OrderControllerUser extends ApiUserController {

    private final OrderService orderService;

    @Autowired
    public OrderControllerUser(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/order/count")
    public ResponseEntity<Integer> getAllOrdersCount() {
        Integer orderCount = orderService.getAllOrdersCount();
        return new ResponseEntity<>(orderCount, HttpStatus.OK);
    }

    @GetMapping(value = "/order")
    public ResponseEntity<List<OrderResponse>> getAllOrders(@RequestParam("page") Integer page,
                                                            @RequestParam("size") Integer pageSize) {
        if (Objects.isNull(page) || page < 0) {
            throw new InvalidArgumentException("Invalid page");
        }
        if (Objects.isNull(pageSize) || pageSize < 0) {
            throw new InvalidArgumentException("Invalid pageSize");
        }
        List<OrderResponse> orders = orderService.getAllOrders(page, pageSize);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping(value = "/order")
    public ResponseEntity<OrderResponse> postOrder(@RequestBody @Valid PostOrderRequest postOrderRequest) {
        OrderResponse orderResponse = orderService.postOrder(postOrderRequest);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

}
