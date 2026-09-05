package com.ecommerce.backend.dto;

import java.util.List;

public record RecommendationRequest(List<RecommendationProduct> products,
                                    List<RecommendationInteraction> interactions,
                                    int limit) {
}