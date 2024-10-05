package com.example.flowerShop.utils.order;

import com.example.flowerShop.dto.order.OrderDetailedDTO;
import com.example.flowerShop.entity.Order;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@NoArgsConstructor
public class OrderUtils {

    public boolean validateOrderMap(OrderDetailedDTO orderDetailedDTO) {
        return orderDetailedDTO.getId_user() != null
                && orderDetailedDTO.getId_orderItems() != null
                && !orderDetailedDTO.getId_orderItems().isEmpty();
    }

    public static void updateOrderValues(Order orderExisting, OrderDetailedDTO orderDetailedDTO) {

        if (Objects.nonNull(orderDetailedDTO.getAddress())) {
            orderExisting.setAddress(orderDetailedDTO.getAddress());
        }

        if (Objects.nonNull(orderDetailedDTO.getPay())) {
            orderExisting.setPay(orderDetailedDTO.getPay());
        }

        if (Objects.nonNull(orderDetailedDTO.getStatus())) {
            orderExisting.setStatus(orderDetailedDTO.getStatus());
        }

        if (Objects.nonNull(orderDetailedDTO.getTotalPrice())) {
            orderExisting.setTotalPrice(orderDetailedDTO.getTotalPrice());
        }
    }

}
