package com.example.flowerShop.mapper;

import com.example.flowerShop.dto.orderItem.OrderItemDTO;
import com.example.flowerShop.dto.orderItem.OrderItemDetailedDTO;;
import com.example.flowerShop.entity.OrderItem;
import com.example.flowerShop.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderItemMapper implements Mapper<OrderItem, OrderItemDTO, OrderItemDetailedDTO> {

    @Override
    public OrderItemDetailedDTO convertToDTO(OrderItem orderItem) {

        if (orderItem != null) {
            return OrderItemDetailedDTO.builder()
                    .id(orderItem.getId())
                    .quantity(orderItem.getQuantity())
                    .id_product(orderItem.getProduct().getId())
                    .order(orderItem.getOrder())
                    .build();
        }
        return null;
    }

    @Override
    public OrderItem convertToEntity(OrderItemDTO orderItemDTO) {
        if (orderItemDTO != null) {
            return OrderItem.builder()
                    .id(orderItemDTO.getId())
                    .quantity(orderItemDTO.getQuantity())
                    .product(orderItemDTO.getProduct())
                    .build();
        }
        return null;
    }

    public OrderItemDTO convToDtoWithObjects(OrderItemDetailedDTO orderItemDetailedDTO, Optional<Product> product) {

        if (orderItemDetailedDTO != null) {
            return OrderItemDTO.builder()
                    .id(orderItemDetailedDTO.getId())
                    .quantity(orderItemDetailedDTO.getQuantity())
                    .product(product.get())
                    .build();
        }
        return null;
    }

    public OrderItemDTO convertEntToDtoWithObjects(OrderItem orderItem) {

        if (orderItem != null) {
            return OrderItemDTO.builder()
                    .id(orderItem.getId())
                    .quantity(orderItem.getQuantity())
                    .product(orderItem.getProduct())
                    .build();
        }
        return null;
    }

    public List<OrderItemDTO> convertListToDtoWithObjects(List<OrderItem> source) {
        return source.stream()
                .map(this::convertEntToDtoWithObjects)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
