package com.example.flowerShop.repository;

import com.example.flowerShop.entity.ShoppingCart;
import com.example.flowerShop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {

    Optional<ShoppingCart> findByUser(User user);
}
