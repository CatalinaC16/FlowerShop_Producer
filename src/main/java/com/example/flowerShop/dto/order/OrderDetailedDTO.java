package com.example.flowerShop.dto.order;


import com.example.flowerShop.utils.order.OrderStatus;
import com.example.flowerShop.utils.order.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderDetailedDTO {

    private UUID id;
    private OrderStatus status;
    private String address;
    private PaymentType pay;
    private Long totalPrice;
    private UUID id_user;
    private LocalDateTime orderDate;
    private List<UUID> id_orderItems;
}
