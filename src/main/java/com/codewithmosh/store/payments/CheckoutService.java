package com.codewithmosh.store.payments;

import com.codewithmosh.store.carts.Cart;
import com.codewithmosh.store.orders.Order;
import com.codewithmosh.store.carts.CartEmptyException;
import com.codewithmosh.store.carts.CartNotFoundException;
import com.codewithmosh.store.orders.OrderNotFoundException;
import com.codewithmosh.store.carts.CartRepository;
import com.codewithmosh.store.orders.OrderRepository;
import com.codewithmosh.store.auth.AuthService;
import com.codewithmosh.store.carts.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;
    private final StripePaymentGateway stripePaymentGateway;


    @Transactional
    public CheckoutResponse checkCart(CheckoutRequest request) {
        Cart cart = cartRepository.getCartsWithItems(request.getCartId()).orElse(null);
        if (cart == null) throw new CartNotFoundException();
        if (cart.isEmpty()) throw new CartEmptyException();

        Order order = Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);

        try {
            CheckoutSession checkoutSession = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(),checkoutSession.getCheckoutUrl());
        } catch (PaymentException e) {
            orderRepository.delete(order);
            throw e;
        }
    }

    public void handleWebhookEvent(WebhookRequest request) {
        paymentGateway
                .parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                    Order order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow(OrderNotFoundException::new);
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                });
        Optional<PaymentResult> paymentResult = stripePaymentGateway.parseWebhookRequest(request);
    }
}