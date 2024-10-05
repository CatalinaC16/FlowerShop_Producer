package com.example.flowerShop.utils.report;

import com.example.flowerShop.dto.order.OrderDTO;
import com.example.flowerShop.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SalesCalculator {

    public Map<String, Long> generateMonthlySalesReport(List<OrderDTO> orders) {
        Map<String, Long> monthlySalesReport = new HashMap<>();

        for (OrderDTO order : orders) {
            String monthYear = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            List<OrderItem> orderItems = order.getOrderItems();
            long totalProductsSold = orderItems.stream().mapToLong(OrderItem::getQuantity).sum();
            monthlySalesReport.put(monthYear, monthlySalesReport.getOrDefault(monthYear, 0L) + totalProductsSold);
        }

        return monthlySalesReport;
    }

    public Map<String, Map<String, Long>> generateMonthlySalesReportForOrderItems(List<OrderDTO> orders) {
        Map<String, Map<String, Long>> monthlySalesReport = new HashMap<>();

        for (OrderDTO order : orders) {
            String monthYear = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));

            for (OrderItem orderItem : order.getOrderItems()) {
                String productName = orderItem.getProduct().getName();
                long quantitySold = orderItem.getQuantity();

                if (!monthlySalesReport.containsKey(productName)) {
                    monthlySalesReport.put(productName, new HashMap<>());
                }
                monthlySalesReport.get(productName).put(monthYear, monthlySalesReport.get(productName).getOrDefault(monthYear, 0L) + quantitySold);
            }
        }
        return monthlySalesReport;
    }


    public Long getMoney(List<OrderDTO> orders) {
        Long money = 0L;
        for (OrderDTO order : orders) {
            money += order.getTotalPrice();
        }
        return money;
    }
}
