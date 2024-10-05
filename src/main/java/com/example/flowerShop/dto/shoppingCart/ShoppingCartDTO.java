package com.example.flowerShop.dto.shoppingCart;

import com.example.flowerShop.entity.Order;
import com.example.flowerShop.entity.OrderItem;
import com.example.flowerShop.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ShoppingCartDTO {
    private UUID id;
    private Order order;
    private Long totalPrice;
    private User user;
    private List<OrderItem> orderItems;
}
