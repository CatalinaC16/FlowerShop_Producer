package com.example.flowerShop.dto.promotion;

import com.example.flowerShop.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PromotionDTO {
    private UUID id;
    private String name;
    private Double discountPercentage;
    private List<Product> products;
}
