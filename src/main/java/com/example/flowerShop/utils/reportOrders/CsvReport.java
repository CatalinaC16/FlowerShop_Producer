package com.example.flowerShop.utils.reportOrders;

import com.example.flowerShop.dto.order.OrderDTO;
import com.example.flowerShop.entity.OrderItem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvReport implements Report {

    @Override
    public void generateReport(List<OrderDTO> orders, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("OrderID,UserID,Product,Quantity,Price,Date\n");
            for (OrderDTO order : orders) {
                for (OrderItem item : order.getOrderItems()) {
                    writer.append(String.valueOf(order.getId())).append(",")
                            .append(order.getUser().getName()).append(",")
                            .append(item.getProduct().getName()).append(",")
                            .append(String.valueOf(item.getQuantity())).append(",")
                            .append(String.valueOf(item.getProduct().getPrice() * item.getQuantity())).append(",")
                            .append(order.getOrderDate().toString()).append("\n");
                }
            }
        } catch (IOException e) {

        }
    }
}
