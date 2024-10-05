package com.example.flowerShop.dto.customProduct;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CustomProductDetailedDTO {

    private UUID id;
    private String name;
    private String description;
    private Double price;
    private UUID id_user;
    private Collection<Integer> quantities;
    private List<UUID> id_products;
}
