package com.codewithmosh.store.products;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(required = false, name = "categoryId") Byte categoryId) {

        if (categoryId != null) {
            return ResponseEntity.ok(
                    productRepository.findByCategoryId(categoryId).stream().map(productMapper::toDto).toList());
        } else {
            return ResponseEntity.ok(
                    productRepository.findAll().stream().map(productMapper::toDto).toList());
        }
    }

    @PostMapping
    public ResponseEntity<ProductDto> registerProduct(@RequestBody ProductDto productDto) {

        Product entity = productMapper.toEntity(productDto);

        entity = productRepository.save(entity);

        return ResponseEntity.ok(productMapper.toDto(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable(name = "id") Long id,
                                                    @RequestBody ProductDto productDto) {

        Product product = productRepository.findById(id).orElse(null);

        if (product == null) return ResponseEntity.notFound().build();

        productMapper.updateProduct(productDto, product);

        product = productRepository.save(product);

        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long id) {

        Product product = productRepository.findById(id).orElse(null);

        if (product == null) return ResponseEntity.notFound().build();

        productRepository.delete(product);

        return ResponseEntity.noContent().build();
    }
}
