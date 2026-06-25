package com.codewithmosh.store.carts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    @Mapping(source = "cartItems", target = "items")
    CreateCartResponse toDto(Cart cart);

    @Mapping(source = "product", target = "product")
    @Mapping(target = "totalPrice" , expression = "java(cartItem.getTotalPrice())")
    CreateCartItemsResponse toItemDto(CartItem cartItem);
}
