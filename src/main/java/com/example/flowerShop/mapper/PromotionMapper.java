package com.example.flowerShop.mapper;

import com.example.flowerShop.dto.promotion.PromotionDTO;
import com.example.flowerShop.dto.promotion.PromotionDetailedDTO;
import com.example.flowerShop.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PromotionMapper implements Mapper<Promotion, PromotionDTO, PromotionDetailedDTO> {

    @Override
    public PromotionDetailedDTO convertToDTO(Promotion promotion) {

        if (promotion != null) {
            return PromotionDetailedDTO.builder()
                    .id(promotion.getId())
                    .name(promotion.getName())
                    .discountPercentage(promotion.getDiscountPercentage())
                    .id_products(this.productsToIdProducts(promotion.getProducts()))
                    .build();
        }
        return null;
    }

    private List<UUID> productsToIdProducts(List<Product> products) {
        return products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
    }

    @Override
    public Promotion convertToEntity(PromotionDTO promotionDTO) {

        if (promotionDTO != null) {
            return Promotion.builder()
                    .id(promotionDTO.getId())
                    .name(promotionDTO.getName())
                    .products(promotionDTO.getProducts())
                    .discountPercentage(promotionDTO.getDiscountPercentage())
                    .build();
        }
        return null;
    }

    public PromotionDTO convToDtoWithObjects(PromotionDetailedDTO promotionDetailedDTO, List<Product> products) {

        if (promotionDetailedDTO != null) {
            return PromotionDTO.builder()
                    .id(promotionDetailedDTO.getId())
                    .name(promotionDetailedDTO.getName())
                    .discountPercentage(promotionDetailedDTO.getDiscountPercentage())
                    .products(products)
                    .build();
        }
        return null;
    }

    public PromotionDTO convertEntToDtoWithObjects(Promotion promotion) {

        if (promotion != null) {
            return PromotionDTO.builder()
                    .id(promotion.getId())
                    .name(promotion.getName())
                    .discountPercentage(promotion.getDiscountPercentage())
                    .products(promotion.getProducts())
                    .build();
        }
        return null;
    }

    public List<PromotionDTO> convertListToDtoWithObjects(List<Promotion> source) {
        return source.stream()
                .map(this::convertEntToDtoWithObjects)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
