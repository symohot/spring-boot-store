package com.codewithmosh.store.carts;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CreateCartResponse {

    private UUID id;

    private List<CreateCartItemsResponse> items = new ArrayList<>();

    private BigDecimal totalPrice= BigDecimal.ZERO;
}
