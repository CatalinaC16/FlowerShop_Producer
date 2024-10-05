package com.example.flowerShop.utils.promotion;

import com.example.flowerShop.dto.promotion.PromotionDetailedDTO;
import com.example.flowerShop.entity.Product;
import com.example.flowerShop.entity.Promotion;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@NoArgsConstructor
public class PromotionUtils {

    public boolean validatePromotionMap(PromotionDetailedDTO promotionDetailedDTO) {
        if (promotionDetailedDTO.getName() == null || promotionDetailedDTO.getDiscountPercentage() == null) {
            return false;
        }
        if (promotionDetailedDTO.getDiscountPercentage() < 1.0 || promotionDetailedDTO.getDiscountPercentage() > 100.0) {
            return false;
        }
        return true;
    }

    public static void updatePromotion(Promotion promotion, PromotionDetailedDTO promotionDetailedDTO, List<Product> products) {

        if (Objects.nonNull(promotionDetailedDTO.getId_products())) {
            promotion.setProducts(products);
        }
        if (Objects.nonNull(promotionDetailedDTO.getName())) {
            promotion.setName(promotionDetailedDTO.getName());
        }
        if (Objects.nonNull(promotionDetailedDTO.getDiscountPercentage())) {
            promotion.setDiscountPercentage(promotionDetailedDTO.getDiscountPercentage());
        }
    }
}
