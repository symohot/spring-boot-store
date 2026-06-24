package com.codewithmosh.store.payments;

import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.services.AuthService;
import com.codewithmosh.store.services.CartService;
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