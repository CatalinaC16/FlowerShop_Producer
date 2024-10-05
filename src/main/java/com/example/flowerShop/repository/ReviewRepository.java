package com.example.flowerShop.repository;


import com.example.flowerShop.entity.Product;
import com.example.flowerShop.entity.Review;
import com.example.flowerShop.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findAllByProduct(Product product);

    @Transactional
    void deleteAllByUser(User user);
}
