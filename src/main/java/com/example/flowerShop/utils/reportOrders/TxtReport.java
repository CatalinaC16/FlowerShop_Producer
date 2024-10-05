package com.example.flowerShop.utils.reportOrders;

import com.example.flowerShop.dto.order.OrderDTO;
import com.example.flowerShop.entity.OrderItem;
import com.itextpdf.text.Paragraph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TxtReport implements Report {

    @Override
    public void generateReport(List<OrderDTO> orders, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

        writer.write("Order Report\n");
        writer.write("Customer: " + orders.get(0).getUser().getName() + "\n");
        for (OrderDTO order : orders) {
            writer.write(("Order ID: " + order.getId()) + "\n");
            for (OrderItem item : order.getOrderItems()) {
                writer.write("Product: " + item.getProduct().getName() + "\n");
                writer.write("Price: " + item.getProduct().getPrice() * item.getQuantity() + "\n");
            }
            writer.write("Pay: " + order.getPay() + "\n");
            writer.write("Total: " + order.getTotalPrice() + "\n");
            writer.write("Date: " + order.getOrderDate() + "\n");
            writer.write("------\n");
        }
        writer.close();
    }
}