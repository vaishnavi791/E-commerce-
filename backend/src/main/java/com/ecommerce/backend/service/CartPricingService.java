package com.ecommerce.backend.service;

import com.ecommerce.backend.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartPricingService {

    public double calculateTotal(List<Product> products) {
        return products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }
}
