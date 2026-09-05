package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.ProductDTO;
import com.ecommerce.backend.dto.RecommendationInteraction;
import com.ecommerce.backend.dto.RecommendationProduct;
import com.ecommerce.backend.dto.RecommendationRequest;
import com.ecommerce.backend.dto.RecommendationResponse;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.UserInteractionRepository;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.util.MapperUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserInteractionRepository interactionRepository;
    private final RestClient mlClient;

    public RecommendationService(ProductRepository productRepository,
                                 UserRepository userRepository,
                                 UserInteractionRepository interactionRepository,
                                 @Value("${ml.service.url:http://localhost:8000}") String mlServiceUrl) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.interactionRepository = interactionRepository;
        this.mlClient = RestClient.builder().baseUrl(mlServiceUrl).build();
    }

    public List<ProductDTO> getRecommendations() {
        User currentUser = getCurrentUser();
        List<Product> products = productRepository.findAll();
        List<Product> fallback = products.stream()
                .sorted(Comparator.comparing(Product::getId).reversed())
                .limit(4)
                .toList();

        if (products.isEmpty()) {
            return List.of();
        }

        List<RecommendationProduct> productFeatures = products.stream()
                .map(product -> new RecommendationProduct(
                        product.getId(),
                        product.getCategory(),
                        product.getSubcategory(),
                        product.getBrand(),
                        product.getDescription()))
                .toList();
        List<RecommendationInteraction> interactions = interactionRepository
                .findTop50ByUserOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(interaction -> new RecommendationInteraction(
                        interaction.getProduct().getId(), interaction.getInteractionType()))
                .toList();

        try {
            RecommendationResponse response = mlClient.post()
                    .uri("/recommend")
                    .body(new RecommendationRequest(productFeatures, interactions, 4))
                    .retrieve()
                    .body(RecommendationResponse.class);
            return hydrate(response == null ? List.of() : response.recommendedProductIds(), products, fallback);
        } catch (RuntimeException exception) {
            return fallback.stream().map(MapperUtil::toProductDTO).toList();
        }
    }

    private List<ProductDTO> hydrate(List<Long> ids, List<Product> products, List<Product> fallback) {
        Map<Long, Product> productsById = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        List<ProductDTO> result = ids.stream()
                .map(productsById::get)
                .filter(product -> product != null)
                .map(MapperUtil::toProductDTO)
                .toList();
        return result.isEmpty()
                ? fallback.stream().map(MapperUtil::toProductDTO).toList()
                : result;
    }

    private User getCurrentUser() {
        String email = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}