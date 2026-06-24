package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dto.ProductDto;
import com.codewithmosh.store.entities.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target="categoryId", source = "category.id")
    ProductDto toDto (Product product);

    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity (ProductDto productDto);

    @Mapping(target = "id", source = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(ProductDto productDto, @MappingTarget Product product);
}
