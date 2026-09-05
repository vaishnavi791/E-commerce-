package com.ecommerce.backend.dto;

import java.util.List;

public record RecommendationResponse(List<Long> recommendedProductIds) {
}