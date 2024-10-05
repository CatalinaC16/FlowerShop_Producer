package com.example.flowerShop.service;

import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.dto.promotion.PromotionDTO;
import com.example.flowerShop.dto.promotion.PromotionDetailedDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface PromotionService {

    ResponseEntity<List<PromotionDTO>> getAllPromotions();

    ResponseEntity<PromotionDTO> getPromotionById(UUID id);

    ResponseEntity<String> addPromotion(PromotionDetailedDTO promotionDetailedDTO);

    ResponseEntity<String> updatePromotionById(UUID id, PromotionDetailedDTO promotionDetailedDTO);

    ResponseEntity<String> deletePromotionById(UUID id);

    ResponseEntity<List<ProductDetailedDTO>> getAllProductsForPromotion();
}
