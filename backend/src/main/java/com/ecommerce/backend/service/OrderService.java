package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.OrderDTO;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.repository.OrderRepository;
import com.ecommerce.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final UserInteractionService interactionService;
    private final CartPricingService cartPricingService;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        UserRepository userRepository,
                        UserInteractionService interactionService,
                        CartPricingService cartPricingService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.interactionService = interactionService;
        this.cartPricingService = cartPricingService;
    }

    @Transactional
    public OrderDTO checkout() {
        User currentUser = getCurrentUser();
        var cart = cartRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getProducts().isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        List<Product> purchasedProducts = new ArrayList<>(cart.getProducts());
        double totalAmount = cartPricingService.calculateTotal(purchasedProducts);

        Order order = new Order();
        order.setUser(currentUser);
        order.setProducts(purchasedProducts);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");
        order.setTotalAmount(totalAmount);

        orderRepository.save(order);
        purchasedProducts.forEach(product -> interactionService.record("PURCHASE", product.getId()));
        cart.getProducts().clear();
        cartRepository.save(cart);

        return toOrderDTO(order);
    }

    public List<OrderDTO> getOrderHistory() {
        User currentUser = getCurrentUser();
        return orderRepository.findByUser(currentUser).stream().map(this::toOrderDTO).toList();
    }

    @Transactional
    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setStatus("CANCELLED");
        return toOrderDTO(orderRepository.save(order));
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private OrderDTO toOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setProductIds(order.getProducts().stream().map(Product::getId).toList());
        orderDTO.setCustomerEmail(order.getUser().getEmail());
        return orderDTO;
    }
}
