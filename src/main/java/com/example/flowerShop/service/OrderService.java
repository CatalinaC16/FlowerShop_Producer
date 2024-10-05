package com.example.flowerShop.service;

import com.example.flowerShop.dto.order.OrderDTO;
import com.example.flowerShop.dto.order.OrderDetailedDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    ResponseEntity<List<OrderDTO>> getAllOrders();

    ResponseEntity<OrderDTO> getOrderById(UUID id);

    ResponseEntity<List<OrderDTO>> getAllOrdersByUser(UUID id);

    ResponseEntity<String> addOrder(OrderDetailedDTO orderDetailedDTO);

    ResponseEntity<String> updateOrderById(UUID id, OrderDetailedDTO orderDetailedDTO);

    ResponseEntity<String> deleteOrderById(UUID id);
}
