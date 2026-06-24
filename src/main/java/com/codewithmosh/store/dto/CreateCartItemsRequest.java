package com.codewithmosh.store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCartItemsRequest {

    @NotNull
    private Long productId;
}
