package com.ecommerce.backend.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CartDTO {
    private Long cartId;
    @NotNull(message = "Product id is required")
    private Long productId;
    private List<Long> productIds;
    private List<String> productNames;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    public List<String> getProductNames() {
        return productNames;
    }

    public void setProductNames(List<String> productNames) {
        this.productNames = productNames;
    }
}
