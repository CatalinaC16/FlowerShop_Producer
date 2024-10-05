package com.example.flowerShop.mapper;

import com.example.flowerShop.dto.order.OrderDTO;
import com.example.flowerShop.dto.order.OrderDetailedDTO;
import com.example.flowerShop.entity.Order;
import com.example.flowerShop.entity.OrderItem;
import com.example.flowerShop.entity.User;
import org.springframework.stereotype.Component;;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMapper implements Mapper<Order, OrderDTO, OrderDetailedDTO> {

    @Override
    public OrderDetailedDTO convertToDTO(Order order) {

        if (order != null) {
            return OrderDetailedDTO.builder()
                    .id(order.getId())
                    .status(order.getStatus())
                    .address(order.getAddress())
                    .pay(order.getPay())
                    .totalPrice(order.getTotalPrice())
                    .id_user(order.getUser().getId())
                    .orderDate(order.getOrderDate())
                    .id_orderItems(this.orderItemsToOrderItemIds(order.getOrderItems()))
                    .build();
        }
        return null;
    }

    private List<UUID> orderItemsToOrderItemIds(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getId)
                .collect(Collectors.toList());
    }

    @Override
    public Order convertToEntity(OrderDTO orderDTO) {
        if (orderDTO != null) {
            return Order.builder()
                    .id(orderDTO.getId())
                    .status(orderDTO.getStatus())
                    .address(orderDTO.getAddress())
                    .pay(orderDTO.getPay())
                    .totalPrice(calculateTotalPrice(orderDTO.getOrderItems()))
                    .user(orderDTO.getUser())
                    .orderDate(orderDTO.getOrderDate())
                    .orderItems(orderDTO.getOrderItems())
                    .build();
        }
        return null;
    }

    public OrderDTO convToDtoWithObjects(OrderDetailedDTO orderDetailedDTO, List<OrderItem> orderItems, Optional<User> user) {

        if (orderDetailedDTO != null) {
            return OrderDTO.builder()
                    .id(orderDetailedDTO.getId())
                    .status(orderDetailedDTO.getStatus())
                    .address(orderDetailedDTO.getAddress())
                    .pay(orderDetailedDTO.getPay())
                    .totalPrice(orderDetailedDTO.getTotalPrice())
                    .user(user.get())
                    .orderDate(orderDetailedDTO.getOrderDate())
                    .orderItems(orderItems)
                    .build();
        }
        return null;
    }

    public OrderDTO convertEntToDtoWithObjects(Order order) {

        if (order != null) {
            return OrderDTO.builder()
                    .id(order.getId())
                    .status(order.getStatus())
                    .address(order.getAddress())
                    .pay(order.getPay())
                    .totalPrice(order.getTotalPrice())
                    .user(order.getUser())
                    .orderDate(order.getOrderDate())
                    .orderItems(order.getOrderItems())
                    .build();
        }
        return null;
    }

    public List<OrderDTO> convertListToDtoWithObjects(List<Order> source) {
        return source.stream()
                .map(this::convertEntToDtoWithObjects)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Long calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToLong(orderItem -> (long) (orderItem.getQuantity() * orderItem.getProduct().getPrice()))
                .sum();
    }
}