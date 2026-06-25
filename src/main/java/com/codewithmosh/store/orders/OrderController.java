package com.codewithmosh.store.orders;

import com.codewithmosh.store.common.ErrorDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {


    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getAllOrder() {
        /*User currentUser = authService.getCurrentUser();
        List<Order> orders = orderService.findOrderByUserId(currentUser.getId());

        List<OrderDto> list = orders.stream().map(orderMapper::toDto).toList();

        List<OrderDto> orderList = new ArrayList<>();

        orders.forEach(order -> {
            OrderDto orderDto = new OrderDto();

            orderDto.setId(order.getId());
            orderDto.setOrderDate(order.getCreatedAt());
            orderDto.setStatus(String.valueOf(order.getStatus()));

            order.getItems().forEach(data -> {
                OrderItemDto orderItemDto = new OrderItemDto();
                orderItemDto.setProduct(new OrderProductDto());

                orderItemDto.getProduct().setId(data.getProduct().getId());
                orderItemDto.getProduct().setName(data.getProduct().getName());
                orderItemDto.getProduct().setPrice(data.getUnitPrice());
                orderItemDto.setQuantity(data.getQuantity());
                orderItemDto.setTotalPrice(data.getTotalPrice());

                orderDto.getItems().add(orderItemDto);
                orderDto.setTotalPrice(orderDto.getTotalPrice().add(orderItemDto.getTotalPrice()));
            });
            orderList.add(orderDto);
        });*/

        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable(name = "orderId") Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDto> handleOrderNotFoundException(OrderNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(e.getMessage()));
    }
}
