package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.CartDTO;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserInteractionService interactionService;
    private final CartPricingService cartPricingService;

    public CartService(CartRepository cartRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository,
                       UserInteractionService interactionService,
                       CartPricingService cartPricingService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.interactionService = interactionService;
        this.cartPricingService = cartPricingService;
    }

    @Transactional
    public CartDTO addItemToCart(CartDTO cartDTO) {
        User currentUser = getCurrentUser();
        Product product = productRepository.findById(cartDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + cartDTO.getProductId()));

        Cart cart = cartRepository.findByUser(currentUser)
                .orElseGet(() -> createCart(currentUser));

        cart.getProducts().add(product);

        cartRepository.save(cart);
        interactionService.record("CART_ADD", product.getId());
        return toCartDTO(cart);
    }

    @Transactional
    public CartDTO removeItemFromCart(Long productId) {
        User currentUser = getCurrentUser();
        Cart cart = cartRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.getProducts().removeIf(product -> product.getId().equals(productId));
        cartRepository.save(cart);
        return toCartDTO(cart);
    }

    @Transactional
    public CartDTO updateCartItemQuantity(Long productId, int quantity) {
        User currentUser = getCurrentUser();
        Cart cart = cartRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.getProducts().removeIf(product -> product.getId().equals(productId));
        if (quantity > 0) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
            for (int i = 0; i < quantity; i++) {
                cart.getProducts().add(product);
            }
        }
        cartRepository.save(cart);
        return toCartDTO(cart);
    }

    public CartDTO viewCart() {
        User currentUser = getCurrentUser();
        Cart cart = cartRepository.findByUser(currentUser)
                .orElseGet(() -> createCart(currentUser));
        return toCartDTO(cart);
    }

    @Transactional
    public void clearCart() {
        User currentUser = getCurrentUser();
        Cart cart = cartRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        cart.getProducts().clear();
        cartRepository.save(cart);
    }

    private Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        return cartRepository.save(cart);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private CartDTO toCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getId());
        cartDTO.setProductIds(cart.getProducts().stream().map(Product::getId).toList());
        cartDTO.setProductNames(cart.getProducts().stream().map(Product::getPName).toList());
        cartDTO.setSubtotal(cartPricingService.calculateTotal(cart.getProducts()));
        return cartDTO;
    }
}
