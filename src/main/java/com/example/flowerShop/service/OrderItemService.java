package com.example.flowerShop.service;

import com.example.flowerShop.dto.orderItem.OrderItemDTO;
import com.example.flowerShop.dto.orderItem.OrderItemDetailedDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface OrderItemService {

    ResponseEntity<List<OrderItemDTO>> getAllOrderItems();

    ResponseEntity<OrderItemDTO> getOrderItemById(UUID id);

    ResponseEntity<String> addOrderItem(OrderItemDetailedDTO orderItemDetailedDTO);

    ResponseEntity<String> updateOrderItemById(UUID id, OrderItemDetailedDTO orderItemDetailedDTO);

    ResponseEntity<String> deleteOrderItemById(UUID id);

    ResponseEntity<String> updateQuantityOrderItemById(UUID id, UUID id_cart, String action);
}
