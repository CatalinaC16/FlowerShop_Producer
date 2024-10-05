package com.example.flowerShop.utils.shoppingCart;

import com.example.flowerShop.dto.shoppingCart.ShoppingCartDetailedDTO;
import com.example.flowerShop.entity.OrderItem;
import com.example.flowerShop.entity.ShoppingCart;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@NoArgsConstructor
public class ShoppingCartUtils {

    public boolean validateCartMap(ShoppingCartDetailedDTO shoppingCartDetailedDTO) {
        return !Objects.equals(shoppingCartDetailedDTO.getId_user(), null);
    }

    public static void updateCartValues(ShoppingCart shoppingCart, ShoppingCartDetailedDTO shoppingCartDetailedDTO, List<OrderItem> orderItemList) {
        if (Objects.nonNull(shoppingCartDetailedDTO.getTotalPrice()) && shoppingCart.getTotalPrice() >= 1) {
            shoppingCart.setTotalPrice(shoppingCartDetailedDTO.getTotalPrice());
        }
        if (Objects.nonNull(shoppingCartDetailedDTO.getId_orderItems())) {
            updatePrice(shoppingCart, orderItemList);
            shoppingCart.setOrderItems(orderItemList);
        }
    }

    public static void updatePrice(ShoppingCart shoppingCart, List<OrderItem> orderItemList) {
        double totalPrice = 0L;
        for (OrderItem orderItem : orderItemList) {
            totalPrice += (orderItem.getQuantity() * orderItem.getProduct().getPrice());
        }
        shoppingCart.setTotalPrice((long) totalPrice);
    }

}
