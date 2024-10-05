package com.example.flowerShop.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetailedDTO {

    private UUID id;
    private UUID id_product;
    private UUID id_user;
    private String text;
    private int rating;
}
