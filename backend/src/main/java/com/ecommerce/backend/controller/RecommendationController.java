package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.ProductDTO;
import com.ecommerce.backend.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getRecommendations() {
        return ResponseEntity.ok(recommendationService.getRecommendations());
    }
}