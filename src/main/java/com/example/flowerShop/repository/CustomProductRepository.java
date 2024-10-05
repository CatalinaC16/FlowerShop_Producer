package com.example.flowerShop.repository;

import com.example.flowerShop.entity.CustomProduct;
import com.example.flowerShop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomProductRepository extends JpaRepository<CustomProduct, UUID> {

    Optional<CustomProduct> findById(@Param("id") UUID id);

    List<CustomProduct> findByUser(User user);
}
