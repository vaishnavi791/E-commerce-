package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.CartDTO;
import com.ecommerce.backend.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<CartDTO> addItemToCart(@Valid @RequestBody CartDTO cartDTO) {
        return new ResponseEntity<>(cartService.addItemToCart(cartDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartDTO> removeItemFromCart(@PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(productId));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartDTO> updateCartItemQuantity(@PathVariable Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateCartItemQuantity(productId, quantity));
    }

    @GetMapping
    public ResponseEntity<CartDTO> viewCart() {
        return ResponseEntity.ok(cartService.viewCart());
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}
