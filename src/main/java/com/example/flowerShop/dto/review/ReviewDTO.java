package com.example.flowerShop.dto.review;

import com.example.flowerShop.entity.Product;
import com.example.flowerShop.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ReviewDTO {

    private UUID id;
    private Product product;
    private User user;
    private String text;
    private int rating;
}
