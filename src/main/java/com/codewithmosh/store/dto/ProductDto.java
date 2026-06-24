package com.codewithmosh.store.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ProductDto {

    private Long id;


    private String name;


    private String description;


    private BigDecimal price;

    private byte categoryId;
}
