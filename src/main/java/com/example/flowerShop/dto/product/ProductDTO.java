package com.example.flowerShop.dto.product;

import com.example.flowerShop.entity.Category;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductDTO {

    private UUID id;
    private String name;
    private String description;
    private String imageUrl;
    private Double price;
    private Integer stock;
    private Category category;
}
