package com.example.flowerShop.utils.customProduct;

import com.example.flowerShop.dto.customProduct.CustomProductDetailedDTO;
import com.example.flowerShop.dto.promotion.PromotionDetailedDTO;
import com.example.flowerShop.entity.Product;
import com.example.flowerShop.entity.Promotion;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@NoArgsConstructor
public class CustomProductUtils {

    public boolean validateCustomProductMap(CustomProductDetailedDTO customProductDetailedDTO) {
        return !Objects.equals(customProductDetailedDTO.getName(), null)
                && !Objects.equals(customProductDetailedDTO.getId_products(), null)
                && !Objects.equals(customProductDetailedDTO.getQuantities(), null)
                && !Objects.equals(customProductDetailedDTO.getId_user(), null);
    }
}
