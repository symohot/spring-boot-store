package com.codewithmosh.store.carts;

import com.codewithmosh.store.products.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
@Tag(name = "Carts")
public class CartController {

    private CartService cartService;

    @PostMapping
    public ResponseEntity<CreateCartResponse> createCart(UriComponentsBuilder uriComponentsBuilder) {

        CreateCartResponse createCartResponse = cartService.creatCart();

        URI uri = uriComponentsBuilder.path("/carts/{id}").buildAndExpand(createCartResponse.getId()).toUri();

        return ResponseEntity.created(uri).body(createCartResponse);
    }

    @Operation(summary = "Add items to cart")
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CreateCartItemsResponse> addToCart(@Parameter(description = "The id of the cart") @PathVariable(name = "cartId") UUID id,
                                                             @Valid @RequestBody CreateCartItemsRequest createCartItemsRequest) {

        CreateCartItemsResponse createCartItemsResponse = cartService.addToCart(id, createCartItemsRequest.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createCartItemsResponse);
    }

    @GetMapping("/{cartId}")
    public CreateCartResponse getCart(@PathVariable(name = "cartId") UUID cartId) {

        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CreateCartResponse updateItem(@PathVariable(name = "cartId") UUID cartId,
                                        @PathVariable(name = "productId") Long productId,
                                        @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest){

        return cartService.updateItem(cartId, productId, updateCartItemRequest.getQuantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable(name = "cartId") UUID cartId,
                                        @PathVariable(name = "productId") Long productId) {

        cartService.removeItem(cartId, productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable(name = "cartId") UUID cartId) {

        cartService.clearCart(cartId);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found in the cart"));
    }
}
