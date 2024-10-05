package com.example.flowerShop.dto.order;

import com.example.flowerShop.entity.OrderItem;
import com.example.flowerShop.entity.User;
import com.example.flowerShop.utils.order.OrderStatus;
import com.example.flowerShop.utils.order.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderDTO {

    private UUID id;
    private OrderStatus status;
    private String address;
    private PaymentType pay;
    private Long totalPrice;
    private User user;
    private LocalDateTime orderDate;
    private List<OrderItem> orderItems;
}
