package com.example.flowerShop.dto.orderItem;

import com.example.flowerShop.entity.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDetailedDTO {

    private UUID id;
    private int quantity;
    private UUID id_product;
    @JsonIgnore
    private Order order;
}
