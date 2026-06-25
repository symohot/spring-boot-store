package com.codewithmosh.store.carts;

import com.codewithmosh.store.products.Product;
import com.codewithmosh.store.products.ProductNotFoundException;
import com.codewithmosh.store.products.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CreateCartResponse creatCart(){
        return cartMapper.toDto(cartRepository.save(new Cart()));
    }

    public CreateCartItemsResponse addToCart(UUID id, Long productId) {

        Cart cart = cartRepository.findById(id).orElse(null);
        if (cart == null) throw new CartNotFoundException();

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) throw new ProductNotFoundException();

        CartItem cartItem = cart.addItem(product);

        cartRepository.save(cart);

        return cartMapper.toItemDto(cartItem);
    }

    public CreateCartResponse getCart(UUID id) {

        Cart cart = cartRepository.getCartsWithItems(id).orElse(null);

        if (cart == null) throw new CartNotFoundException();

        return cartMapper.toDto(cart);
    }

    public CreateCartResponse updateItem(UUID cartId, Long productId, Integer quantity) {

        Cart cart = cartRepository.getCartsWithItems(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException();

        CartItem cartItem = cart.getItem(productId);
        if (cartItem == null) throw new ProductNotFoundException();

        cartItem.setQuantity(quantity);

        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    public void removeItem(UUID cartId, Long productId) {

        Cart cart = cartRepository.getCartsWithItems(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException();

        cart.removeItem(productId);

        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId) {

        Cart cart = cartRepository.getCartsWithItems(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException();

        cart.clear();

        cartRepository.save(cart);
    }
}
