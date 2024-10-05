package com.example.flowerShop.dto.customProduct;

import com.example.flowerShop.entity.Product;
import com.example.flowerShop.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CustomProductDTO {

    private UUID id;
    private String name;
    private String description;
    private Double price;
    private User user;
    private Collection<Integer> quantities;
    private List<Product> products;
}
