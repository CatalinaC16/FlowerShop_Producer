package com.example.flowerShop.dto.category;

import com.example.flowerShop.entity.Product;
import com.example.flowerShop.utils.category.CategoryName;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CategoryDTO {

    private UUID id;
    private CategoryName name;
    private String description;
    private List<Product> products;
}
