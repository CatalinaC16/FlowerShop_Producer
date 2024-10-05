package com.example.flowerShop.mapper;

import com.example.flowerShop.dto.shoppingCart.ShoppingCartDTO;
import com.example.flowerShop.dto.shoppingCart.ShoppingCartDetailedDTO;
import com.example.flowerShop.entity.OrderItem;
import com.example.flowerShop.entity.ShoppingCart;
import com.example.flowerShop.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ShoppingCartMapper implements Mapper<ShoppingCart, ShoppingCartDTO, ShoppingCartDetailedDTO> {
    @Override
    public ShoppingCartDetailedDTO convertToDTO(ShoppingCart shoppingCart) {

        if (shoppingCart != null) {
            return ShoppingCartDetailedDTO.builder()
                    .id(shoppingCart.getId())
                    .id_order(shoppingCart.getOrder() != null ? shoppingCart.getOrder().getId() : null)
                    .id_user(shoppingCart.getUser().getId())
                    .totalPrice(shoppingCart.getTotalPrice())
                    .id_orderItems(this.orderItemsToOrderItemIds(shoppingCart.getOrderItems()))
                    .build();
        }
        return null;
    }

    @Override
    public ShoppingCart convertToEntity(ShoppingCartDTO shoppingCartDTO) {
        if (shoppingCartDTO != null) {
            return ShoppingCart.builder()
                    .id(shoppingCartDTO.getId())
                    .order(null)
                    .user(shoppingCartDTO.getUser())
                    .orderItems(shoppingCartDTO.getOrderItems())
                    .totalPrice(calculateTotalPrice(shoppingCartDTO.getOrderItems()))
                    .build();
        }
        return null;
    }

    public static List<UUID> orderItemsToOrderItemIds(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getId)
                .collect(Collectors.toList());
    }

    private Long calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToLong(orderItem -> (long) (orderItem.getQuantity() * orderItem.getProduct().getPrice()))
                .sum();
    }

    public List<ShoppingCartDTO> convertListToDtoWithObjects(List<ShoppingCart> source) {
        return source.stream()
                .map(this::convertEntToDtoWithObjects)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public ShoppingCartDTO convToDtoWithObjects(ShoppingCartDetailedDTO shoppingCartDetailedDTO, List<OrderItem> orderItems, Optional<User> user) {

        if (shoppingCartDetailedDTO != null) {
            return ShoppingCartDTO.builder()
                    .id(shoppingCartDetailedDTO.getId())
                    .user(user.get())
                    .order(null)
                    .orderItems(orderItems)
                    .totalPrice(shoppingCartDetailedDTO.getTotalPrice())
                    .build();
        }
        return null;
    }

    public ShoppingCartDTO convertEntToDtoWithObjects(ShoppingCart shoppingCart) {

        if (shoppingCart != null) {
            return ShoppingCartDTO.builder()
                    .id(shoppingCart.getId())
                    .order(shoppingCart.getOrder())
                    .user(shoppingCart.getUser())
                    .orderItems(shoppingCart.getOrderItems())
                    .totalPrice(shoppingCart.getTotalPrice())
                    .build();
        }
        return null;
    }
}
