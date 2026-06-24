package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dto.CreateCartItemsResponse;
import com.codewithmosh.store.dto.CreateCartResponse;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
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
