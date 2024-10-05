package com.example.flowerShop.service;

import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.dto.review.ReviewDTO;
import com.example.flowerShop.dto.review.ReviewDetailedDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    ResponseEntity<List<ReviewDTO>> getAllReviewsByProductId(UUID id);

    ResponseEntity<List<ReviewDTO>> getAllReviews();

    ResponseEntity<List<ProductDetailedDTO>> getAllProductsForReview();

    ResponseEntity<ReviewDTO> getReviewById(UUID id);

    ResponseEntity<String> addReview(ReviewDetailedDTO detailedDTO);

    ResponseEntity<String> updateReviewById(UUID id, ReviewDetailedDTO reviewDetailedDTO);

    ResponseEntity<String> deleteReviewById(UUID id);
}
