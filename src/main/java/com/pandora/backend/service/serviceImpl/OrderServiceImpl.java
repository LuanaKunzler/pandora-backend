package com.pandora.backend.service.serviceImpl;

import com.pandora.backend.converter.order.OrderResponseConverter;
import com.pandora.backend.error.exception.InvalidArgumentException;
import com.pandora.backend.error.exception.ResourceNotFoundException;
import com.pandora.backend.model.entity.*;
import com.pandora.backend.model.request.order.PostOrderRequest;
import com.pandora.backend.model.response.order.OrderResponse;
import com.pandora.backend.repository.OrderRepository;
import com.pandora.backend.service.CartService;
import com.pandora.backend.service.OrderService;
import com.pandora.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CartService cartService;
    private final OrderResponseConverter orderResponseConverter;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
            UserService userService,
            CartService cartService,
            OrderResponseConverter orderResponseConverter) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.cartService = cartService;
        this.orderResponseConverter = orderResponseConverter;
    }

    @Override
    public Integer getAllOrdersCount() {
        User user = userService.getUser();
        return orderRepository.countAllByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("An error occurred whilst fetching orders count"));
    }

    @Override
    public List<OrderResponse> getAllOrders(Integer page, Integer pageSize) {
        User user = userService.getUser();
        List<Order> orders = orderRepository.findAllByUserOrderByDateDesc(user, PageRequest.of(page, pageSize));
        return orders.stream()
                .map(orderResponseConverter)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse postOrder(PostOrderRequest postOrderRequest) {

        User user = userService.getUser();
        Cart cart = user.getCart();
        if (Objects.isNull(cart) || Objects.isNull(cart.getCartItemList())) {
            throw new InvalidArgumentException("Cart is not valid");
        }

        if (cart.getCartItemList().stream()
                .anyMatch(cartItem -> cartItem.getBook().getUnitsInStock() < cartItem.getAmount())) {
            throw new InvalidArgumentException("A book in your cart is out of stock.");
        }

        Order saveOrder = new Order();
        saveOrder.setUser(user);
        saveOrder.setShipName(postOrderRequest.getShipName());
        saveOrder.setPhone(postOrderRequest.getPhone());
        saveOrder.setShipAddress(postOrderRequest.getShipAddress());
        saveOrder.setBillingAddress(postOrderRequest.getBillingAddress());
        saveOrder.setCity(postOrderRequest.getCity());
        saveOrder.setCountry(postOrderRequest.getCountry());
        saveOrder.setState(postOrderRequest.getState());
        saveOrder.setZip(postOrderRequest.getZip());

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        saveOrder.setDate(date);

        saveOrder.setOrderDetailList(new ArrayList<>());

        cart.getCartItemList().forEach(cartItem -> {
            Book book = cartItem.getBook();
            Integer sellCount = book.getSellCount();
            int currentSellCount = sellCount != null ? sellCount : 0;
            book.setSellCount(currentSellCount + cartItem.getAmount());

            int currentUnitsInStock = book.getUnitsInStock();
            int updatedUnitsInStock = currentUnitsInStock - cartItem.getAmount();
            book.setUnitsInStock(updatedUnitsInStock);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setAmount(cartItem.getAmount());
            orderDetail.setOrder(saveOrder);
            orderDetail.setBook(cartItem.getBook());
            saveOrder.getOrderDetailList().add(orderDetail);
        });

        saveOrder.setTotalPrice(cart.getTotalPrice());
        saveOrder.setTotalCargoPrice(cart.getTotalCargoPrice());
        saveOrder.setDiscount(cart.getDiscount());
        saveOrder.setShipped(OrderStatus.IN_PREPARATION);

        Order order = orderRepository.save(saveOrder);
        cartService.emptyCart();
        return orderResponseConverter.apply(order);
    }
}
