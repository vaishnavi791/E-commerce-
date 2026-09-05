package com.ecommerce.backend.service;

import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.UserInteraction;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.UserInteractionRepository;
import com.ecommerce.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserInteractionService {

    private final UserInteractionRepository interactionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public UserInteractionService(UserInteractionRepository interactionRepository,
                                  UserRepository userRepository,
                                  ProductRepository productRepository) {
        this.interactionRepository = interactionRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public void record(String interactionType, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        interactionRepository.save(new UserInteraction(getCurrentUser(), product, interactionType));
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}