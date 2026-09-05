package com.ecommerce.backend.dto;

public record RecommendationProduct(Long id, String category, String subcategory,
                                    String brand, String description) {
}