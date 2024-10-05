package com.example.flowerShop.dto.promotion;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PromotionDetailedDTO {

    private UUID id;
    private String name;
    private Double discountPercentage;
    private List<UUID> id_products;
}
