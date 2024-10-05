package com.example.flowerShop.dto.shoppingCart;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ShoppingCartDetailedDTO {
    private UUID id;
    private UUID id_order;
    private Long totalPrice;
    private UUID id_user;
    private List<UUID> id_orderItems;
}
