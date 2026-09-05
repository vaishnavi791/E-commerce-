package com.ecommerce.backend.repository;

import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {

    List<UserInteraction> findTop50ByUserOrderByCreatedAtDesc(User user);
}