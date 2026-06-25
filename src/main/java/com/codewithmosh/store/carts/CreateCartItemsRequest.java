package com.codewithmosh.store.carts;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCartItemsRequest {

    @NotNull
    private Long productId;
}
