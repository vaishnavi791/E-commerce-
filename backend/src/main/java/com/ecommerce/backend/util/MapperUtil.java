package com.ecommerce.backend.util;

import com.ecommerce.backend.dto.ProductDTO;
import com.ecommerce.backend.model.Product;

public final class MapperUtil {

    private MapperUtil() {
    }

    public static ProductDTO toProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setPName(product.getPName());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setCategory(product.getCategory());
        dto.setSubcategory(product.getSubcategory());
        dto.setBrand(product.getBrand());
        dto.setImageUrl(product.getImageUrl());
        return dto;
    }
}
