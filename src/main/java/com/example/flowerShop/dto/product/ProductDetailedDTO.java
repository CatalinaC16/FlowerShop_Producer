package com.example.flowerShop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailedDTO {

    private UUID id;
    private String name;
    private String description;
    private String imageUrl;
    private Double price;
    private Integer stock;
    private String category;
}
