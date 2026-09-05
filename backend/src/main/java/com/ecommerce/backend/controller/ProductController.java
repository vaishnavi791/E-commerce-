package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.ProductDTO;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.UserInteractionService;
import com.ecommerce.backend.util.MapperUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final UserInteractionService interactionService;

    public ProductController(ProductService productService, UserInteractionService interactionService) {
        this.productService = productService;
        this.interactionService = interactionService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO dto) {
        Product savedProduct = productService.addProduct(dto);
        return new ResponseEntity<>(MapperUtil.toProductDTO(savedProduct), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts().stream()
                .map(MapperUtil::toProductDTO)
                .toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        interactionService.record("VIEW", id);
        return ResponseEntity.ok(MapperUtil.toProductDTO(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(MapperUtil.toProductDTO(productService.updateProduct(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}