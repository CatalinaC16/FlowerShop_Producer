package com.example.flowerShop.utils.reportOrders;

import com.example.flowerShop.dto.order.OrderDTO;
import com.example.flowerShop.entity.OrderItem;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfReport implements Report {

    @Override
    public void generateReport(List<OrderDTO> orders, String filePath) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();
        document.add(new Paragraph("Order Report"));
        document.add(new Paragraph("Customer: " + orders.get(0).getUser().getName() + "\n"));

        for (OrderDTO order : orders) {
            document.add(new Paragraph("Order ID: " + order.getId()));
            for (OrderItem item : order.getOrderItems()) {
                document.add(new Paragraph("Product: " + item.getProduct().getName()));
                document.add(new Paragraph("Price: " + (item.getProduct().getPrice() * item.getQuantity())));
            }
//            document.add(new Paragraph("Pay: " + order.getPay()));
//            document.add(new Paragraph("Total: " + order.getTotalPrice()));
//            document.add(new Paragraph("Date: " + order.getOrderDate()));
            document.add(new Paragraph("------"));
        }
        document.close();
    }
}
