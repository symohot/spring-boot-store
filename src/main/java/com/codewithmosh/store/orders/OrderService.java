package com.codewithmosh.store.orders;

import com.codewithmosh.store.auth.AuthService;
import com.codewithmosh.store.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders() {
        User currentUser = authService.getCurrentUser();
        List<Order> orders = orderRepository.getOrdersByCustomer(currentUser);

        return  orders.stream().map(orderMapper::toDto).toList();
    }

    public List<Order> findOrderByUserId(Long userId) {
        return orderRepository.findByCustomerId(userId);
    }

    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository
                .getOrderWithItems(orderId)
                .orElseThrow(OrderNotFoundException::new);

        User currentUser = authService.getCurrentUser();

        if (!order.isPlacedBy(currentUser))
            throw new AccessDeniedException("You do not have permission to access this order");

        return orderMapper.toDto(order);
    }
}
