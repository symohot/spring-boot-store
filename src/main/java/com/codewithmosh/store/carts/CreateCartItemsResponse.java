package com.codewithmosh.store.carts;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCartItemsResponse {

    private ProductInCart product;

    private Integer quantity;

    private BigDecimal totalPrice;
}
